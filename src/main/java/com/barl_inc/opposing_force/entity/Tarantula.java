package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.LightDependentTargetGoal;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Tarantula extends AnimatedMonster {

    private static final int ATTACK_ANIMATION = 1;
    private static final int SLAM_ANIMATION = 2;
    private static final int JUMP_ANIMATION = 3;

    public final SmoothAnimationState sprintAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = new SmoothAnimationState(0.25F);
    public final SmoothAnimationState jumpAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attack1AnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attack2AnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slamAnimationState = new SmoothAnimationState();

    private boolean attackAlt = false;

    public Tarantula(EntityType<? extends Tarantula> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 25;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 140.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 8.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TarantulaAttackGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Armadillo.class, 6.0F, 1.0D, 1.2D, (entity) -> !((Armadillo) entity).isScared()));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Bewilder.class));
        this.targetSelector.addGoal(1, new LightDependentTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(2, new LightDependentTargetGoal<>(this, IronGolem.class, true, true));
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 10;
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() != JUMP_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(!this.isSprinting() && this.getAnimationState() != JUMP_ANIMATION, this.tickCount);
        this.sprintAnimationState.animateWhen(this.isSprinting() && this.getAnimationState() != JUMP_ANIMATION, this.tickCount);
        this.jumpAnimationState.animateWhen(!this.onGround() && this.getAnimationState() == JUMP_ANIMATION, this.tickCount);
        this.attack1AnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION && !this.attackAlt, this.tickCount);
        this.attack2AnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION && this.attackAlt, this.tickCount);
        this.slamAnimationState.animateWhen(this.getAnimationState() == SLAM_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 6.0F, 1.0F);
        this.walkAnimation.update(speed, 0.2F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ANIMATION_STATE.equals(key)) {
            if (this.getAnimationState() == ATTACK_ANIMATION) {
                this.attackAlt = this.getRandom().nextBoolean();
            }
        }
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
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 0.7F);
    }

    private static class TarantulaAttackGoal extends AttackGoal {

        private final Tarantula tarantula;
        private int attackCooldown;
        private int slamCooldown;
        private int jumpCooldown;

        public TarantulaAttackGoal(Tarantula tarantula) {
            super(tarantula);
            this.tarantula = tarantula;
        }

        @Override
        public void start() {
            super.start();
            this.attackCooldown = 0;
            this.slamCooldown = 50;
            this.jumpCooldown = 20;
        }

        @Override
        public void tick() {
            LivingEntity target = this.tarantula.getTarget();
            if (target != null) {
                double distance = this.tarantula.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.tarantula.getNavigation().stop();
                    this.lookAtTarget(target, 10.0F, 10.0F);
                    this.tickAttack(target);
                }
                else if (this.attackState == 2) {
                    this.tarantula.getNavigation().stop();
                    this.lookAtTarget(target, 10.0F, 10.0F);
                    this.tickSlam();
                }
                else if (this.attackState == 3) {
                    this.tarantula.getNavigation().stop();
                    this.tickJump(target);
                }
                else {
                    this.lookAtTarget(target, 10.0F, 10.0F);
                    this.tarantula.getNavigation().moveTo(target, 1.3D);
                    if (this.attackCooldown > 0) {
                        this.attackCooldown--;
                    }
                    if (this.slamCooldown > 0) {
                        this.slamCooldown--;
                    }
                    if (this.jumpCooldown > 0) {
                        this.jumpCooldown--;
                    }
                    if (this.attackCooldown <= 0 && this.slamCooldown > 0 && distance <= this.getAttackReachSqr(target, 1.2D)) {
                        this.attackState = 1;
                    }
                    else if (this.slamCooldown <= 0 && distance <= this.getAttackReachSqr(target, 1.2D)) {
                        this.attackState = 2;
                    }
                    else if (this.jumpCooldown <= 0 && distance >= 42 && distance < 100 && this.isWithinYRange(target, 1) && this.isPathClear(target)) {
                        this.attackState = 3;
                    }
                }
            }
        }

        private void tickAttack(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.tarantula.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 13 && this.isInAttackRange(target, 0.9D)) {
                this.tarantula.doHurtTarget(target);
            }
            if (this.timer > 20) {
                this.tarantula.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown();
                this.attackState = 0;
            }
        }

        private void tickSlam() {
            this.timer++;
            if (this.timer == 1) {
                this.tarantula.setAnimationState(SLAM_ANIMATION);
            }
            if (this.timer == 23) {
                this.hurtNearbyEntities();
            }
            if (this.timer > 40) {
                this.tarantula.setAnimationState(0);
                this.timer = 0;
                this.slamCooldown = 70 + this.tarantula.getRandom().nextInt(50);
                this.attackState = 0;
            }
        }

        private void tickJump(LivingEntity target) {
            this.timer++;
            if (this.timer <= 14) {
                this.lookAtTarget(target, 25.0F, 25.0F);
            }
            Vec3 deltaMovement = this.tarantula.getDeltaMovement();
            Vec3 jumpVec = new Vec3(target.getX() - this.tarantula.getX(), 0.0F, target.getZ() - this.tarantula.getZ());
            if (jumpVec.lengthSqr() > 1.0E-7D) {
                jumpVec = jumpVec.normalize().scale(1.6D).add(deltaMovement);
            }
            if (this.timer == 15 && this.tarantula.onGround()) {
                this.tarantula.setAnimationState(JUMP_ANIMATION);
                this.tarantula.setDeltaMovement(jumpVec.x, 0.5F, jumpVec.z);
            }
            boolean stopJump = this.tarantula.onGround() || this.tarantula.onClimbable() || this.tarantula.isInWaterOrBubble();
            if (this.timer > 60 || (this.timer > 15 && stopJump)) {
                this.tarantula.setAnimationState(0);
                this.timer = 0;
                this.jumpCooldown = 100 + this.tarantula.getRandom().nextInt(100);
                this.attackCooldown();
                this.slamCooldown = 0;
                this.attackState = 0;
            }
        }

        private void attackCooldown() {
            this.attackCooldown = 10 + this.tarantula.getRandom().nextInt(5);
        }

        private void hurtNearbyEntities() {
            AABB attackBox = this.tarantula.getBoundingBox().move(this.tarantula.getLookAngle().normalize().multiply(2.0D, 0.0D, 2.0D)).inflate(1.5D, -0.25D, 1.5D);
            List<LivingEntity> nearbyEntities = this.tarantula.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.tarantula, attackBox);
            if (!nearbyEntities.isEmpty()) {
                nearbyEntities.stream().filter(entity -> entity != this.tarantula).forEach(entity -> {
                    entity.hurt(this.tarantula.damageSources().mobAttack(this.tarantula), (float) this.tarantula.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F);
                    float yawRad = this.tarantula.getYRot() * Mth.DEG_TO_RAD;
                    entity.knockback(1.5F, Mth.sin(yawRad), -Mth.cos(yawRad));
                    if (entity.isDamageSourceBlocked(this.tarantula.damageSources().mobAttack(this.tarantula)) && entity instanceof Player player) {
                        player.disableShield();
                        player.knockback(1.0F, Mth.sin(yawRad), (-Mth.cos(yawRad)));
                        player.hurtMarked = true;
                    }
                });
            }
        }

        private boolean isPathClear(LivingEntity target) {
            double dx = target.getZ() - this.tarantula.getZ();
            double dz = target.getX() - this.tarantula.getX();
            double d2 = dx / dz;
            for (int i = 0; i < 6; i++) {
                double d3 = d2 == 0.0D ? 0.0D : dx * (i / 6.0D);
                double d4 = d2 == 0.0D ? dz * (i / 6.0D) : d3 / d2;
                for (int j = 1; j < 4; j++) {
                    if (!this.tarantula.level().getBlockState(BlockPos.containing(this.tarantula.getX() + d4, this.tarantula.getY() + (double) j, this.tarantula.getZ() + d3)).canBeReplaced()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
