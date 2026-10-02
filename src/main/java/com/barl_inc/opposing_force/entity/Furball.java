package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.LightDependentTargetGoal;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Furball extends AnimatedMonster {

    private static final int ATTACK_ANIMATION = 1;
    public static final int JUMP_ANIMATION = 2;

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sprintAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState jumpAnimationState = new SmoothAnimationState();

    public Furball(EntityType<? extends Furball> entityType, Level level) {
        super(entityType, level);
        this.lookControl = new FurballLookControl(this);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FurballAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new LightDependentTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Chicken.class, 50, true, true, this::canAttack));
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setSprinting(this.isAggressive() && this.getDeltaMovement().horizontalDistance() > 0.075D);
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(!this.isInWaterOrBubble() && this.getAnimationState() != JUMP_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(!this.isInWaterOrBubble() && !this.isSprinting() && this.getAnimationState() != JUMP_ANIMATION, this.tickCount);
        this.sprintAnimationState.animateWhen(!this.isInWaterOrBubble() && this.isSprinting() && this.getAnimationState() != JUMP_ANIMATION, this.tickCount);
        this.swimAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.jumpAnimationState.animateWhen(!this.onGround() && this.getAnimationState() == JUMP_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 8.0F, 1.0F);
        this.walkAnimation.update(speed, 0.5F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return OFSoundEvents.FURBALL_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return OFSoundEvents.FURBALL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return OFSoundEvents.FURBALL_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.7F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.1F, 1.2F);
    }

    public static boolean checkFurballSpawnRules(EntityType<Furball> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && checkMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    private static class FurballAttackGoal extends AttackGoal {

        private final Furball furball;
        private int attackCooldown;
        private int jumpCooldown;

        public FurballAttackGoal(Furball furball) {
            super(furball);
            this.furball = furball;
        }

        @Override
        public void start() {
            super.start();
            this.furball.setSprinting(false);
            this.attackCooldown = 0;
            this.jumpCooldown = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.furball.setSprinting(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.furball.getTarget();
            if (target != null) {
                double distance = this.furball.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.furball.getNavigation().stop();
                    this.lookAtTarget(target, 30.0F, 20.0F);
                    this.tickAttack(target);
                }
                else if (this.attackState == 2) {
                    this.furball.getNavigation().stop();
                    this.tickJump(target);
                }
                else {
                    this.lookAtTarget(target, 30.0F, 20.0F);
                    this.furball.getNavigation().moveTo(target, 1.0D);
                    if (this.attackCooldown > 0) {
                        this.attackCooldown--;
                    }
                    if (this.jumpCooldown > 0) {
                        this.jumpCooldown--;
                    }
                    if (this.attackCooldown <= 0 && distance <= this.getAttackReachSqr(target, 1.5D)) {
                        this.attackState = 1;
                    }
                    else if (this.jumpCooldown <= 0 && distance >= 15 && this.isWithinYRange(target, 3)) {
                        this.attackState = 2;
                    }
                }
            }
        }

        private void tickAttack(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.furball.setAnimationState(ATTACK_ANIMATION);
                this.furball.playSound(OFSoundEvents.FURBALL_ATTACK.get(), 1.0F, SinewSoundUtils.randomizePitch(this.furball));
            }
            if (this.timer == 6 && this.isInAttackRange(target, 0.6D)) {
                this.furball.doHurtTarget(target);
            }
            if (this.timer > 10) {
                this.furball.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown = 5 + this.furball.getRandom().nextInt(3);
                this.attackState = 0;
            }
        }

        private void tickJump(LivingEntity target) {
            this.timer++;
            if (this.timer <= 5) {
                this.lookAtTarget(target, 45.0F, 20.0F);
            }
            Vec3 deltaMovement = this.furball.getDeltaMovement();
            Vec3 jumpVec = new Vec3(target.getX() - this.furball.getX(), 0.0F, target.getZ() - this.furball.getZ());
            if (jumpVec.lengthSqr() > 1.0E-7D) {
                jumpVec = jumpVec.normalize().scale(1.5D).add(deltaMovement);
            }
            if (this.timer == 6 && this.furball.onGround()) {
                this.furball.setAnimationState(JUMP_ANIMATION);
                this.furball.setDeltaMovement(jumpVec.x, 0.55F, jumpVec.z);
            }
            if (this.timer > 6) {
                if (jumpVec.y * jumpVec.y < 0.03F && this.furball.getXRot() != 0.0F) {
                    this.furball.setXRot(Mth.rotLerp(0.2F, this.furball.getXRot(), 0.0F));
                } else {
                    double distance = jumpVec.horizontalDistance();
                    double xRot = Math.signum(-jumpVec.y) * Math.acos(distance / jumpVec.length()) * Mth.RAD_TO_DEG;
                    this.furball.setXRot((float) xRot);
                }
            }
            boolean stopJump = this.furball.onGround() || this.furball.onClimbable() || this.furball.isInWaterOrBubble();
            if (this.timer > 40 || (this.timer > 6 && stopJump)) {
                this.furball.setAnimationState(0);
                this.timer = 0;
                this.jumpCooldown = 50 + this.furball.getRandom().nextInt(30);
                this.attackCooldown = 5 + this.furball.getRandom().nextInt(3);
                this.attackState = 0;
            }
        }
    }

    private static class FurballLookControl extends LookControl {

        private final Furball furball;

        public FurballLookControl(Furball furball) {
            super(furball);
            this.furball = furball;
        }

        @Override
        protected boolean resetXRotOnTick() {
            return this.furball.getAnimationState() != JUMP_ANIMATION;
        }
    }
}
