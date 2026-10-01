package com.barl_inc.opposing_force.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;

public class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public SpiderTargetGoal(Spider spider, Class<T> entityTypeToTarget) {
        super(spider, entityTypeToTarget, true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canUse() {
        float f = this.mob.getLightLevelDependentMagicValue();
        return !(f >= 0.5F) && super.canUse();
    }
}
