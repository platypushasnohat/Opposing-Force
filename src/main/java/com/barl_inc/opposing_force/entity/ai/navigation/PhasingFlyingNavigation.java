package com.barl_inc.opposing_force.entity.ai.navigation;

import com.google.common.collect.Lists;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothFlyingNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.Set;

public class PhasingFlyingNavigation extends SmoothFlyingNavigation {

    public PhasingFlyingNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Nullable
    @Override
    protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
        if (targets.isEmpty() || !this.canUpdatePath()) {
            return null;
        }
        BlockPos target = targets.iterator().next();
        return this.patchPath(new Path(Lists.newArrayList(new Node(target.getX(), target.getY(), target.getZ())), target, true));
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return true;
    }
}
