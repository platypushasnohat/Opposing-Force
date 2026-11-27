package com.barl_inc.opposing_force.entity.post_end;

import com.barl_inc.opposing_force.entity.projectile.ElectricCharge;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class Shocker extends AnimatedMonster {

    private static final EntityDataAccessor<Float> HEAD_SPIN = SynchedEntityData.defineId(Shocker.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> AGGRO = SynchedEntityData.defineId(Shocker.class, EntityDataSerializers.BOOLEAN);

    public static final byte TWEAK_EVENT = 70;

    public static final int CHARGE_ANIMATION = 1;
    public static final int SHOOT_ANIMATION = 2;

    public final SmoothAnimationState tweakAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState aggroStartAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState aggroIdleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState aggroWalkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState chargeAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState shootAnimationState = new SmoothAnimationState();

    private int tweakCooldown;
    private int tweakTicks;
    private int aggroStartTicks;
    private int chargeTicks;

    public float prevHeadSpin;
    public float headSpin;

    public float prevGlow;
    public float glow;

    public Shocker(EntityType<? extends Shocker> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 15;
        this.tweakCooldown = 100 + this.getRandom().nextInt(101);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ShockerAttackGoal(this));
        this.goalSelector.addGoal(2, new ShockerTweakGoal(this));
        this.goalSelector.addGoal(3, new ShockerHeadSpinGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.05F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ShockerBodyRotationControl(this);
    }

    @Override
    public int getMaxHeadYRot() {
        return 180;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HEAD_SPIN, 0.0F);
        builder.define(AGGRO, false);
    }

    public boolean isAggro() {
        return this.getEntityData().get(AGGRO);
    }

    public void setAggro(boolean aggro) {
        this.getEntityData().set(AGGRO, aggro);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (AGGRO.equals(accessor) && this.level().isClientSide() && this.isAggro()) {
            this.aggroStartTicks = 10;
        }
    }

    public float getHeadSpin() {
        return this.getEntityData().get(HEAD_SPIN);
    }

    public void setHeadSpin(float degrees) {
        this.getEntityData().set(HEAD_SPIN, Mth.wrapDegrees(degrees));
    }

    public float getHeadSpin(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.prevHeadSpin, this.headSpin);
    }

    public float getGlow(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevGlow, this.glow);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.prevHeadSpin = this.headSpin;
            this.headSpin = this.getHeadSpin();
            this.prevGlow = this.glow;
            this.glow += (this.getDarkness() - this.glow) * 0.1F;
            if (this.aggroStartTicks > 0) {
                this.aggroStartTicks--;
            }
            if (this.getAnimationState() == CHARGE_ANIMATION) {
                this.chargeTicks++;
                this.spawnChargeArcs();
            } else {
                this.chargeTicks = 0;
            }
        }
        if (this.tweakTicks > 0) {
            this.tweakTicks--;
        }
    }

    private void spawnChargeArcs() {
        int count = this.chargeTicks / 20;
        if (this.getRandom().nextFloat() < (this.chargeTicks % 20) / 20.0F) {
            count++;
        }
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.getYHeadRot() + this.getHeadSpin());
        Vec3 side = new Vec3(-forward.z, 0.0D, forward.x);
        for (int i = 0; i < count; i++) {
            Vec3 pos = this.position().add(forward.scale(0.3D + this.getRandom().nextDouble() * 1.4D)).add(side.scale((this.getRandom().nextDouble() - 0.5D) * 0.8D)).add(0.0D, 0.4D, 0.0D);
            this.level().addParticle(OFParticleTypes.ELECTRIC_ARC.get(), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
        }
    }

    public void shootCharge(Vec3 aim) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.getYHeadRot());
        Vec3 spawn = this.position().add(forward.scale(1.0D)).add(0.0D, 0.4D, 0.0D);
        Vec3 direction = aim.subtract(spawn).normalize();
        ElectricCharge charge = new ElectricCharge(this.level(), this);
        charge.setPos(spawn.x, spawn.y, spawn.z);
        charge.shoot(direction.x, direction.y, direction.z, 0.75F, 0.0F);
        this.level().addFreshEntity(charge);
        this.level().playSound(null, spawn.x, spawn.y, spawn.z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.HOSTILE, 1.0F, 1.4F + this.getRandom().nextFloat() * 0.2F);
    }

    public boolean isTweaking() {
        return this.tweakTicks > 0;
    }

    public float getDarkness() {
        BlockPos pos = BlockPos.containing(this.getEyePosition());
        float daylight = Mth.clamp(0.5F + 2.0F * Mth.cos(this.level().getTimeOfDay(1.0F) * Mth.TWO_PI), 0.0F, 1.0F);
        float weather = (1.0F - this.level().getRainLevel(1.0F) * 5.0F / 16.0F) * (1.0F - this.level().getThunderLevel(1.0F) * 5.0F / 16.0F);
        float sky = this.level().getBrightness(LightLayer.SKY, pos) / 15.0F * daylight * weather;
        float block = this.level().getBrightness(LightLayer.BLOCK, pos) / 15.0F;
        return 1.0F - Math.max(sky, block);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.setAggro(this.getTarget() != null);
        if (this.getTarget() != null) {
            this.setHeadSpin(Mth.approachDegrees(this.getHeadSpin(), 0.0F, 10.0F));
        } else {
            this.tweakCooldown--;
        }
        if (this.tweakCooldown <= 0) {
            this.level().broadcastEntityEvent(this, TWEAK_EVENT);
            this.tweakTicks = 40;
            this.tweakCooldown = 400 + this.getRandom().nextInt(101);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == TWEAK_EVENT) {
            this.tweakTicks = 40;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(!this.isAggro(), this.tickCount);
        this.walkAnimationState.animateWhen(!this.isAggro(), this.tickCount);
        this.aggroIdleAnimationState.animateWhen(this.isAggro(), this.tickCount);
        this.aggroWalkAnimationState.animateWhen(this.isAggro(), this.tickCount);
        this.aggroStartAnimationState.animateWhen(this.aggroStartTicks > 0, this.tickCount);
        this.tweakAnimationState.animateWhen(this.tweakTicks > 0, this.tickCount);
        this.chargeAnimationState.animateWhen(this.getAnimationState() == CHARGE_ANIMATION, this.tickCount);
        this.shootAnimationState.animateWhen(this.getAnimationState() == SHOOT_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 8.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    private static class ShockerBodyRotationControl extends BodyRotationControl {

        private final Shocker shocker;

        public ShockerBodyRotationControl(Shocker shocker) {
            super(shocker);
            this.shocker = shocker;
        }

        @Override
        public void clientTick() {
            if (this.shocker.getDeltaMovement().horizontalDistanceSqr() > 2.5E-7D) {
                this.shocker.yBodyRot = this.shocker.getYRot();
            }
        }
    }

    private static class ShockerAttackGoal extends Goal {

        private final Shocker shocker;
        private Vec3 aim = Vec3.ZERO;
        private int phase;
        private int timer;
        private int cooldown;

        public ShockerAttackGoal(Shocker shocker) {
            this.shocker = shocker;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        private boolean hasValidTarget() {
            LivingEntity target = this.shocker.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean canUse() {
            return this.hasValidTarget();
        }

        @Override
        public boolean canContinueToUse() {
            return this.hasValidTarget() || this.phase != 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.phase = 0;
            this.timer = 0;
            this.cooldown = 20;
        }

        @Override
        public void stop() {
            this.phase = 0;
            this.timer = 0;
            this.shocker.setAnimationState(0);
            this.shocker.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.shocker.getTarget();
            boolean valid = this.hasValidTarget();
            if (valid && this.phase != 2) {
                this.aim = target.getBoundingBox().getCenter();
            }
            if (valid) {
                this.shocker.getLookControl().setLookAt(target, 30.0F, 30.0F);
            } else {
                this.shocker.getLookControl().setLookAt(this.aim.x, this.aim.y, this.aim.z, 30.0F, 30.0F);
            }
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (this.phase == 1) {
                this.shocker.getNavigation().stop();
                this.timer++;
                if (this.timer >= 80) {
                    this.phase = 2;
                    this.timer = 0;
                    this.cooldown = 100;
                    this.shocker.setAnimationState(SHOOT_ANIMATION);
                    this.shocker.shootCharge(this.aim);
                }
            } else {
                if (valid) {
                    this.keepDistance(target);
                }
                if (this.phase == 2) {
                    this.timer++;
                    if (this.timer >= 20) {
                        this.phase = 0;
                        this.timer = 0;
                        this.shocker.setAnimationState(0);
                    }
                } else if (valid && this.cooldown <= 0 && this.shocker.distanceToSqr(target) < 256.0D && this.shocker.hasLineOfSight(target)) {
                    this.phase = 1;
                    this.timer = 0;
                    this.shocker.getNavigation().stop();
                    this.shocker.setAnimationState(CHARGE_ANIMATION);
                    this.shocker.playSound(SoundEvents.BEACON_ACTIVATE, 1.0F, 1.5F);
                }
            }
        }

        private void keepDistance(LivingEntity target) {
            double distance = this.shocker.distanceTo(target);
            if (distance > 4.0D) {
                this.shocker.getNavigation().moveTo(target, 1.25D);
            } else if (distance < 3.0D) {
                Vec3 away = this.shocker.position().subtract(target.position()).multiply(1.0D, 0.0D, 1.0D).normalize().scale(2.0D);
                Vec3 pos = this.shocker.position().add(away);
                this.shocker.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.25D);
            } else {
                this.shocker.getNavigation().stop();
            }
        }
    }

    private static class ShockerTweakGoal extends Goal {

        private final Shocker shocker;

        public ShockerTweakGoal(Shocker shocker) {
            this.shocker = shocker;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.shocker.isTweaking() && this.shocker.getTarget() == null;
        }

        @Override
        public void start() {
            this.shocker.getNavigation().stop();
        }
    }

    private static class ShockerHeadSpinGoal extends Goal {

        private final Shocker shocker;
        private float direction;
        private float spun;
        private int nextSpinTick;

        public ShockerHeadSpinGoal(Shocker shocker) {
            this.shocker = shocker;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.shocker.tickCount >= this.nextSpinTick && this.shocker.getTarget() == null && this.shocker.getNavigation().isDone() && this.shocker.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4D && this.shocker.getRandom().nextInt(reducedTickDelay(200)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.spun < 360.0F && this.shocker.getTarget() == null;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.direction = this.shocker.getRandom().nextBoolean() ? 1.0F : -1.0F;
            this.spun = 0.0F;
        }

        @Override
        public void stop() {
            this.nextSpinTick = this.shocker.tickCount + 400;
        }

        @Override
        public void tick() {
            float step = Math.min(3.0F, 360.0F - this.spun);
            this.spun += step;
            this.shocker.setHeadSpin(this.shocker.getHeadSpin() + step * this.direction);
        }
    }
}