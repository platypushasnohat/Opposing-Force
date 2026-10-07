package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.JukeboxDanceGoal;
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

    private static final int ATTACK_ANIMATION = 1;
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
        // Coast to a stop on the next full turn in the spin direction
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

    private static class MushyAttackGoal extends AttackGoal {
        private final Mushy mushy;
        private int attackCooldown;
        private boolean swinging;
        //attack state value:
        //0 deciding
        //1 melee
        //2 launch windup
        //3 ascending
        //4 launch hang
        //5 descending
        //6 recovery

        public MushyAttackGoal(Mushy mushy) {
            super(mushy);
            this.mushy = mushy;
        }

        private boolean isLaunching() {
            return this.attackState >= 2;
        }

        @Override
        public void start() {
            super.start();
            this.attackCooldown = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.mushy.setLaunchTilt(0.0F);
        }

        @Override
        public boolean isInterruptable() {
            return !this.isLaunching();
        }

        @Override
        public boolean canContinueToUse() {
            return this.isLaunching() || super.canContinueToUse();
        }

        @Override
        public void tick() {
            LivingEntity target = this.mushy.getTarget();
            switch (this.attackState) {
                case 1 -> this.tickMelee(target);
                case 2 -> this.tickWindup(target);
                case 3 -> this.tickAscend(target);
                case 4 -> this.tickHang(target);
                case 5 -> this.tickDive();
                case 6 -> this.tickRecover();
                default -> this.tickDecide(target);
            }
        }

        private void tickDecide(LivingEntity target) {
            if (target != null) {
                this.lookAtTarget(target, 30.0F, 20.0F);
                if (this.attackCooldown > 0) {
                    this.attackCooldown--;
                    return;
                }
                if (this.mushy.getRandom().nextFloat() < 0.33F && this.canLaunchAt(target)) {
                    this.setState(2);
                }
                else {
                    this.swinging = false;
                    this.setState(1);
                }
            }
        }

        private boolean canLaunchAt(LivingEntity target) {
            return this.mushy.onGround()
                    && !this.mushy.isInWaterOrBubble()
                    && this.mushy.distanceToSqr(target) <= 11.0D * 11.0D
                    && this.isWithinYRange(target, 4)
                    && this.mushy.hasLineOfSight(target)
                    && this.mushy.level().noCollision(this.mushy, this.mushy.getBoundingBox().expandTowards(0.0D, 5.0D, 0.0D));
        }

        private void tickMelee(LivingEntity target) {
            if (target != null) {
                this.lookAtTarget(target, 30.0F, 20.0F);
                this.mushy.getNavigation().moveTo(target, 1.1D);
                if (!this.swinging) {
                    if (this.mushy.distanceToSqr(target) <= this.getAttackReachSqr(target, 1.55D)) {
                        this.swinging = true;
                        this.timer = 0;
                    }
                    return;
                }
                this.timer++;
                if (this.timer == 1) {
                    this.mushy.setAnimationState(ATTACK_ANIMATION);
                    this.mushy.playSound(OFSoundEvents.FURBALL_ATTACK.get(), 1.0F, SinewSoundUtils.randomizePitch(this.mushy));
                }
                if (this.timer == 11 && this.isInAttackRange(target, 0.65D)) {
                    this.mushy.doHurtTarget(target);
                }
                if (this.timer > 15) {
                    this.mushy.setAnimationState(0);
                    this.swinging = false;
                    this.attackCooldown = 5 + this.mushy.getRandom().nextInt(3);
                    this.setState(0);
                }
            }
        }

        private void tickWindup(LivingEntity target) {
            this.timer++;
            this.mushy.getNavigation().stop();
            if (target != null) {
                this.lookAtTarget(target, 30.0F, 20.0F);
            }
            if (this.timer == 1) {
                this.mushy.setAnimationState(JUMP_ANIMATION);
            }
            if (this.timer >= 15) {
                Vec3 motion = this.mushy.getDeltaMovement();
                this.mushy.setDeltaMovement(motion.x, 1.3D, motion.z);
                this.mushy.hasImpulse = true;
                this.mushy.setAnimationState(SPIN_ANIMATION);
                this.setState(3);
            }
        }

        private void tickAscend(LivingEntity target) {
            this.timer++;
            this.mushy.getNavigation().stop();
            this.steerOver(target);
            if (this.timer > 3 && this.mushy.getDeltaMovement().y < 0.35D) {
                if (target != null) {
                    this.mushy.setLaunchTiltYaw(yawToward(this.mushy.position(), target.position()));
                }
                else {
                    this.mushy.setLaunchTiltYaw(this.mushy.getYRot());
                }
                this.setState(4);
            }
            this.checkAirTime();
        }

        private void tickHang(LivingEntity target) {
            this.timer++;
            this.mushy.getNavigation().stop();
            float progress = Mth.clamp((float) this.timer / 16, 0.0F, 1.0F);
            float tilt = 180.0F * (progress * progress * (3.0F - (progress * 2.0F)));
            this.mushy.setLaunchTilt(tilt);
            if (tilt < 120.0F) {
                this.steerOver(target);
            }
            Vec3 motion = this.mushy.getDeltaMovement();
            this.mushy.setDeltaMovement(motion.x, motion.y + 0.05D, motion.z);
            if (this.timer >= 16) {
                this.setState(5);
            }
        }

        //aim over player for fall
        private void steerOver(LivingEntity target) {
            if (target != null) {
                this.lookAtTarget(target, 30.0F, 30.0F);
                Vec3 motion = this.mushy.getDeltaMovement();
                Vec3 offset = new Vec3(target.getX() - this.mushy.getX(), 0.0D, target.getZ() - this.mushy.getZ());
                double distance = offset.length();
                Vec3 wanted = distance > 1.0E-4D ? offset.scale(Math.min(distance * 0.25D, 0.45D) / distance) : Vec3.ZERO; //skip tiny angle to avoid snapping
                this.mushy.setDeltaMovement(Mth.lerp(0.3D, motion.x, wanted.x), motion.y, Mth.lerp(0.3D, motion.z, wanted.z));
            }
        }

        private void tickDive() {
            this.timer++;
            this.mushy.getNavigation().stop();
            Vec3 motion = this.mushy.getDeltaMovement();
            double y = Math.max(motion.y - 0.15D, -2.2D);
            this.mushy.setDeltaMovement(motion.x * 0.5D, y, motion.z * 0.5D);
            if (this.mushy.onGround() || this.isTouchingEntity()) {
                this.impact(this.getEntitiesInImpactRange());
            }
            else if (this.mushy.isInWaterOrBubble()) {
                this.endLaunch();
            }
            this.checkAirTime();
        }

        private boolean isTouchingEntity() {
            return !this.mushy.level().getEntitiesOfClass(LivingEntity.class,
                    this.mushy.getBoundingBox().inflate(0.1D),
                    this::canImpactHit).isEmpty();
        }

        private List<LivingEntity> getEntitiesInImpactRange() {
            return this.mushy.level().getEntitiesOfClass(LivingEntity.class,
                    this.mushy.getBoundingBox().inflate(1.5D, 0.5D, 1.5D),
                    this::canImpactHit);
        }

        private boolean canImpactHit(LivingEntity entity) {
            return entity != this.mushy && entity.isAlive() && this.mushy.canAttack(entity);
        }

        private void impact(List<LivingEntity> hit) {
            float damage = (float) this.mushy.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0F;
            for (LivingEntity entity : hit) {
                if (entity.hurt(this.mushy.damageSources().mobAttack(this.mushy), damage)) {
                    entity.knockback(2.0D, this.mushy.getX() - entity.getX(), this.mushy.getZ() - entity.getZ());
                }
            }
            this.mushy.setDeltaMovement(0.0D, 0.0D, 0.0D);
            this.mushy.fallDistance = 0.0F;
            this.mushy.setAnimationState(0);
            this.setState(6);
        }

        private void spawnSporeBurst() {
            if ((this.mushy.level() instanceof ServerLevel level)) {
                RandomSource random = this.mushy.getRandom();
                int clumps = 8;
                for (int i = 0; i < clumps; i++) {
                    double angle = (Math.PI * 2.0D) * (i + random.nextDouble() * 0.5D) / clumps;
                    double dirX = Math.cos(angle);
                    double dirZ = Math.sin(angle);
                    double clumpX = this.mushy.getX() + dirX * 0.6D;
                    double clumpY = this.mushy.getY() + 0.1D + random.nextDouble();
                    double clumpZ = this.mushy.getZ() + dirZ * 0.6D;
                    for (int j = 0; j < 8; j++) {
                        double x = clumpX + (random.nextDouble() - 0.5D) * 0.5D;
                        double y = clumpY + (random.nextDouble() - 0.5D) * 0.6D;
                        double z = clumpZ + (random.nextDouble() - 0.5D) * 0.5D;
                        double speed = 0.06D + random.nextDouble() * 0.06D;
                        level.sendParticles(OFParticleTypes.SPORE_CLOUD.get(), x, y, z, 0, dirX * speed, 0.02D, dirZ * speed, 1.0D);
                    }
                }
            }
        }

        private void tickRecover() {
            this.timer++;
            this.mushy.getNavigation().stop();
            int bounceDelay = 3;
            if (this.timer == bounceDelay) { //small bounce upwards after delay so it lands on ground visually
                this.spawnSporeBurst();
                this.mushy.setDeltaMovement(0.0D, 0.65D, 0.0D);
                this.mushy.hasImpulse = true;
            }
            if (this.timer > bounceDelay + 1 && this.mushy.onGround() && this.mushy.getAnimationState() == SPIN_ANIMATION) {
                this.mushy.setAnimationState(0);
            }
            //flip upright
            float revert = Mth.clamp(1.0F - (float) (this.timer - bounceDelay) / 8.0F, 0.0F, 1.0F);
            this.mushy.setLaunchTilt(180.0F * revert);
            if (this.timer >= 25 + bounceDelay) {
                this.endLaunch();
            }
        }

        private void endLaunch() {
            this.mushy.setLaunchTilt(0.0F);
            if (this.mushy.getAnimationState() == SPIN_ANIMATION || this.mushy.getAnimationState() == JUMP_ANIMATION) {
                this.mushy.setAnimationState(0);
            }
            this.attackCooldown = 10;
            this.setState(0);
        }

        //launch timeout
        private void checkAirTime() {
            if (this.timer > 300) {
                this.endLaunch();
            }
        }

        private void setState(int state) {
            this.attackState = state;
            this.timer = 0;
        }

        private static float yawToward(Vec3 from, Vec3 to) {
            return (float) (Mth.atan2(to.z - from.z, to.x - from.x) * Mth.RAD_TO_DEG) - 90.0F;
        }
    }
}
