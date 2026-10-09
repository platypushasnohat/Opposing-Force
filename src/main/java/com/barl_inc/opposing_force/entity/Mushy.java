package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.JukeboxDanceGoal;
import com.barl_inc.opposing_force.entity.ai.goal.MushyAttackGoal;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Mushy extends AnimatedMonster {
    private static final EntityDataAccessor<Float> LAUNCH_TILT = SynchedEntityData.defineId(Mushy.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> LAUNCH_TILT_YAW = SynchedEntityData.defineId(Mushy.class, EntityDataSerializers.FLOAT);
    private float tilt;
    private float tilt_old;
    private float angle;
    private float angle_old;

    public static final int ATTACK_ANIMATION = 1;
    public static final int JUMP_ANIMATION = 2;
    public static final int SPIN_ANIMATION = 3;
    public static final int DANCE_ANIMATION = 4;

    private JukeboxDanceGoal<Mushy> danceGoal;

    public final SmoothAnimationState danceAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState launchAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState spinAnimationState = new SmoothAnimationState(0.15F);
    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);

    public Mushy(EntityType<? extends Mushy> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, this.danceGoal = new JukeboxDanceGoal<>(this, DANCE_ANIMATION));
        this.goalSelector.addGoal(2, new MushyAttackGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LAUNCH_TILT, 0.0F);
        builder.define(LAUNCH_TILT_YAW, 0.0F);
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
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.tilt_old = this.tilt;
            this.tilt = this.getLaunchTilt();
            this.tickSpin();
        }
    }

    private void tickSpin() {
        this.angle_old = this.angle;
        if (this.getAnimationState() == SPIN_ANIMATION) {
            this.angle -= 36.0F;
            return;
        }
        float stop = Mth.floor(this.angle / 360.0F) * 360.0F;
        float remaining = this.angle - stop;
        if (remaining > 0.01F) {
            this.angle = Math.max(this.angle - Math.min(36.0F, Math.max(remaining * 0.15F, 0.5F)), stop);
        }
        else {
            this.angle = 0.0F;
            this.angle_old = 0.0F;
        }
    }

    public float getSpinAngle(float partialTicks) {
        return Mth.lerp(partialTicks, this.angle_old, this.angle);
    }

    // 0-180 degrees
    public float getLaunchTilt() {
        return this.entityData.get(LAUNCH_TILT);
    }

    public void setLaunchTilt(float tilt) {
        this.entityData.set(LAUNCH_TILT, tilt);
    }

    public float getLaunchTiltYaw() {
        return this.entityData.get(LAUNCH_TILT_YAW);
    }

    public void setLaunchTiltYaw(float yaw) {
        this.entityData.set(LAUNCH_TILT_YAW, yaw);
    }

    public float getTilt(float partialTicks) {
        return Mth.lerp(partialTicks, this.tilt_old, this.tilt);
    }

    public boolean isDancing() {
        return this.danceGoal != null && this.danceGoal.isDancing();
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(!this.isInWaterOrBubble()
                && this.getAnimationState() != JUMP_ANIMATION
                && this.getAnimationState() != SPIN_ANIMATION
                && this.getAnimationState() != DANCE_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(!this.isInWaterOrBubble()
                && this.getAnimationState() != JUMP_ANIMATION
                && this.getAnimationState() != DANCE_ANIMATION
                && this.getAnimationState() != SPIN_ANIMATION, this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.danceAnimationState.animateWhen(!this.isInWaterOrBubble() && this.onGround() && this.getAnimationState() == DANCE_ANIMATION, this.tickCount);
        this.launchAnimationState.animateWhen(!this.isInWaterOrBubble() && this.onGround() && this.getAnimationState() == JUMP_ANIMATION, this.tickCount);
        this.spinAnimationState.animateWhen(!this.isInWaterOrBubble() && this.getAnimationState() == SPIN_ANIMATION, this.tickCount);
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

    public static boolean checkMushySpawnRules(EntityType<Mushy> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && checkMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }


}
