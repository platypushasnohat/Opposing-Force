package com.barl_inc.opposing_force.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class LightDependentTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public LightDependentTargetGoal(PathfinderMob mob, Class<T> entityTypeToTarget, boolean mustSee) {
        super(mob, entityTypeToTarget, mustSee, false);
    }

    public LightDependentTargetGoal(PathfinderMob mob, Class<T> entityTypeToTarget, boolean mustSee, boolean mustReach) {
        super(mob, entityTypeToTarget, mustSee, mustReach);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canUse() {
        float f = this.mob.getLightLevelDependentMagicValue();
        return !(f >= 0.5F) && super.canUse();
    }
}
