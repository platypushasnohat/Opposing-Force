package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.BrickAttackGoal;
import com.barl_inc.opposing_force.entity.ai.goal.JukeboxDanceGoal;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.barl_inc.opposing_force.tags.OFEntityTypeTags;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

public class Brick extends AnimatedMonster {

    public static final int DEFEND_START_ANIMATION = 1;
    public static final int DEFEND_ANIMATION = 2;
    public static final int DANCE_ANIMATION = 3;
    public static final int MELT_ANIMATION = 4;

    private static final byte SQUISH = 80;

    private int squishTicks;
    private int sporeTimer;
    private boolean critHit;

    private JukeboxDanceGoal<Brick> danceGoal;

    public final SmoothAnimationState sprintAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState danceAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState defendStartAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState defendAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState meltAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState squishAnimationState = new SmoothAnimationState(1.0F);

    public Brick(EntityType<? extends Brick> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, this.danceGoal = new JukeboxDanceGoal<>(this, DANCE_ANIMATION));
        this.goalSelector.addGoal(2, new BrickAttackGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static boolean checkBrickSpawnRules(EntityType<Brick> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() <= 40 && checkUndergroundMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    public boolean isDancing() {
        return this.danceGoal != null && this.danceGoal.isDancing();
    }

    public boolean isMelting() {
        return this.getAnimationState() == MELT_ANIMATION;
    }

    public void setCritHit(boolean critHit) {
        this.critHit = critHit;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !this.isDancing() && super.canAttack(target);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt;
        if (source.is(OFDamageTypes.ACID) && !this.isDeadOrDying()) {
            this.setAnimationState(MELT_ANIMATION);
            hurt = super.hurt(source, Float.MAX_VALUE);
            if (!hurt) {
                this.setAnimationState(0);
            }
        }
        else {
            hurt = super.hurt(source, amount);
            if (hurt && this.isAlive()) {
                this.level().broadcastEntityEvent(this, SQUISH);
                if (this.critHit) {
                    this.sporeTimer = 6;
                }
            }
        }
        this.critHit = false;
        return hurt;
    }

    public boolean canBounce(Projectile projectile) {
        return this.isAlive() && !projectile.getType().is(OFEntityTypeTags.PROJECTILE_BYPASSES_BRICK);
    }

    public void bounce(Projectile projectile) {
        projectile.deflect(ProjectileDeflection.REVERSE, this, null, false);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == SQUISH) {
            this.squishAnimationState.start(this.tickCount);
            this.squishTicks = 10;
        }
        else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.squishTicks > 0) {
                this.squishTicks--;
            }
        }
        else if (this.sporeTimer > 0) {
            this.sporeTimer--;
            if (this.sporeTimer == 0 && this.isAlive()) {
                this.releaseSpores();
            }
        }
    }

    private void releaseSpores() {
        if (this.level() instanceof ServerLevel level) {
            AABB area = this.getBoundingBox().inflate(3.0D, 1.0D, 3.0D);
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, living -> living.isAlive() && !(living instanceof Enemy))) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0), this);
            }
            RandomSource random = this.getRandom();
            int clumps = 12;
            for (int i = 0; i < clumps; i++) {
                double angle = (Math.PI * 2.0D) * (i + random.nextDouble() * 0.5D) / clumps;
                double dirX = Math.cos(angle);
                double dirZ = Math.sin(angle);
                double clumpX = this.getX() + dirX * 1.2D;
                double clumpY = this.getY() + 0.2D + random.nextDouble() * 0.8D;
                double clumpZ = this.getZ() + dirZ * 1.2D;
                for (int j = 0; j < 12; j++) {
                    double x = clumpX + (random.nextDouble() - 0.5D) * 0.8D;
                    double y = clumpY + (random.nextDouble() - 0.5D) * 0.6D;
                    double z = clumpZ + (random.nextDouble() - 0.5D) * 0.8D;
                    double speed = 0.08D + random.nextDouble() * 0.08D;
                    level.sendParticles(OFParticleTypes.BRICK_SPORE_CLOUD.get(), x, y, z, 0, dirX * speed, 0.02D, dirZ * speed, 0.2D);
                }
            }
        }
    }

    @Override
    protected void tickDeath() {
        if (this.isMelting()) {
            this.deathTime++;
            if (this.deathTime >= 40 && !this.level().isClientSide() && !this.isRemoved()) {
                this.level().broadcastEntityEvent(this, (byte) 60);
                this.remove(RemovalReason.KILLED);
            }
        }
        else {
            super.tickDeath();
        }
    }

    @Override
    public void setupAnimationStates() {
        int state = this.getAnimationState();
        boolean grounded = !this.isInWaterOrBubble();
        this.idleAnimationState.animateWhen(grounded && state == 0, this.tickCount);
        this.walkAnimationState.animateWhen(grounded && state == 0 && !this.isAggressive(), this.tickCount);
        this.sprintAnimationState.animateWhen(grounded && state == 0 && this.isAggressive(), this.tickCount);
        this.danceAnimationState.animateWhen(grounded && this.onGround() && state == DANCE_ANIMATION, this.tickCount);
        this.defendStartAnimationState.animateWhen(state == DEFEND_START_ANIMATION, this.tickCount);
        this.defendAnimationState.animateWhen(state == DEFEND_ANIMATION, this.tickCount);
        this.meltAnimationState.animateWhen(state == MELT_ANIMATION, this.tickCount);
        this.squishAnimationState.animateWhen(this.squishTicks > 0, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 10.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return OFSoundEvents.MUSHY_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return OFSoundEvents.MUSHY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return OFSoundEvents.MUSHY_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }
}
