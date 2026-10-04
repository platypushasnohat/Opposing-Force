package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.SwimmingMoveControl;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.ai.goal.SwimWanderGoal;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothAmphibiousNavigation;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.utils.BodyChain;
import com.platypushasnohat.sinew.entity.utils.BodyChainMob;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.List;

public class Terror extends AnimatedMonster implements BodyChainMob {

    private static final EntityDataAccessor<Boolean> HAS_LEGS = SynchedEntityData.defineId(Terror.class, EntityDataSerializers.BOOLEAN);

    public static final int ATTACK_ANIMATION = 1;
    private static final int COOLDOWN_ANIMATION = 2;
    private static final int GROW_LEGS_ANIMATION = 3;

    private static final EntityDimensions FISH_OUT_OF_WATER_DIMENSIONS = EntityDimensions.scalable(1.25F, 1.75F).withEyeHeight(1.65F);

    private final BodyChain bodyChain = new BodyChain(0.75F, 3.0F, 30.0F, 0.1F, new float[]{0.3F, 0.2F, 0.4F}, new float[]{0.3F, 0.2F, 0.4F});

    private float prevSwimPitch;
    private float swimPitch;

    private int growLegsTimer = 0;

    private boolean isLandNavigator;

    public final SmoothAnimationState sprintAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimIdleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sprintSwimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState cooldownAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState growLegsAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState hideLegsAnimationState = new SmoothAnimationState();

    public Terror(EntityType<? extends Terror> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
        this.switchNavigator(true);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TerrorAttackGoal(this));
        this.goalSelector.addGoal(2, new SwimWanderGoal(this, 1.0D, 30) {
            @Override
            public boolean canUse() {
                return super.canUse() && Terror.this.isInWater();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && Terror.this.isInWater();
            }
        });
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Terror.this.isInWater();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !Terror.this.isInWater();
            }
        });
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Mob.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_LEGS, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("HasLegs", this.hasLegs());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setHasLegs(compoundTag.getBoolean("HasLegs"));
    }

    public boolean hasLegs() {
        return this.entityData.get(HAS_LEGS);
    }

    public void setHasLegs(boolean hasLegs) {
        this.entityData.set(HAS_LEGS, hasLegs);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        if (HAS_LEGS.equals(entityDataAccessor)) {
            this.refreshDimensions();
        }
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.lookControl = new LookControl(this);
            this.navigation = this.createNavigation(this.level());
            this.isLandNavigator = true;
        } else {
            this.moveControl = new SwimmingMoveControl(this, 85, 15, 0.2F, false);
            this.lookControl = new SmoothSwimmingLookControl(this, 15);
            this.navigation = new SmoothAmphibiousNavigation(this, this.level());
            this.isLandNavigator = false;
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            return 10.0F;
        } else {
            return level.getPathfindingCostFromLightLevels(pos);
        }
    }

    @Override
    public boolean canDrownInFluidType(FluidType fluidType) {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void travel(Vec3 travelVector) {
        if (this.getAnimationState() == GROW_LEGS_ANIMATION) {
            if (this.getNavigation().getPath() != null) {
                this.getNavigation().stop();
            }
            travelVector = Vec3.ZERO;
        }
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.horizontalCollision && this.isEyeInFluid(FluidTags.WATER) && this.isPathFinding()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.hasLegs() ? FISH_OUT_OF_WATER_DIMENSIONS.scale(this.getScale()) : super.getDefaultDimensions(pose);
    }

    @Override
    public void refreshDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPos(x, y, z);
    }

    @Override
    public BodyChain getBodyChain() {
        return this.bodyChain;
    }

    @Override
    public float getRenderYaw(float partialTicks) {
        return this.bodyChain.getRenderYaw(partialTicks);
    }

    @Override
    public float getSegmentYawOffset(int index, float partialTicks) {
        return this.bodyChain.getSegmentYawOffset(index, partialTicks);
    }

    @Override
    public float getSegmentPitchOffset(int index, float partialTicks) {
        return this.bodyChain.getSegmentPitchOffset(index, partialTicks, this.getSwimPitch(partialTicks));
    }

    public float getRoll(float partialTicks) {
        return this.bodyChain.getRoll(partialTicks);
    }

    public float getSwimPitch(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevSwimPitch, this.swimPitch);
    }

    @Override
    public void remove(RemovalReason reason) {
        OpposingForce.PROXY.clearSoundCacheFor(this);
        super.remove(reason);
    }

    @Override
    public void tick() {
        super.tick();

        final boolean canLandNavigate = !this.isInWater() && this.hasLegs();
        if (!canLandNavigate && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (canLandNavigate && !this.isLandNavigator) {
            this.switchNavigator(true);
        }

        this.prevSwimPitch = this.swimPitch;
        float targetPitch = 0.0F;
        if (this.isInWater()) {
            double dx = this.getX() - this.xo;
            double dy = this.getY() - this.yo;
            double dz = this.getZ() - this.zo;
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            double speed = Math.sqrt(horizontal * horizontal + dy * dy);
            float speedFactor = (float) Mth.clamp((speed - 0.01D) / (0.05D - 0.01D), 0.0D, 1.0D);
            if (speedFactor > 0.0F) {
                float angle = (float) (-(Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG));
                targetPitch = Mth.clamp(angle, -85.0F, 85.0F) * speedFactor;
            }
        }
        this.swimPitch += (targetPitch - this.swimPitch) * 0.15F;
        this.bodyChain.tick(this.yBodyRot, this.swimPitch, targetPitch);

        if (!this.hasLegs() && !this.isInWaterOrBubble() && this.onGround()) {
            if (this.getAnimationState() != GROW_LEGS_ANIMATION) {
                this.setAnimationState(GROW_LEGS_ANIMATION);
                this.growLegsTimer = 40;
            }
        }
        if (this.growLegsTimer > 0) {
            this.growLegsTimer--;
            if (this.growLegsTimer == 0 && this.getAnimationState() == GROW_LEGS_ANIMATION) {
                this.setAnimationState(0);
                this.setHasLegs(true);
            }
        }

        if (this.isInWaterOrBubble() && this.hasLegs()) {
            this.setHasLegs(false);
        }

        if (this.getAnimationState() == ATTACK_ANIMATION && this.isSprinting()) {
            OpposingForce.PROXY.playSound(this, (byte) 3);
        }
    }

    @Override
    public void setupAnimationStates() {
        boolean outsideWater = !this.isInWaterOrBubble() && this.getAnimationState() != GROW_LEGS_ANIMATION;
        this.idleAnimationState.animateWhen(outsideWater, this.tickCount);
        this.walkAnimationState.animateWhen(outsideWater && !this.isSprinting(), this.tickCount);
        this.sprintAnimationState.animateWhen(outsideWater && this.isSprinting(), this.tickCount);
        this.hideLegsAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
        this.swimIdleAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
        this.swimAnimationState.animateWhen(this.isInWaterOrBubble() && !this.isSprinting(), this.tickCount);
        this.sprintSwimAnimationState.animateWhen(this.isInWaterOrBubble() && this.isSprinting(), this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.cooldownAnimationState.animateWhen(this.getAnimationState() == COOLDOWN_ANIMATION, this.tickCount);
        this.growLegsAnimationState.animateWhen(!this.isInWaterOrBubble() && this.getAnimationState() == GROW_LEGS_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, this.isInWater() ? this.getY() - this.yo : 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 6.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return OFSoundEvents.TERROR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return OFSoundEvents.TERROR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return OFSoundEvents.TERROR_DEATH.get();
    }

    private static class TerrorAttackGoal extends AttackGoal {

        private final Terror terror;
        private int attackCooldown;

        public TerrorAttackGoal(Terror terror) {
            super(terror);
            this.terror = terror;
        }

        @Override
        public void start() {
            super.start();
            this.terror.setSprinting(false);
            this.attackCooldown();
        }

        @Override
        public void stop() {
            super.stop();
            this.terror.setSprinting(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.terror.getTarget();
            if (target != null) {
                double distance = this.terror.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.terror.getNavigation().stop();
                    this.tickCharge(target);
                }
                else if (this.attackState == 2) {
                    this.terror.getNavigation().stop();
                    this.tickStopCharge();
                }
                else {
                    if (this.terror.getAnimationState() != GROW_LEGS_ANIMATION) {
                        this.lookAtTarget(target, 25.0F, 25.0F);
                        this.terror.getNavigation().moveTo(target, 1.3D);
                        if (this.attackCooldown > 0) {
                            this.attackCooldown--;
                        }
                        if (this.attackCooldown <= 0 && distance <= 100) {
                            this.attackState = 1;
                        }
                    } else {
                        this.terror.getNavigation().stop();
                    }
                }
            }
        }

        private void tickCharge(LivingEntity target) {
            this.timer++;
            double distance = this.terror.distanceTo(target);
            if (this.timer == 1) {
                this.terror.playSound(OFSoundEvents.TERROR_SAW_START.get(), 1.0F, SinewSoundUtils.randomizePitch(this.terror));
                this.terror.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 15) {
                this.terror.setSprinting(true);
            }
            if (this.timer <= 15) {
                this.lookAtTarget(target, 45.0F, 45.0F);
            }
            if (this.timer > 15) {
                Vec3 chargeDirection = new Vec3(target.getX() - this.terror.getX(), target.getY() - this.terror.getY(), target.getZ() - this.terror.getZ()).normalize();
                float desiredYaw = (float) (Mth.atan2(chargeDirection.z, chargeDirection.x) * Mth.RAD_TO_DEG) - 90.0F;
                this.terror.setYRot(Mth.approachDegrees(this.terror.getYRot(), desiredYaw, 0.5F));
                this.terror.yBodyRot = this.terror.getYRot();
                this.terror.yHeadRot = this.terror.getYRot();
                float yawRad = this.terror.getYRot() * Mth.DEG_TO_RAD;
                float speed = 0.375F;
                Vec3 forward = new Vec3(-Mth.sin(yawRad), this.terror.isInWater() ? chargeDirection.y : 0.0F, Mth.cos(yawRad));
                this.terror.setDeltaMovement(forward.multiply(speed, 0.04F, speed).add(0.0F, this.terror.getDeltaMovement().y, 0.0F));
                this.hurtNearbyEntities();
            }
            BlockHitResult hitResult = this.terror.level().clip(new ClipContext(this.terror.position().add(0.0F, this.terror.getBbHeight() - 0.2F, 0.0F), this.terror.position().add(0.0F, this.terror.getBbHeight() - 0.2F, 0.0F).add(this.terror.getLookAngle().scale(1.1D)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.terror));
            BlockPos hitPos = hitResult.getBlockPos();
            BlockState state = this.terror.level().getBlockState(hitPos);
            SoundType soundType = state.getSoundType(this.terror.level(), hitPos, this.terror);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                this.terror.level().playSound(null, hitPos.getX(), hitPos.getY(), hitPos.getZ(), soundType.getBreakSound(), this.terror.getSoundSource(), 1.0F, 0.9F);
                this.timer = 0;
                this.terror.setSprinting(false);
                this.attackState = 2;
            }
            if (this.timer >= 5 && this.terror.getAnimationState() == GROW_LEGS_ANIMATION) {
                this.timer = 0;
                this.terror.setSprinting(false);
                this.attackCooldown();
                this.attackState = 0;
            }
            if (this.timer > 80 || (this.timer > 25 && distance > 10)) {
                this.timer = 0;
                this.terror.setSprinting(false);
                this.terror.setAnimationState(0);
                this.attackState = 2;
            }
        }

        private void tickStopCharge() {
            this.timer++;
            if (this.timer == 1) {
                this.terror.playSound(OFSoundEvents.TERROR_SAW_END.get(), 1.0F, SinewSoundUtils.randomizePitch(this.terror));
                this.terror.setAnimationState(COOLDOWN_ANIMATION);
            }
            if (this.timer > 50) {
                this.terror.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown();
                this.attackState = 0;
            }
        }

        private void attackCooldown() {
            this.attackCooldown = 10 + this.terror.getRandom().nextInt(10);
        }

        private void hurtNearbyEntities() {
            AABB attackBox = this.terror.getBoundingBox().move(this.terror.getLookAngle().normalize()).inflate(0.2D, 0.0D, 0.2D);
            List<LivingEntity> nearbyEntities = this.terror.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.terror, attackBox);
            if (!nearbyEntities.isEmpty()) {
                nearbyEntities.stream().filter(entity -> entity != this.terror).limit(4).forEach(entity -> {
                    this.terror.doHurtTarget(entity);
                    float yawRad = this.terror.getYRot() * Mth.DEG_TO_RAD;
                    entity.knockback(0.4F, Mth.sin(yawRad), -Mth.cos(yawRad));
                    if (entity.isDamageSourceBlocked(this.terror.damageSources().mobAttack(this.terror)) && entity instanceof Player player) {
                        player.disableShield();
                        player.knockback(0.2F, Mth.sin(yawRad), (-Mth.cos(yawRad)));
                        player.hurtMarked = true;
                        this.terror.addDeltaMovement(new Vec3(0, 0.25D, 0));
                        this.terror.addDeltaMovement(this.terror.getLookAngle().scale(1.0D).multiply(-0.5D, 0, -0.5D));
                        this.timer = 0;
                        this.terror.setSprinting(false);
                        this.attackState = 2;
                    }
                });
            }
        }
    }
}
