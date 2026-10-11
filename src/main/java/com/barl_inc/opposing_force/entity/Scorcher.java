package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.UnrestrictedBodyRotationControl;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Scorcher extends AnimatedMonster {

    private static final EntityDataAccessor<Boolean> PREPARING_FIRE = SynchedEntityData.defineId(Scorcher.class, EntityDataSerializers.BOOLEAN);

    public static final int ATTACK_ANIMATION = 1;
    public static final int FIRE_ANIMATION = 2;

    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState fireAnimationState = new SmoothAnimationState();

    public float prevFireProgress;
    public float fireProgress;

    public Scorcher(EntityType<? extends Scorcher> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ScorcherAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PREPARING_FIRE, false);
    }

    public boolean isPreparingFire() {
        return this.getEntityData().get(PREPARING_FIRE);
    }

    public void setPreparingFire(boolean flag) {
        this.getEntityData().set(PREPARING_FIRE, flag);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new UnrestrictedBodyRotationControl(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevFireProgress = this.fireProgress;

        if (this.isPreparingFire() && this.fireProgress < 10.0F) {
            this.fireProgress++;
        }
        if (!this.isPreparingFire() && this.fireProgress > 0.0F) {
            this.fireProgress--;
        }

        if (this.getAnimationState() == FIRE_ANIMATION) {
            Vec3 look = this.getLookAngle();
            double dist = 0.5D;
            double px = this.getX() + look.x() * dist;
            double py = this.getY() + 0.6D + look.y() * dist;
            double pz = this.getZ() + look.z() * dist;
            for (int i = 0; i < 4; i++) {
                double dx = look.x();
                double dy = look.y();
                double dz = look.z();
                double spread = 5.0D + this.getRandom().nextDouble() * 2.5D;
                double velocity = 0.75D;
                dx += this.getRandom().nextGaussian() * 0.01D * spread;
                dy += this.getRandom().nextGaussian() * 0.01D * spread;
                dz += this.getRandom().nextGaussian() * 0.01D * spread;
                dx *= velocity;
                dy *= velocity;
                dz *= velocity;
                this.level().addParticle(OFParticleTypes.FIRE_BREATH.get(), px, py, pz, dx, dy, dz);
            }
            this.playSound(SoundEvents.BLAZE_SHOOT, this.getRandom().nextFloat() * 0.5F, SinewSoundUtils.randomizePitch(this));
        }
    }

    public float getFireProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevFireProgress, this.fireProgress) * 0.1F;
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() != FIRE_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() != FIRE_ANIMATION, this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.fireAnimationState.animateWhen(this.getAnimationState() == FIRE_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 8.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
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
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    private static class ScorcherAttackGoal extends AttackGoal {

        private final Scorcher scorcher;
        private int fireCooldown;

        public ScorcherAttackGoal(Scorcher scorcher) {
            super(scorcher);
            this.scorcher = scorcher;
        }

        @Override
        public void start() {
            super.start();
            this.scorcher.setPreparingFire(false);
            this.fireCooldown = 20;
        }

        @Override
        public void stop() {
            super.stop();
            this.scorcher.setPreparingFire(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.scorcher.getTarget();
            if (target != null) {
                double distance = this.scorcher.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.scorcher.getNavigation().stop();
                    this.lookAtTarget(target, 30.0F, 30.0F);
                    this.tickAttack(target);
                }
                else if (this.attackState == 2) {
                    this.scorcher.getNavigation().stop();
                    this.tickFire(target);
                }
                else {
                    this.lookAtTarget(target, 30.0F, 30.0F);
                    this.scorcher.getNavigation().moveTo(target, 1.2D);
                    if (this.fireCooldown > 0) {
                        this.fireCooldown--;
                    }
                    if (distance <= this.getAttackReachSqr(target, 1.75D)) {
                        this.attackState = 1;
                    }
                    if (distance <= 42 && this.fireCooldown <= 0 && this.isWithinYRange(target, 1)) {
                        this.attackState = 2;
                    }
                }
            }
        }

        private void tickAttack(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.scorcher.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 5 && this.isInAttackRange(target, 0.8D)) {
                if (this.scorcher.doHurtTarget(target) && this.scorcher.getRandom().nextFloat() <= 0.4F) {
                    target.igniteForSeconds(3);
                }
            }
            if (this.timer > 20) {
                this.scorcher.setAnimationState(0);
                this.timer = 0;
                this.attackState = 0;
            }
        }

        private void tickFire(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.scorcher.setPreparingFire(true);
            }
            if (this.timer == 20) {
                this.scorcher.setAnimationState(FIRE_ANIMATION);
            }
            if (this.timer < 20) {
                this.lookAtTarget(target, 15.0F, 15.0F);
            }
            if (this.timer > 20 && this.timer < 100) {
                this.lookAtTarget(target, 0.9F, 0.0F);
                this.burnEntities();
            }
            if (this.timer > 100) {
                this.scorcher.setAnimationState(0);
                this.scorcher.setPreparingFire(false);
                this.timer = 0;
                this.attackState = 0;
                this.fireCooldown = 70 + this.scorcher.getRandom().nextInt(40);
            }
        }

        private void burnEntities() {
            float distanceBurned = 0.0F;
            float burnWidth = 0.2F;
            Vec3 headPos = this.scorcher.position().add(this.scorcher.getLookAngle());
            while (distanceBurned < 8) {
                burnWidth += 0.04F;
                Vec3 burnPos = headPos.add(this.rotateOffsetVec(new Vec3(0, 0, distanceBurned), 0, this.scorcher.getYRot()));
                this.hurtEntitiesAround(burnPos, burnWidth, 5.0F);
                distanceBurned += burnWidth;
            }
        }

        public void hurtEntitiesAround(Vec3 center, float radius, float damageAmount) {
            AABB aabb = new AABB(center.subtract(radius, 0.0F, radius), center.add(radius, 0.95F, radius));
            DamageSource damageSource = OFDamageTypes.causeScorchDamage(this.scorcher.level().registryAccess(), this.scorcher);
            for (LivingEntity living : this.scorcher.level().getEntitiesOfClass(LivingEntity.class, aabb, EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
                if (!living.is(this.scorcher) && !living.fireImmune() && living.distanceToSqr(center.x, center.y, center.z) <= radius * radius) {
                    if (living.hurt(damageSource, damageAmount)) {
                        living.igniteForSeconds(6);
                        living.knockback(0.07F, center.x - living.getX(), center.z - living.getZ());
                    }
                }
            }
        }
    }
}
