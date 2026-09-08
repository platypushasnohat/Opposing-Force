package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.misc.DicerLaser;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.utils.SinewParticleUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Dicer extends AnimatedMonster {

    private static final EntityDataAccessor<Boolean> HAS_AFTERIMAGE = SynchedEntityData.defineId(Dicer.class, EntityDataSerializers.BOOLEAN);

    private static final int SLASH1_ANIMATION = 1;
    private static final int SLASH2_ANIMATION = 2;
    private static final int CROSS_SLASH_ANIMATION = 3;
    private static final int LASER_ANIMATION = 4;

    public float prevLaserProgress;
    public float laserProgress;

    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slash1AnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState slash2AnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState crossSlashAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState laserAnimationState = new SmoothAnimationState();

    public Dicer(EntityType<? extends AnimatedMonster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
                .add(Attributes.ARMOR, 6.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DicerAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, false, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_AFTERIMAGE, false);
    }

    public boolean hasAfterimage() {
        return this.getEntityData().get(HAS_AFTERIMAGE);
    }

    public void setHasAfterimage(boolean flag) {
        this.getEntityData().set(HAS_AFTERIMAGE, flag);
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() == 0 && !this.isSprinting(), this.tickCount);
        this.runAnimationState.animateWhen(this.getAnimationState() == 0 && this.isSprinting(), this.tickCount);
        this.slash1AnimationState.animateWhen(this.getAnimationState() == SLASH1_ANIMATION, this.tickCount);
        this.slash2AnimationState.animateWhen(this.getAnimationState() == SLASH2_ANIMATION, this.tickCount);
        this.crossSlashAnimationState.animateWhen(this.getAnimationState() == CROSS_SLASH_ANIMATION, this.tickCount);
        this.laserAnimationState.animateWhen(this.getAnimationState() == LASER_ANIMATION, this.tickCount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setSprinting(this.isAggressive() && this.getDeltaMovement().horizontalDistance() > 0.05D);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevLaserProgress = this.laserProgress;
        if (this.getAnimationState() == LASER_ANIMATION && this.laserProgress < 5.0F) {
            this.laserProgress++;
        }
        if (this.getAnimationState() != LASER_ANIMATION && this.laserProgress > 0.0F) {
            this.laserProgress--;
        }
        if (this.level().isClientSide && this.isAlive() && this.hasAfterimage()) {
            SinewParticleUtils.createAfterImage(this, Vec3.directionFromRotation(0.0F, this.yHeadRot));
        }
    }

    public float getLaserProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevLaserProgress, this.laserProgress) * 0.2F;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (super.doHurtTarget(entity)) {
            this.playSound(OFSoundEvents.DICER_ATTACK.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return OFSoundEvents.DICER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return OFSoundEvents.DICER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return OFSoundEvents.DICER_DEATH.get();
    }

    private static class DicerAttackGoal extends AttackGoal {

        private final Dicer dicer;
        private DicerLaser laser;
        private int crossSlashCooldown;
        private int laserCooldown;

        public DicerAttackGoal(Dicer dicer) {
            super(dicer);
            this.dicer = dicer;
        }

        @Override
        public void start() {
            super.start();
            this.dicer.setAnimationState(0);
            this.crossSlashCooldown = 30 + this.dicer.getRandom().nextInt(30);
            this.laserCooldown = 50 + this.dicer.getRandom().nextInt(50);
            this.dicer.setHasAfterimage(false);
            if (this.laser != null) {
                this.laser.discard();
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.dicer.setAnimationState(0);
            this.crossSlashCooldown = 30 + this.dicer.getRandom().nextInt(30);
            this.laserCooldown = 50 + this.dicer.getRandom().nextInt(50);
            this.dicer.setHasAfterimage(false);
            if (this.laser != null) {
                this.laser.discard();
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.dicer.getTarget();
            if (target != null) {
                double distance = this.dicer.distanceToSqr(target.getX(), target.getY(), target.getZ());
                if (this.attackState != 2 && this.attackState != 3) {
                    this.lookAtTarget(target, 30.0F, 30.0F);
                }
                if (this.crossSlashCooldown > 0) {
                    this.crossSlashCooldown--;
                }
                if (this.laserCooldown > 0) {
                    this.laserCooldown--;
                }

                if (this.attackState == 1) {
                    this.dicer.getNavigation().stop();
                    this.tickSlash(target);
                }
                else if (this.attackState == 2) {
                    this.dicer.getNavigation().stop();
                    this.tickCrossSlash(target);
                }
                else if (this.attackState == 3) {
                    this.dicer.getNavigation().stop();
                    this.tickLaser(target);
                }
                else {
                    if (this.dicer.tickCount % 5 == 0) {
                        this.dicer.getNavigation().moveTo(target, 1.25D);
                    }
                    if (distance < this.getAttackReachSqr(target)) {
                        this.attackState = 1;
                    }
                    else if (this.crossSlashCooldown == 0 && distance < 64.0D) {
                        this.attackState = 2;
                    }
                    else if (this.laserCooldown == 0 && distance < 256.0D && distance > 9.0D) {
                        this.attackState = 3;
                    }
                }
            }
        }

        private void tickSlash(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.dicer.setAnimationState(this.dicer.getRandom().nextBoolean() ? SLASH2_ANIMATION : SLASH1_ANIMATION);
            }
            if (this.timer == 9 && this.isInAttackRange(target, 1.5D)) {
                this.dicer.doHurtTarget(target);
            }
            if (this.timer > 20) {
                this.timer = 0;
                this.attackState = 0;
                this.dicer.setAnimationState(0);
            }
        }

        private void tickCrossSlash(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.dicer.setAnimationState(CROSS_SLASH_ANIMATION);
            }
            if (this.timer < 19) {
                this.lookAtTarget(target, 60.0F, 30.0F);
            }
            if (timer == 28) {
                this.dicer.setHasAfterimage(true);
                this.dicer.addDeltaMovement(this.dicer.getLookAngle().scale(5.0D).multiply(1.0D, 0.0D, 1.0D));
            }
            if (this.timer > 28 && this.timer < 32) {
                this.hurtNearbyEntities();
            }
            if (this.timer < 40) {
                this.dicer.setDeltaMovement(this.dicer.getDeltaMovement().add(0.0D, -0.3D, 0.0D));
            }
            if (this.timer == 40) {
                this.dicer.setHasAfterimage(false);
            }
            if (this.timer > 50) {
                this.timer = 0;
                this.attackState = 0;
                this.crossSlashCooldown = 100 + this.dicer.getRandom().nextInt(50);
                this.dicer.setAnimationState(0);
            }
        }

        private void tickLaser(LivingEntity target) {
            this.timer++;
            if (this.timer == 6) {
                this.dicer.playSound(OFSoundEvents.DICER_LASER_START.get(), 2.0F, 1.0F);
            }
            if (this.timer == 10) {
                this.dicer.setAnimationState(LASER_ANIMATION);
            }
            if (this.timer < 30) {
                this.lookAtTarget(target, 30.0F, 30.0F);
            }
            if (this.timer == 30) {
                Level level = this.dicer.level();
                float distance = 0.2F;
                int duration = 72;
                this.laser = new DicerLaser(OFEntities.DICER_LASER.get(), level, this.dicer, this.dicer.getX() + distance * Math.sin(-this.dicer.getYRot() * Mth.DEG_TO_RAD), this.dicer.getEyeY(), this.dicer.getZ() + distance * Math.cos(-this.dicer.getYRot() * Mth.DEG_TO_RAD), (this.dicer.yHeadRot + 90.0F) * Mth.DEG_TO_RAD, -this.dicer.getXRot() * Mth.DEG_TO_RAD, duration);
                level.addFreshEntity(this.laser);
            }

            if (this.timer > 30) {
                this.dicer.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ(), 0.95F, 90.0F);
            }
            if (this.timer == 78) {
                this.dicer.playSound(OFSoundEvents.DICER_LASER_END.get(), 2.0F, 1.0F);
            }
            if (this.timer > 130) {
                this.timer = 0;
                this.attackState = 0;
                this.laserCooldown = 150 + this.dicer.getRandom().nextInt(100);
                this.dicer.setAnimationState(0);
            }
        }

        private void hurtNearbyEntities() {
            List<LivingEntity> nearbyEntities = this.dicer.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.dicer, this.dicer.getBoundingBox().inflate(1.8D));
            if (!nearbyEntities.isEmpty()) {
                nearbyEntities.stream().filter(entity -> entity != this.dicer).limit(8).forEach(entity -> {
                    this.dicer.doHurtTarget(entity);
                    if (entity.isDamageSourceBlocked(this.dicer.damageSources().mobAttack(this.dicer)) && entity instanceof Player player) {
                        player.disableShield();
                    }
                });
            }
        }
    }
}
