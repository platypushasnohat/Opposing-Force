package com.barl_inc.opposing_force.entity;

import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Grasper extends AnimatedMonster {

    public static final int LEAP_START_ANIMATION = 1;
    public static final int LEAP_LOOP_ANIMATION = 2;
    public static final int PUNCH_ANIMATION = 3;
    public static final int SLAP_ANIMATION = 4;
    public static final int FLINGED_ANIMATION = 5;
    public static final int STUN_ANIMATION = 6;
    public static final int FLIP_BACK_ANIMATION = 7;

    public final SmoothAnimationState leapStartAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState leapLoopAnimationState = new SmoothAnimationState(0.15F);
    public final SmoothAnimationState punchAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slapAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flingedAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState stunAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flipBackAnimationState = new SmoothAnimationState();

    private int stunTicks;

    public Grasper(EntityType<? extends Grasper> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GrasperAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick()) {
            this.igniteForSeconds(8.0F);
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isStunned()) {
            this.stunTicks++;
            this.getNavigation().stop();
            if (this.getAnimationState() == FLINGED_ANIMATION && this.stunTicks > 2 && this.onGround()) {
                this.setStun(STUN_ANIMATION);
            } else if (this.getAnimationState() == STUN_ANIMATION && this.stunTicks >= 40) {
                this.setStun(FLIP_BACK_ANIMATION);
            } else if (this.getAnimationState() == FLIP_BACK_ANIMATION && this.stunTicks >= 30) {
                this.setAnimationState(0);
                this.setStunFlags(false);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean attacking = this.getAnimationState() == PUNCH_ANIMATION || this.getAnimationState() == SLAP_ANIMATION;
        boolean hurt = super.hurt(source, amount);
        Entity attacker = source.getEntity();
        if (hurt && attacking && attacker != null) {
            Vec3 away = this.position().subtract(attacker.position()).multiply(1.0D, 0.0D, 1.0D).normalize();
            this.setDeltaMovement(away.x * 0.8D, 0.5D, away.z * 0.8D);
            this.hasImpulse = true;
            this.setStun(FLINGED_ANIMATION);
            this.setStunFlags(true);
        }
        return hurt;
    }

    private void setStun(int animation) {
        this.setAnimationState(animation);
        this.stunTicks = 0;
    }

    private void setStunFlags(boolean stunned) {
        for (Goal.Flag flag : Goal.Flag.values()) {
            if (stunned) {
                this.goalSelector.disableControlFlag(flag);
            } else {
                this.goalSelector.enableControlFlag(flag);
            }
        }
    }

    public boolean isStunned() {
        return this.getAnimationState() >= FLINGED_ANIMATION;
    }

    public boolean isAirborne() {
        return this.getAnimationState() >= LEAP_LOOP_ANIMATION && this.getAnimationState() <= FLINGED_ANIMATION;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isAirborne() ? EntityDimensions.scalable(0.9F, 2.0F) : super.getDefaultDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (ANIMATION_STATE.equals(accessor)) {
            this.refreshDimensions();
        }
    }

    public static boolean checkGrasperSpawnRules(EntityType<Grasper> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return (!level.getBiome(pos).is(Biomes.WOODED_BADLANDS) || level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) && checkSurfaceMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    public void setupAnimationStates() {
        int state = this.getAnimationState();
        this.idleAnimationState.animateWhen(state == 0, this.tickCount);
        this.walkAnimationState.animateWhen(state == 0, this.tickCount);
        this.leapStartAnimationState.animateWhen(state == LEAP_START_ANIMATION, this.tickCount);
        if (state == FLINGED_ANIMATION) {
            this.leapLoopAnimationState.factor = 0.0F;
        }
        this.leapLoopAnimationState.animateWhen(state >= LEAP_LOOP_ANIMATION && state <= SLAP_ANIMATION, this.tickCount);
        this.punchAnimationState.animateWhen(state == PUNCH_ANIMATION, this.tickCount);
        this.slapAnimationState.animateWhen(state == SLAP_ANIMATION, this.tickCount);
        this.flingedAnimationState.animateWhen(state == FLINGED_ANIMATION, this.tickCount);
        this.stunAnimationState.animateWhen(state == STUN_ANIMATION, this.tickCount);
        this.flipBackAnimationState.animateWhen(state == FLIP_BACK_ANIMATION, this.tickCount);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 0.8F);
    }

    private static class GrasperAttackGoal extends AttackGoal {

        private final Grasper grasper;
        private int leapCooldown;
        private int attacks;
        private int maxAttacks;
        private boolean hit;
        private boolean punchNext;
        private Vec3 retreatStart = Vec3.ZERO;

        public GrasperAttackGoal(Grasper grasper) {
            super(grasper);
            this.grasper = grasper;
        }

        @Override
        public boolean canUse() {
            return !this.grasper.isStunned() && super.canUse();
        }

        @Override
        public void stop() {
            if (this.grasper.isStunned()) {
                this.grasper.getNavigation().stop();
                this.timer = 0;
                this.attackState = 0;
            } else {
                super.stop();
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.grasper.getTarget();
            if (target != null) {
                this.lookAtTarget(target, 30.0F, 30.0F);
                switch (this.attackState) {
                    case 1 -> this.tickLeapStart();
                    case 2 -> this.tickFlying(target);
                    case 3 -> this.tickAttack(target);
                    case 4 -> this.tickWindup(target);
                    default -> this.tickApproach(target);
                }
            }
        }

        private void tickApproach(LivingEntity target) {
            this.grasper.getNavigation().moveTo(target, 1.2D);
            if (this.leapCooldown > 0) {
                this.leapCooldown--;
            }
            if (this.leapCooldown <= 0 && this.grasper.onGround() && this.grasper.distanceTo(target) < 3.5D && this.grasper.hasLineOfSight(target)) {
                this.grasper.getNavigation().stop();
                this.grasper.setAnimationState(LEAP_START_ANIMATION);
                this.timer = 0;
                this.attackState = 1;
            }
        }

        private void tickLeapStart() {
            this.timer++;
            if (this.timer >= 20) {
                this.attacks = 0;
                this.maxAttacks = 2 + this.grasper.getRandom().nextInt(2);
                this.fly();
            }
        }

        private void tickFlying(LivingEntity target) {
            this.timer++;
            this.grasper.getNavigation().moveTo(target, 1.5D);
            if ((this.punchNext || this.timer >= 15) && this.grasper.distanceTo(target) <= 2.5D) {
                this.timer = 0;
                if (this.punchNext) {
                    this.grasper.getNavigation().stop();
                    this.retreatStart = this.grasper.position();
                    this.attackState = 4;
                } else {
                    this.startAttack(SLAP_ANIMATION);
                }
            } else if (this.timer > 100) {
                this.land();
            }
        }

        private void tickWindup(LivingEntity target) {
            this.timer++;
            if (this.grasper.position().distanceTo(this.retreatStart) >= 2.5D || this.timer >= 20) {
                this.timer = 0;
                this.startAttack(PUNCH_ANIMATION);
            } else {
                Vec3 back = this.grasper.position().subtract(target.position()).multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.2D);
                this.grasper.setDeltaMovement(back.x, this.grasper.getDeltaMovement().y, back.z);
            }
        }

        private void startAttack(int animation) {
            this.grasper.setAnimationState(animation);
            this.hit = false;
            this.attackState = 3;
        }

        private void tickAttack(LivingEntity target) {
            this.timer++;
            boolean punch = this.grasper.getAnimationState() == PUNCH_ANIMATION;
            this.grasper.getNavigation().moveTo(target, punch ? 2.0D : 1.5D);
            if (!this.hit && (punch ? this.timer >= 5 && this.timer <= 17 : this.timer >= 10 && this.timer <= 12) && this.isInAttackBox(target, 1.0D, 0.5D, 0.0D)) {
                this.hit = target.hurt(this.grasper.damageSources().mobAttack(this.grasper), punch ? 4.0F : 6.0F);
            }
            if (this.timer >= 20) {
                this.attacks++;
                if (this.attacks >= this.maxAttacks) {
                    this.land();
                } else {
                    this.fly();
                }
            }
        }

        private void fly() {
            this.grasper.setAnimationState(LEAP_LOOP_ANIMATION);
            this.punchNext = this.grasper.getRandom().nextBoolean();
            this.timer = 0;
            this.attackState = 2;
        }

        private void land() {
            this.grasper.setAnimationState(0);
            this.leapCooldown = 40;
            this.timer = 0;
            this.attackState = 0;
        }
    }
}