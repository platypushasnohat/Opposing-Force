package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.ai.goal.FlyingWanderGoal;
import com.barl_inc.opposing_force.entity.base.BasicFlyingMonster;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Gnat extends BasicFlyingMonster {

    private static final int ATTACK_ANIMATION = 1;

    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);

    public Gnat(EntityType<? extends Gnat> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 2;
        this.moveControl = new FlyingMoveControl(this, 20, true);
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
        this.goalSelector.addGoal(2, new FlyingWanderGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
    }

    @Override
    public void tick() {
        super.tick();
        OpposingForce.PROXY.playSound(this, (byte) 2);
    }

    @Override
    public void remove(RemovalReason reason) {
        OpposingForce.PROXY.clearSoundCacheFor(this);
        super.remove(reason);
    }

    @Override
    public void setupAnimationStates() {
        this.flyAnimationState.animateWhen(this.isFlying(), this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
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
                    if (distance <= this.getAttackReachSqr(target)) {
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
