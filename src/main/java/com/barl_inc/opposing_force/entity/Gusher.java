package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.projectile.AcidCharge;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Gusher extends AnimatedMonster {

    public static final int ATTACK_ANIMATION = 1;
    public static final int GUSH_ANIMATION = 2;

    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState gushAnimationState = new SmoothAnimationState();

    public Gusher(EntityType<? extends Gusher> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 10.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GusherAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Mob.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Spider.class, 50, true, true, this::canAttack));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.is(OFDamageTypes.ACID);
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() != GUSH_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() != GUSH_ANIMATION, this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.gushAnimationState.animateWhen(this.getAnimationState() == GUSH_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 8.0F, 1.0F);
        this.walkAnimation.update(speed, 0.2F);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3, 0, 3);
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
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 0.85F);
    }

    public static boolean checkGusherSpawnRules(EntityType<Gusher> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() <= 0 && checkUndergroundMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    private static class GusherAttackGoal extends AttackGoal {

        private final Gusher gusher;
        private int biteCooldown;
        private int gushCooldown;

        public GusherAttackGoal(Gusher gusher) {
            super(gusher);
            this.gusher = gusher;
        }

        @Override
        public void start() {
            super.start();
            this.biteCooldown = 3 + this.gusher.getRandom().nextInt(3);
            this.gushCooldown = 80 + this.gusher.getRandom().nextInt(40);
        }

        @Override
        public void tick() {
            LivingEntity target = this.gusher.getTarget();
            if (target != null) {
                double distance = this.gusher.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.lookAtTarget(target, 2.0F, 90.0F);
                    this.gusher.getNavigation().stop();
                    this.tickBite(target);
                }
                else if (this.attackState == 2) {
                    this.lookAtTarget(target, 20.0F, 90.0F);
                    this.gusher.getNavigation().stop();
                    this.tickGush();
                }
                else {
                    this.lookAtTarget(target, 20.0F, 90.0F);
                    if (this.biteCooldown > 0) {
                        this.biteCooldown--;
                    }
                    if (this.gushCooldown > 0) {
                        this.gushCooldown--;
                    }
                    if (this.gusher.tickCount % 3 == 0) {
                        this.gusher.getNavigation().moveTo(target, 1.4D);
                    }
                    if (distance <= this.getAttackReachSqr(target, 2.5D) && this.biteCooldown <= 0) {
                        this.attackState = 1;
                    }
                    if (distance <= 64 && distance > this.getAttackReachSqr(target, 1.0D) && this.getAirAbove() >= 7 && this.gushCooldown <= 0) {
                        this.attackState = 2;
                    }
                }
            }
        }

        private void tickBite(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.gusher.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 14 && (this.isInAttackBox(target, 4.5D, 0.2D, -0.3D, true) || this.isInAttackRange(target, 0.7D))) {
                this.gusher.doHurtTarget(target);
            }
            if (this.timer > 40) {
                this.gusher.setAnimationState(0);
                this.timer = 0;
                this.biteCooldown = 3 + this.gusher.getRandom().nextInt(3);
                this.attackState = 0;
            }
        }

        private void tickGush() {
            this.timer++;
            if (this.timer == 1) {
                this.gusher.setAnimationState(GUSH_ANIMATION);
            }
            if (this.timer >= 20 && this.timer <= 40 && this.timer % 4 == 0) {
                this.shootAcidCharge();
            }
            if (this.timer > 80) {
                this.gusher.setAnimationState(0);
                this.timer = 0;
                this.gushCooldown = 60 + this.gusher.getRandom().nextInt(30);
                this.attackState = 0;
            }
        }

        private void shootAcidCharge() {
            Vec3 lookAngle = this.gusher.getLookAngle().scale(1.8D);
            AcidCharge acidCharge = new AcidCharge(this.gusher.level(), this.gusher.getX() + lookAngle.x, this.gusher.getY() + this.gusher.getBbHeight() + 2.0F, this.gusher.getZ() + lookAngle.z);
            float shootAngle = Mth.clamp(this.gusher.getXRot() - 72.5F, -85.0F, -72.5F);
            acidCharge.shootFromRotation(this.gusher, shootAngle, this.gusher.getYRot(), 0.0F, 0.55F, 20.0F);
            this.gusher.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F * SinewSoundUtils.randomizePitch(this.gusher));
            this.gusher.level().addFreshEntity(acidCharge);
        }

        private int getAirAbove() {
            int air = 0;
            BlockPos.MutableBlockPos checkPos = this.gusher.blockPosition().above(2).mutable();
            while (this.gusher.level().getBlockState(checkPos).isEmpty()) {
                air++;
                checkPos.move(0, 1, 0);
                if (air > 8) {
                    break;
                }
            }
            return air;
        }
    }
}
