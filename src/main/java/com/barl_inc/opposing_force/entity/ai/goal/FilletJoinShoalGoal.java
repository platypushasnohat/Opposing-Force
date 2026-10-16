package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Fillet;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.Predicate;

public class FilletJoinShoalGoal extends Goal {

    private final Fillet fillet;
    private int timeToRecalcPath;
    private int nextStartTick;

    public FilletJoinShoalGoal(Fillet fillet) {
        this.fillet = fillet;
        this.nextStartTick = this.nextStartTick(fillet);
    }

    protected int nextStartTick(Fillet fillet) {
        return reducedTickDelay(200 + fillet.getRandom().nextInt(200) % 20);
    }

    @Override
    public boolean canUse() {
        if (this.fillet.isGroupLeader() || this.fillet.isCircling()) {
            return false;
        } else if (this.fillet.hasGroupLeader()) {
            return true;
        } else if (this.nextStartTick > 0) {
            this.nextStartTick--;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.fillet);
            Predicate<Fillet> predicate = (fish) -> fish.canGroupGrow() || !fish.hasGroupLeader();
            List<? extends Fillet> list = this.fillet.level().getEntitiesOfClass(this.fillet.getClass(), this.fillet.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
            Fillet shoalingFish = DataFixUtils.orElse(list.stream().filter(Fillet::canGroupGrow).findAny(), this.fillet);
            shoalingFish.addFollowers(list.stream().filter((fish) -> !fish.hasGroupLeader()));
            return this.fillet.hasGroupLeader();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.fillet.hasGroupLeader() && this.fillet.inRangeOfGroupLeader() && !this.fillet.isCircling();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.fillet.leaveGroup();
    }

    @Override
    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.fillet.moveToGroupLeader();
        }
    }
}