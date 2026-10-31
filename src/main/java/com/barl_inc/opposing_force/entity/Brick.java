package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.ai.goal.BrickAttackGoal;
import com.barl_inc.opposing_force.entity.ai.goal.BrickSummonDefendGoal;
import com.barl_inc.opposing_force.entity.ai.goal.JukeboxDanceGoal;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.barl_inc.opposing_force.registry.datapack.tags.OFEntityTypeTags;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.UnrestrictedBodyRotationControl;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.base.SummonableMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Brick extends SummonableMonster {

    public static final int DEFEND_ANIMATION = 1;
    public static final int DANCE_ANIMATION = 2;
    public static final int MELT_ANIMATION = 3;

    private static final EntityDimensions DEFENDING_DIMENSIONS = EntityDimensions.scalable(2.3F, 1.9F);
    private static final ResourceLocation DEFENDING_ARMOR_MODIFIER_ID = OpposingForce.location("defending");
    private static final AttributeModifier DEFENDING_ARMOR_MODIFIER = new AttributeModifier(DEFENDING_ARMOR_MODIFIER_ID, 8.0D, AttributeModifier.Operation.ADD_VALUE);

    private JukeboxDanceGoal<Brick> danceGoal;

    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState danceAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState defendAnimationState = new SmoothAnimationState(0.2F);
    public final SmoothAnimationState meltAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState hurtAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState summonAnimationState = new SmoothAnimationState(1.0F);

    public Brick(EntityType<? extends Brick> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.17F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BrickPanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, this.danceGoal = new JukeboxDanceGoal<>(this, DANCE_ANIMATION));
        this.goalSelector.addGoal(3, new BrickSummonDefendGoal(this));
        this.goalSelector.addGoal(3, new BrickAttackGoal(this));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.2D, 6.0F, 2.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, target -> !this.isTame()));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static boolean checkBrickSpawnRules(EntityType<Brick> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return (MobSpawnType.isSpawner(spawnType) || pos.getY() <= 40) && AnimatedMonster.checkUndergroundMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    protected void onSummoned(Player player) {
        super.onSummoned(player);
        AttributeInstance attribute = this.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(50.0D);
            this.setHealth(50.0F);
        }
    }

    @Override
    public float getSummonCost() {
        return 3.0F;
    }

    public boolean isDancing() {
        return this.danceGoal != null && this.danceGoal.isDancing();
    }

    public boolean isMelting() {
        return this.getAnimationState() == MELT_ANIMATION;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !this.isDancing() && super.canAttack(target);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.getAnimationState() == DEFEND_ANIMATION;
    }

    @Override
    public boolean isPushable() {
        return this.getAnimationState() != DEFEND_ANIMATION;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new UnrestrictedBodyRotationControl(this);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.getAnimationState() == DEFEND_ANIMATION) {
            return DEFENDING_DIMENSIONS.scale(this.getAgeScale());
        }
        return super.getDefaultDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ANIMATION_STATE.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            AttributeInstance attribute = this.getAttribute(Attributes.ARMOR);
            if (attribute != null) {
                attribute.removeModifier(DEFENDING_ARMOR_MODIFIER_ID);
                if (this.getAnimationState() == DEFEND_ANIMATION) {
                    attribute.addTransientModifier(DEFENDING_ARMOR_MODIFIER);
                }
            }
        }
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
        }
        return hurt;
    }

    public boolean canBounce(Projectile projectile) {
        return this.isAlive() && !projectile.getType().is(OFEntityTypeTags.PROJECTILE_BYPASSES_BRICK);
    }

    public void bounce(Projectile projectile) {
        Vec3 normalize = this.getDeltaMovement().normalize();
        projectile.setDeltaMovement(normalize.scale(0.4D));
        projectile.hasImpulse = true;
        projectile.deflect(ProjectileDeflection.NONE, this, null, false);
    }

    public void spawnSporeParticles(ServerLevel level, int clumps) {
        RandomSource random = this.getRandom();
        for (int i = 0; i < clumps; i++) {
            double angle = (Math.PI * 2.0D) * (i + random.nextDouble() * 0.5D) / clumps;
            double dirX = Math.cos(angle);
            double dirZ = Math.sin(angle);
            double clumpX = this.getX() + dirX * 1.2D;
            double clumpY = this.getY() + 0.2D + random.nextDouble() * 0.8D;
            double clumpZ = this.getZ() + dirZ * 1.2D;
            for (int j = 0; j < 8; j++) {
                double x = clumpX + (random.nextDouble() - 0.5D) * 0.8D;
                double y = clumpY + (random.nextDouble() - 0.5D) * 0.6D;
                double z = clumpZ + (random.nextDouble() - 0.5D) * 0.8D;
                double speed = 0.08D + random.nextDouble() * 0.08D;
                level.sendParticles(OFParticleTypes.BRICK_SPORE_CLOUD.get(), x, y, z, 0, dirX * speed, 0.02D, dirZ * speed, 0.5D);
            }
        }
    }

    @Override
    protected void tickDeath() {
        if (this.isMelting()) {
            this.deathTime++;
            if (this.deathTime >= 40 && !this.level().isClientSide && !this.isRemoved()) {
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
        boolean outsideWater = !this.isInWaterOrBubble();
        this.idleAnimationState.animateWhen(outsideWater && state == 0, this.tickCount);
        this.walkAnimationState.animateWhen(outsideWater && state == 0 && !this.isSprinting(), this.tickCount);
        this.runAnimationState.animateWhen(outsideWater && state == 0 && this.isSprinting(), this.tickCount);
        this.danceAnimationState.animateWhen(outsideWater && this.onGround() && state == DANCE_ANIMATION, this.tickCount);
        this.defendAnimationState.animateWhen(state == DEFEND_ANIMATION, this.tickCount);
        this.meltAnimationState.animateWhen(state == MELT_ANIMATION, this.tickCount);
        this.hurtAnimationState.animateWhen(this.hurtTime > 0 && state != MELT_ANIMATION, this.tickCount);
        this.summonAnimationState.animateWhen(this.isSummoned() && this.tickCount < 30, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 17.5F, 1.0F);
        this.walkAnimation.update(speed, 0.2F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return OFSoundEvents.BRICK_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return OFSoundEvents.BRICK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return OFSoundEvents.BRICK_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.7F;
    }

    public static class BrickPanicGoal extends PanicGoal {

        private final Brick brick;

        public BrickPanicGoal(Brick brick, double speedModifier) {
            super(brick, speedModifier);
            this.brick = brick;
        }

        private List<LivingEntity> getNearbyMobs() {
            return this.brick.level().getEntitiesOfClass(LivingEntity.class, this.brick.getBoundingBox().inflate(6.0D), entity -> entity != this.brick && !this.brick.isAlliedTo(entity));
        }

        @Override
        protected boolean shouldPanic() {
            return this.brick.getAnimationState() != DEFEND_ANIMATION && super.shouldPanic();
        }

        @Override
        protected boolean findRandomPosition() {
            Vec3 vec3 = DefaultRandomPos.getPos(this.brick, 7, 4);
            if (this.brick.getLastHurtByMob() != null) {
                vec3 = LandRandomPos.getPosAway(this.brick, 7, 4, this.brick.getLastHurtByMob().position());
            }
            if (vec3 == null) {
                return false;
            } else {
                this.posX = vec3.x;
                this.posY = vec3.y;
                this.posZ = vec3.z;
                return true;
            }
        }

        @Override
        public void start() {
            super.start();
            for (LivingEntity entity : this.getNearbyMobs()) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1), this.brick);
            }
            if (!this.brick.level().isClientSide && this.brick.level() instanceof ServerLevel serverLevel) {
                this.brick.spawnSporeParticles(serverLevel, 8);
            }
            this.brick.setSprinting(true);
        }

        @Override
        public void stop() {
            super.stop();
            this.brick.setSprinting(false);
        }
    }
}
