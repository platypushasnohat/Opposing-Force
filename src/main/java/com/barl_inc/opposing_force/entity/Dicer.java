package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.base.OFMonster;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.animation.SmoothAnimationState;
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
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Dicer extends OFMonster {

    private static final int SLASH1_ANIMATION = 1;
    private static final int SLASH2_ANIMATION = 2;
    private static final int CROSS_SLASH_ANIMATION = 3;
    private static final int LASER_ANIMATION = 4;

    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slash1AnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState slash2AnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState crossSlashAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState laserAnimationState = new SmoothAnimationState();

    public Dicer(EntityType<? extends OFMonster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 36.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D);
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
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAttackAnimation() == 0, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAttackAnimation() == 0 && !this.isSprinting(), this.tickCount);
        this.runAnimationState.animateWhen(this.getAttackAnimation() == 0 && this.isSprinting(), this.tickCount);
        this.slash1AnimationState.animateWhen(this.getAttackAnimation() == SLASH1_ANIMATION, this.tickCount);
        this.slash2AnimationState.animateWhen(this.getAttackAnimation() == SLASH2_ANIMATION, this.tickCount);
        this.crossSlashAnimationState.animateWhen(this.getAttackAnimation() == CROSS_SLASH_ANIMATION, this.tickCount);
        this.laserAnimationState.animateWhen(this.getAttackAnimation() == LASER_ANIMATION, this.tickCount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setSprinting(this.isAggressive() && this.getDeltaMovement().horizontalDistance() > 0.05D);
    }

    private static class DicerAttackGoal extends AttackGoal {

        private final Dicer dicer;

        public DicerAttackGoal(Dicer dicer) {
            super(dicer);
            this.dicer = dicer;
        }

        @Override
        public void start() {
            super.start();
            this.dicer.setAttackAnimation(0);
        }

        @Override
        public void stop() {
            super.stop();
            this.dicer.setAttackAnimation(0);
        }

        @Override
        public void tick() {
            LivingEntity target = this.dicer.getTarget();
            if (target != null) {
                double distance = this.dicer.distanceToSqr(target.getX(), target.getY(), target.getZ());
                if (this.attackState != 2 && this.attackState != 3) {
                    this.lookAtTarget(target, 30.0F, 30.0F);
                }
                if (this.attackState == 1) {
                    this.dicer.getNavigation().stop();
                    this.tickSlash(target);
                }
                else if (this.attackState == 2) {
                    this.dicer.getNavigation().stop();
//                    this.tickCrossSlash();
                }
                else {
                    this.dicer.getNavigation().moveTo(target, 1.3D);
                    if (distance < this.getAttackReachSqr(target, 1.5D)) {
                        this.attackState = 1;
                    }
                }
            }
        }

        private void tickSlash(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.dicer.setAttackAnimation(this.dicer.getRandom().nextBoolean() ? SLASH2_ANIMATION : SLASH1_ANIMATION);
            }
            if (this.timer == 9 && this.isInAttackRange(target, 1.5D)) {
                this.dicer.doHurtTarget(target);
            }
            if (this.timer > 20) {
                this.timer = 0;
                this.attackState = 0;
                this.dicer.setAttackAnimation(0);
            }
        }

//        private void tickCrossSlash() {
//            this.timer++;
//            LivingEntity target = dicer.getTarget();
//            if (timer == 1) dicer.setPose(OPPoses.CROSS_SLASHING.get());
//            if (timer < 19) {
//                this.dicer.lookAt(target, 30F, 30F);
//                this.dicer.getLookControl().setLookAt(target, 30F, 30F);
//            }
//            if (timer == 28) dicer.addDeltaMovement(dicer.getLookAngle().scale(3.25D).multiply(1.0D, 0, 1.0D));
//            if (timer > 28 && timer < 32) {
//                this.hurtNearbyEntities();
//            }
//            if (timer > 50) {
//                this.dicer.setPose(Pose.STANDING);
//                this.timer = 0;
//                this.dicer.setAttackState(0);
//                this.dicer.crossSlashCooldown = 80 + dicer.getRandom().nextInt(50);
//            }
//        }
//
//        private void hurtNearbyEntities() {
//            List<LivingEntity> nearbyEntities = this.dicer.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.dicer, this.dicer.getBoundingBox().inflate(1.6D));
//            if (!nearbyEntities.isEmpty()) {
//                LivingEntity entity = nearbyEntities.getFirst();
//                if (!(entity instanceof Dicer)) {
//                    if (entity.hurt(entity.damageSources().mobAttack(this.dicer), (float) this.dicer.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
//                        this.dicer.playSound(OPSoundEvents.DICER_ATTACK.get(), 1.0F, 1.0F / (this.dicer.getRandom().nextFloat() * 0.4F + 0.8F));
//                    }
//                    entity.knockback(0.3F, this.dicer.position().x - entity.getX(), this.dicer.position().z - entity.getZ());
//                    if (entity.isDamageSourceBlocked(this.dicer.damageSources().mobAttack(this.dicer)) && entity instanceof Player player) {
//                        player.disableShield();
//                    }
//                }
//            }
//        }
    }
}
