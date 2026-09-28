package com.barl_inc.opposing_force.entity;

import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Bewilder extends AnimatedMonster {

    private static final int WARN_ANIMATION = 1;
    private static final int STOP_ANIMATION = 2;
    private static final int STUN_ANIMATION = 3;

    public final SmoothAnimationState warnAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState chargeAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState stopAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState stunAnimationState = new SmoothAnimationState();

    public Bewilder(EntityType<? extends Bewilder> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 12.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BewilderAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Mob.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Bewilder.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            amount *= 0.75F;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() == 0 && !this.isSprinting(), this.tickCount);
        this.chargeAnimationState.animateWhen(this.getAnimationState() == 0 && this.isSprinting(), this.tickCount);
        this.warnAnimationState.animateWhen(this.getAnimationState() == WARN_ANIMATION, this.tickCount);
        this.stopAnimationState.animateWhen(this.getAnimationState() == STOP_ANIMATION, this.tickCount);
        this.stunAnimationState.animateWhen(this.getAnimationState() == STUN_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 10.0F, 1.0F);
        this.walkAnimation.update(speed, 0.2F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 0.8F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    public static boolean checkBewilderSpawnRules(EntityType<Bewilder> bewilder, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Monster.checkMonsterSpawnRules(bewilder, level, spawnType, pos, random);
    }

    private static class BewilderAttackGoal extends AttackGoal {

        private final Bewilder bewilder;
        private int attackCooldown;

        public BewilderAttackGoal(Bewilder bewilder) {
            super(bewilder);
            this.bewilder = bewilder;
        }

        @Override
        public void start() {
            super.start();
            this.bewilder.setAnimationState(0);
            this.bewilder.setSprinting(false);
            this.attackCooldown();
        }

        @Override
        public void stop() {
            super.stop();
            this.bewilder.setAnimationState(0);
            this.bewilder.setSprinting(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.bewilder.getTarget();
            if (target != null) {
                double distance = this.bewilder.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.lookAtTarget(target, 20.0F, 20.0F);
                    this.bewilder.getNavigation().stop();
                    this.tickChargeStart();
                }
                else if (this.attackState == 2) {
                    this.bewilder.getNavigation().stop();
                    this.tickCharge(target);
                }
                else if (this.attackState == 3) {
                    this.bewilder.getNavigation().stop();
                    this.tickStopCharge();
                }
                else if (this.attackState == 4) {
                    this.bewilder.getNavigation().stop();
                    this.tickStun();
                }
                else {
                    this.lookAtTarget(target, 20.0F, 20.0F);
                    if (this.attackCooldown > 0) {
                        this.attackCooldown--;
                    }
                    if (this.bewilder.tickCount % 5 == 0) {
                        this.bewilder.getNavigation().moveTo(target, 1.2D);
                    }
                    if (this.attackCooldown <= 0 && distance < 144.0D) {
                        this.attackState = 1;
                    }
                }
            }
        }

        private void tickChargeStart() {
            this.timer++;
            if (this.timer == 1) {
                this.bewilder.setAnimationState(WARN_ANIMATION);
            }
            if (this.timer == 18) {
                this.bewilder.playSound(SoundEvents.COW_AMBIENT, 1.2F, SinewSoundUtils.randomizePitch(this.bewilder));
            }
            if (this.timer > 50) {
                this.timer = 0;
                this.bewilder.setAnimationState(0);
                this.attackState = 2;
            }
        }

        private void tickCharge(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.bewilder.setSprinting(true);
            }
            double dx = target.getX() - this.bewilder.getX();
            double dz = target.getZ() - this.bewilder.getZ();
            float desiredYaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            this.bewilder.setYRot(Mth.approachDegrees(this.bewilder.getYRot(), desiredYaw, 1.9F));
            this.bewilder.yBodyRot = this.bewilder.getYRot();
            this.bewilder.yHeadRot = this.bewilder.getYRot();
            float yawRad = this.bewilder.getYRot() * Mth.DEG_TO_RAD;
            Vec3 forward = new Vec3(-Mth.sin(yawRad), 0.0F, Mth.cos(yawRad));
            this.bewilder.setDeltaMovement(forward.scale(0.4D).add(0.0F, this.bewilder.getDeltaMovement().y, 0.0F));
            this.hurtNearbyEntities();
            BlockHitResult hitResult = this.bewilder.level().clip(new ClipContext(this.bewilder.position().add(0.0F, this.bewilder.getBbHeight() - 0.2F, 0.0F), this.bewilder.position().add(0.0F, this.bewilder.getBbHeight() - 0.2F, 0.0F).add(this.bewilder.getLookAngle().scale(1.2D)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.bewilder));
            BlockPos hitPos = hitResult.getBlockPos();
            BlockState state = this.bewilder.level().getBlockState(hitPos);
            SoundType soundType = state.getSoundType(this.bewilder.level(), hitPos, this.bewilder);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                this.bewilder.level().playSound(null, hitPos.getX(), hitPos.getY(), hitPos.getZ(), soundType.getBreakSound(), SoundSource.HOSTILE, 1.0F, 0.8F);
                this.timer = 0;
                this.bewilder.setSprinting(false);
                this.attackState = 4;
            }
            else if (this.timer > 100) {
                this.timer = 0;
                this.bewilder.setSprinting(false);
                this.attackState = 3;
            }
        }

        private void tickStopCharge() {
            this.timer++;
            if (this.timer == 1) {
                this.bewilder.setAnimationState(STOP_ANIMATION);
            }
            if (this.timer > 20) {
                this.bewilder.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown();
                this.attackState = 0;
            }
        }

        private void tickStun() {
            this.timer++;
            int stunTime = 40;
            if (this.timer == 1) {
                stunTime = 50 + this.bewilder.getRandom().nextInt(50);
                this.bewilder.setAnimationState(STUN_ANIMATION);
                this.bewilder.addDeltaMovement(new Vec3(0, 0.3D, 0));
                this.bewilder.addDeltaMovement(this.bewilder.getLookAngle().scale(1.0D).multiply(-0.5D, 0, -0.5D));
                this.bewilder.hurtMarked = true;
            }
            if (this.timer > stunTime) {
                this.bewilder.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown();
                this.attackState = 0;
            }
        }

        private void attackCooldown() {
            this.attackCooldown = 15 + this.bewilder.getRandom().nextInt(10);
        }

        private void hurtNearbyEntities() {
            AABB attackBox = this.bewilder.getBoundingBox().move(this.bewilder.getLookAngle().normalize().scale(0.6D)).inflate(0.4D, 0.0D, 0.4D);
            List<LivingEntity> nearbyEntities = this.bewilder.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.bewilder, attackBox);
            if (!nearbyEntities.isEmpty()) {
                nearbyEntities.stream().filter(entity -> entity != this.bewilder).limit(4).forEach(entity -> {
                    this.bewilder.doHurtTarget(entity);
                    float yawRad = this.bewilder.getYRot() * Mth.DEG_TO_RAD;
                    entity.knockback(1.5F, Mth.sin(yawRad), (-Mth.cos(yawRad)));
                    if (entity.isDamageSourceBlocked(this.bewilder.damageSources().mobAttack(this.bewilder)) && entity instanceof Player player) {
                        player.disableShield();
                        player.knockback(1.0F, Mth.sin(yawRad), (-Mth.cos(yawRad)));
                        player.hurtMarked = true;
                        this.timer = 0;
                        this.bewilder.setSprinting(false);
                        this.attackState = 4;
                    }
                });
            }
        }
    }
}
