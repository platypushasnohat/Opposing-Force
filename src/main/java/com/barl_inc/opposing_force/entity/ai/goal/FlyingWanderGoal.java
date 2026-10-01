package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.base.BasicFlyingMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FlyingWanderGoal extends Goal {

    private final BasicFlyingMonster monster;

    public FlyingWanderGoal(BasicFlyingMonster monster) {
        this.monster = monster;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.monster.getNavigation().isDone() && this.monster.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.monster.getNavigation().isDone();
    }

    @Override
    public void start() {
        Vec3 location = this.getRandomLocation();
        if (location != null) {
            if (this.monster.onGround()) {
                this.monster.addDeltaMovement(new Vec3(0.0F, 0.3F, 0.0F));
            }
            this.monster.getNavigation().moveTo(this.monster.getNavigation().createPath(BlockPos.containing(location), 1), 1.0);
        }
    }

    @Nullable
    private Vec3 getRandomLocation() {
        Vec3 viewVector = this.monster.getViewVector(0.0F);
        Vec3 hoverPos = HoverRandomPos.getPos(this.monster, 8, 7, viewVector.x, viewVector.z, (float) (Math.PI / 2), 3, 1);
        return hoverPos != null ? hoverPos : AirAndWaterRandomPos.getPos(this.monster, 8, 4, -2, viewVector.x, viewVector.z, (float) (Math.PI / 2));
    }
}