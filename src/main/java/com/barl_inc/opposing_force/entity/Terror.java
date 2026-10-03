package com.barl_inc.opposing_force.entity;

import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.SwimmingMoveControl;
import com.platypushasnohat.sinew.entity.ai.goal.SwimWanderGoal;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothAmphibiousNavigation;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.utils.BodyChain;
import com.platypushasnohat.sinew.entity.utils.BodyChainMob;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

public class Terror extends AnimatedMonster implements BodyChainMob {

    private static final EntityDataAccessor<Boolean> HAS_LEGS = SynchedEntityData.defineId(Terror.class, EntityDataSerializers.BOOLEAN);

    private static final int ATTACK_ANIMATION = 1;
    private static final int COOLDOWN_ANIMATION = 2;
    private static final int GROW_LEGS_ANIMATION = 3;

    private final BodyChain bodyChain = new BodyChain(1.0F, 5.0F, 30.0F, 0.1F, new float[]{0.35F, 0.16F, 0.3F}, new float[]{0.24F, 0.12F, 0.2F});

    private float prevSwimPitch;
    private float swimPitch;

    private int growLegsTimer = 0;

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
        this.moveControl = new SwimmingMoveControl(this, 85, 20, 0.2F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new SwimWanderGoal(this, 1.0D, 30) {
            @Override
            public boolean canUse() {
                return Terror.this.isInWater() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return Terror.this.isInWater() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return !Terror.this.isInWater() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !Terror.this.isInWater() && super.canContinueToUse();
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

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothAmphibiousNavigation(this, level);
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
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
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
    public void tick() {
        super.tick();

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
        float speed = Math.min(length * 8.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
    }
}
