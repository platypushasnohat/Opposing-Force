package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.ai.goal.FlyingWanderGoal;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.ai.goal.TamedOwnerHurtByTargetGoal;
import com.platypushasnohat.sinew.entity.ai.goal.TamedOwnerHurtTargetGoal;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothFlyingNavigation;
import com.platypushasnohat.sinew.entity.base.SummonableMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

public class Gnat extends SummonableMonster implements FlyingAnimal {

    public static final int ATTACK_ANIMATION = 1;

    public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState summonAnimationState = new SmoothAnimationState();

    public Gnat(EntityType<? extends Gnat> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 2;
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.FLYING_SPEED, 0.5F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GnatAttackGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.2D, 6.0F, 2.0F));
        this.goalSelector.addGoal(3, new FlyingWanderGoal(this));
        this.targetSelector.addGoal(0, new TamedOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new TamedOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, target -> !this.isTame()));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, target -> this.isTame() && target instanceof Enemy && !target.isAlliedTo(this)));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        SmoothFlyingNavigation navigation = new SmoothFlyingNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !level().getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D && this.getTarget() == null) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.6F, 1.0F));
        }
        OpposingForce.PROXY.playSound(this, (byte) 2);
    }

    @Override
    public void remove(RemovalReason reason) {
        OpposingForce.PROXY.clearSoundCacheFor(this);
        super.remove(reason);
    }

    @Override
    public float getSummonCost() {
        return 0.5F;
    }

    @Override
    public void setupAnimationStates() {
        this.flyAnimationState.animateWhen(this.isFlying(), this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.summonAnimationState.animateWhen(this.isSummoned() && this.tickCount < 20, this.tickCount);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private static class GnatAttackGoal extends AttackGoal {

        private final Gnat gnat;
        private int attackCooldown;

        public GnatAttackGoal(Gnat gnat) {
            super(gnat);
            this.gnat = gnat;
        }

        @Override
        public void start() {
            super.start();
            this.attackCooldown = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.gnat.getTarget();
            if (target != null) {
                double distance = this.gnat.distanceToSqr(target);
                this.lookAtTarget(target, 45.0F, 45.0F);
                this.gnat.getNavigation().moveTo(target.getX(), target.getEyeY(), target.getZ(), 1.2D);
                if (this.gnat.onGround()) {
                    this.gnat.addDeltaMovement(new Vec3(0.0F, 0.3F, 0.0F));
                }
                if (this.attackState == 1) {
                    this.tickAttack(target);
                }
                else {
                    if (this.attackCooldown > 0) {
                        this.attackCooldown--;
                    }
                    if (distance <= this.getAttackReachSqr(target) && this.attackCooldown <= 0) {
                        this.attackState = 1;
                    }
                }
            }
        }

        private void tickAttack(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.gnat.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 12 && this.isInAttackRange(target, 0.7D)) {
                this.gnat.doHurtTarget(target);
            }
            if (this.timer > 20) {
                this.gnat.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown = 3 + this.gnat.getRandom().nextInt(2);
                this.attackState = 0;
            }
        }
    }
}
