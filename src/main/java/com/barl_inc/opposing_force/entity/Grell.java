package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.FlyingWanderGoal;
import com.barl_inc.opposing_force.entity.base.BasicFlyingMonster;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class Grell extends BasicFlyingMonster {

    public final SmoothAnimationState chargeAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState brainAnimationState = new SmoothAnimationState();

    public Grell(EntityType<? extends Grell> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 20;
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.FLYING_SPEED, 0.35F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GrellAttackGoal(this));
        this.goalSelector.addGoal(2, new FlyingWanderGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    public static boolean checkGrellSpawnRules(EntityType<Grell> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() < 40 && checkMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    public void setupAnimationStates() {
        boolean free = this.getAnimationState() == 0;
        this.idleAnimationState.animateWhen(free, this.tickCount);
        this.walkAnimationState.animateWhen(free && !this.isAggressive(), this.tickCount);
        this.chargeAnimationState.animateWhen(free && this.isAggressive(), this.tickCount);
        this.brainAnimationState.animateWhen(this.isAlive(), this.tickCount);
    }

    private static class GrellAttackGoal extends AttackGoal {

        private final Grell grell;

        public GrellAttackGoal(Grell grell) {
            super(grell);
            this.grell = grell;
        }

        @Override
        public void tick() {
            LivingEntity target = this.grell.getTarget();
            if (target != null) {
                this.lookAtTarget(target, 30.0F, 30.0F);
                this.grell.getNavigation().moveTo(target, 1.6D);
            }
        }
    }
}
