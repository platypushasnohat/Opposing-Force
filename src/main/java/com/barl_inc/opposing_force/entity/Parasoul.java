package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.misc.Shockwave;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.ai.goal.SpectralStareGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralFade;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class Parasoul extends AnimatedMonster implements SpectralEntity {

    private static final EntityDataAccessor<Boolean> DRUNK = SynchedEntityData.defineId(Parasoul.class, EntityDataSerializers.BOOLEAN);
    private static final float HOP_PHASE = 10.0F / 1.5F; //500ms (time on floor) / 75 ms(phase) = 6.67 (phase where spawn shockwave)

    public static final int LEAP_ANIMATION = 1;
    public static final int MOCK_ANIMATION = 2;

    public final SmoothAnimationState mockAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState leapAnimationState = new SmoothAnimationState();

    private final SpectralFade fade = new SpectralFade(this);
    private int drunkTicks;
    private float walkSpeed;
    private float prevWalkSpeed;
    private float walkPhase;
    private boolean pacified;
    private boolean bounce;

    public Parasoul(EntityType<? extends Parasoul> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ParasoulAttackGoal(this));
        this.goalSelector.addGoal(2, new DrunkFollowGoal(this));
        this.goalSelector.addGoal(3, new MockGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new SpectralStareGoal(this) {
            @Override
            public boolean canUse() {
                return !Parasoul.this.pacified && super.canUse();
            }
        });
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DRUNK, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("DrunkTicks", this.drunkTicks);
        compound.putBoolean("Pacified", this.pacified);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.drunkTicks = compound.getInt("DrunkTicks");
        this.pacified = compound.getBoolean("Pacified");
        this.entityData.set(DRUNK, this.drunkTicks > 0);
    }

    public boolean isDrunk() {
        return this.entityData.get(DRUNK);
    }

    private void setDrunk(boolean drunk) {
        this.entityData.set(DRUNK, drunk);
        SpectralSight.refreshTracking(this);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(OFItems.SAKE.get()) && !this.isDrunk()) {
            stack.consume(1, player);
            this.playSound(SoundEvents.GENERIC_DRINK, 1.0F, 0.8F);
            if (this.level().isClientSide) {
                for (int i = 0; i < 5; i++) {
                    this.level().addParticle(ParticleTypes.HEART, this.getRandomX(0.8D), this.getRandomY() + 0.5D, this.getRandomZ(0.8D), 0.0D, 0.0D, 0.0D);
                }
            } else {
                this.drunkTicks = 6000; //5 mins
                this.pacified = true;
                this.targetSelector.getAvailableGoals().stream().filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
                this.setTarget(null);
                this.setPersistenceRequired();
                this.setDrunk(true);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        float lastPhase = this.walkPhase;
        //vanilla limb swing calc
        this.prevWalkSpeed = this.walkSpeed;
        this.walkSpeed += ((this.hurtTime > 0 ? 0.0F : Math.min((float) Mth.length(this.getX() - this.xo, this.getZ() - this.zo) * 4.0F, 1.0F)) - this.walkSpeed) * 0.4F;
        this.walkPhase += this.walkSpeed;
        this.fade.tick();
        if (!this.level().isClientSide) {
            if (this.getAnimationState() == 0 && this.onGround() && this.walkSpeed > 0.2F && (int) (this.walkPhase / HOP_PHASE) > (int) (lastPhase / HOP_PHASE)) {
                this.shockwave(false);
                this.level().broadcastEntityEvent(this, (byte) 70);
            }
            if (this.bounce) {
                this.setDeltaMovement(this.getDeltaMovement().x, 0.42D, this.getDeltaMovement().z);
                this.shockwave(false);
                this.bounce = false;
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.drunkTicks > 0 && --this.drunkTicks == 0) {
            this.setDrunk(false);
        }
        if (this.level().isDay() && !this.isPersistenceRequired()) {
            this.discard();
        }
    }

    private void shockwave(boolean big) {
        if (big) {
            Shockwave.spawn(this, true, 5.0F, 10, 2.0F, 1.0F, 0.8F);
            this.playSound(SoundEvents.ANVIL_LAND, 1.2F, 0.6F);
        } else {
            Shockwave.spawn(this, false, 1.75F, 6, 0.0F, 1.1F, 0.35F).setIgnoresPlayers(this.pacified);
            this.playSound(SoundEvents.SLIME_JUMP, 0.6F, 1.3F);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 70) {
            this.walkPhase = Math.round(this.walkPhase / HOP_PHASE) * HOP_PHASE;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public float getWalkSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevWalkSpeed, this.walkSpeed);
    }

    public float getWalkPhase(float partialTicks) {
        return this.walkPhase - this.walkSpeed * (1.0F - partialTicks);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        this.bounce = fallDistance > 5.0F;
        return false;
    }

    public float getFade(float partialTicks) {
        return this.fade.get(partialTicks);
    }

    @Override
    public boolean isPerceivedBy(Entity viewer) {
        return this.isDrunk();
    }

    @Override
    public boolean isVisibleWithoutSight(Player viewer) {
        return this.fade.isVisibleTo(viewer);
    }

    @Override
    public int getFadeInTicks() {
        return 0;
    }

    @Override
    public int getStareTicksToTarget() {
        return 60;
    }

    @Override
    public boolean canBeHeardBy(Player listener) {
        return true;
    }

    @Override
    public void setupAnimationStates() {
        int state = this.getAnimationState();
        this.idleAnimationState.animateWhen(state == 0 || state == MOCK_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(state == 0, this.tickCount);
        this.mockAnimationState.animateWhen(state == MOCK_ANIMATION, this.tickCount);
        this.leapAnimationState.animateWhen(state == LEAP_ANIMATION, this.tickCount);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private static class ParasoulAttackGoal extends AttackGoal {

        private final Parasoul parasoul;
        private int cooldown;
        private Vec3 drift = Vec3.ZERO;

        public ParasoulAttackGoal(Parasoul parasoul) {
            super(parasoul);
            this.parasoul = parasoul;
        }

        @Override
        public void tick() {
            LivingEntity target = this.parasoul.getTarget();
            if (target != null) {
                if (this.attackState == 1) {
                    this.tickLeap(target);
                } else {
                    this.tickApproach(target);
                }
            }
        }

        private void tickApproach(LivingEntity target) {
            this.lookAtTarget(target, 30.0F, 30.0F);
            this.parasoul.getNavigation().moveTo(target, 1.2D);
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (this.cooldown <= 0 && this.parasoul.onGround() && this.parasoul.distanceTo(target) <= 3.0D && this.parasoul.hasLineOfSight(target)) {
                this.parasoul.getNavigation().stop();
                this.parasoul.setAnimationState(LEAP_ANIMATION);
                this.timer = 0;
                this.attackState = 1;
            }
        }

        private void tickLeap(LivingEntity target) {
            this.timer++;
            if (this.timer < 8) {
                this.lookAtTarget(target, 30.0F, 30.0F);
            } else if (this.timer == 8) {
                Vec3 offset = target.position().subtract(this.parasoul.position()).multiply(1.0D, 0.0D, 1.0D);
                this.drift = offset.normalize().scale(Math.min(offset.length(), 2.5D) / 17.0D);
                this.parasoul.playSound(SoundEvents.PHANTOM_FLAP, 1.0F, 0.8F);
            } else if (this.timer < 25) {
                this.parasoul.setDeltaMovement(this.drift.x, this.parasoul.getDeltaMovement().y, this.drift.z);
            } else if (this.timer == 25) {
                this.stomp();
            } else if (this.timer >= 35) {
                this.parasoul.setAnimationState(0);
                this.cooldown = 40;
                this.timer = 0;
                this.attackState = 0;
            }
        }

        private void stomp() {
            this.parasoul.setDeltaMovement(Vec3.ZERO);
            this.parasoul.shockwave(true);
            for (LivingEntity entity : this.parasoul.level().getEntitiesOfClass(LivingEntity.class, this.parasoul.getBoundingBox().inflate(2.0D, 0.25D, 2.0D), entity -> entity != this.parasoul && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity))) {
                if (entity.hurt(this.parasoul.damageSources().mobAttack(this.parasoul), (float) this.parasoul.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                    entity.invulnerableTime = 0;
                }
            }
        }
    }

    private static class MockGoal extends Goal {

        private final Parasoul parasoul;
        private LivingEntity watched;
        private int timer;
        private long lastMock = -1200L;

        public MockGoal(Parasoul parasoul) {
            this.parasoul = parasoul;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.parasoul.level().getGameTime() - this.lastMock >= 1200L && this.parasoul.getAnimationState() == 0 && this.parasoul.getTarget() == null && this.parasoul.onGround() && this.parasoul.getRandom().nextInt(400) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 50;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.watched = this.parasoul.level().getNearestPlayer(this.parasoul, 12.0D);
            if (this.watched == null) {
                this.watched = this.parasoul.level().getNearestEntity(LivingEntity.class, TargetingConditions.forNonCombat().range(12.0D), this.parasoul, this.parasoul.getX(), this.parasoul.getEyeY(), this.parasoul.getZ(), this.parasoul.getBoundingBox().inflate(12.0D, 4.0D, 12.0D));
            }
            this.parasoul.getNavigation().stop();
            this.parasoul.setAnimationState(MOCK_ANIMATION);
        }

        @Override
        public void stop() {
            if (this.parasoul.getAnimationState() == MOCK_ANIMATION) {
                this.parasoul.setAnimationState(0);
            }
            this.watched = null;
            this.lastMock = this.parasoul.level().getGameTime();
        }

        @Override
        public void tick() {
            this.timer++;
            if (this.watched != null) {
                this.parasoul.getLookControl().setLookAt(this.watched, 30.0F, 30.0F);
                this.parasoul.setYRot(this.parasoul.yHeadRot);
                this.parasoul.yBodyRot = this.parasoul.yHeadRot;
            }
        }
    }

    private static class DrunkFollowGoal extends Goal {

        private final Parasoul parasoul;
        private Player player;
        private float angle;
        private int recalculate;

        public DrunkFollowGoal(Parasoul parasoul) {
            this.parasoul = parasoul;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.parasoul.isDrunk() && this.parasoul.getTarget() == null && (this.player = this.parasoul.level().getNearestPlayer(this.parasoul, 16.0D)) != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.parasoul.isDrunk() && this.parasoul.getTarget() == null && this.player.isAlive() && !this.player.isSpectator() && this.parasoul.distanceToSqr(this.player) < 576.0D;
        }

        @Override
        public void start() {
            this.angle = (float) Mth.atan2(this.parasoul.getZ() - this.player.getZ(), this.parasoul.getX() - this.player.getX());
            this.recalculate = 0;
        }

        @Override
        public void stop() {
            this.player = null;
            this.parasoul.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.parasoul.getLookControl().setLookAt(this.player, 30.0F, 30.0F);
            if (--this.recalculate <= 0) {
                this.recalculate = 10;
                if (this.parasoul.distanceTo(this.player) > 7.0D) {
                    this.parasoul.getNavigation().moveTo(this.player, 1.0D);
                } else {
                    this.angle += 0.6F;
                    this.parasoul.getNavigation().moveTo(this.player.getX() + Mth.cos(this.angle) * 3.5D, this.player.getY(), this.player.getZ() + Mth.sin(this.angle) * 3.5D, 0.8D);
                }
            }
        }
    }
}