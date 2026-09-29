package com.barl_inc.opposing_force.entity;

import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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

public class Gusher extends AnimatedMonster {

    private static final int ATTACK_ANIMATION = 1;
    private static final int GUSH_ANIMATION = 2;

    public final SmoothAnimationState attackOverlayAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState gushAnimationState = new SmoothAnimationState();

    public Gusher(EntityType<? extends Gusher> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 15;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 70.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 8.0D);
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
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.attackOverlayAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.gushAnimationState.animateWhen(this.getAnimationState() == GUSH_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 8.0F, 1.0F);
        this.walkAnimation.update(speed, 0.2F);
    }

    private static class GusherAttackGoal extends AttackGoal {

        private final Gusher gusher;

        public GusherAttackGoal(Gusher gusher) {
            super(gusher);
            this.gusher = gusher;
        }

        @Override
        public void tick() {
            LivingEntity target = this.gusher.getTarget();
            if (target != null) {
                double distance = this.gusher.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.lookAtTarget(target, 20.0F, 20.0F);
                    this.gusher.getNavigation().stop();
                    this.tickBite(target);
                }
                else {
                    this.lookAtTarget(target, 20.0F, 20.0F);
                    if (this.gusher.tickCount % 3 == 0) {
                        this.gusher.getNavigation().moveTo(target, 1.5D);
                    }
                    if (distance <= this.getAttackReachSqr(target, 2.7D)) {
                        this.attackState = 1;
                    }
                }
            }
        }

        private void tickBite(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.gusher.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 14 && this.isInAttackBox(target, 4.6D, 0.8D, -0.5D, true, true)) {
                this.gusher.doHurtTarget(target);
            }
            if (this.timer > 40) {
                this.gusher.setAnimationState(0);
                this.timer = 0;
                this.attackState = 0;
            }
        }
    }
}
