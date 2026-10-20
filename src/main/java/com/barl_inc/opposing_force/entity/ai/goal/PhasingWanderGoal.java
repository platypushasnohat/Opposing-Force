package com.barl_inc.opposing_force.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PhasingWanderGoal extends Goal {

    private final PathfinderMob mob;
    private final double playerSearchRange;
    private final double minPlayerDistance;
    private final double maxPlayerDistance;
    private final double playerHeight;
    private final double idleRange;
    private final double idleHeight;
    @Nullable
    private Vec3 wanderPos;
    private int pauseTicks;

    public PhasingWanderGoal(PathfinderMob mob) {
        this(mob, 64.0D, 6.0D, 20.0D, 7.0D, 8.0D, 4.0D);
    }

    public PhasingWanderGoal(PathfinderMob mob, double playerSearchRange, double minPlayerDistance, double maxPlayerDistance, double playerHeight, double idleRange, double idleHeight) {
        this.mob = mob;
        this.playerSearchRange = playerSearchRange;
        this.minPlayerDistance = minPlayerDistance;
        this.maxPlayerDistance = maxPlayerDistance;
        this.playerHeight = playerHeight;
        this.idleRange = idleRange;
        this.idleHeight = idleHeight;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.pauseTicks > 0) {
            this.pauseTicks--;
            return false;
        }
        if (!this.mob.getNavigation().isDone() || this.mob.getRandom().nextInt(10) != 0) {
            return false;
        }
        this.wanderPos = this.findWanderPos();
        return this.wanderPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        if (this.wanderPos != null) {
            this.mob.getNavigation().moveTo(this.wanderPos.x, this.wanderPos.y, this.wanderPos.z, 1.0D);
        }
    }

    @Override
    public void stop() {
        if (this.mob.getRandom().nextBoolean()) {
            this.pauseTicks = 20 + this.mob.getRandom().nextInt(40);
        }
    }

    @Nullable
    private Vec3 findWanderPos() {
        Player player = this.mob.level().getNearestPlayer(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.playerSearchRange, EntitySelector.NO_SPECTATORS);
        Vec3 center = player != null ? player.position() : this.mob.position();
        double minDistance = player != null ? this.minPlayerDistance : 0.0D;
        double maxDistance = player != null ? this.maxPlayerDistance : this.idleRange;
        double height = player != null ? this.playerHeight : this.idleHeight;
        Vec3 fallback = null;
        for (int i = 0; i < 10; i++) {
            double angle = this.mob.getRandom().nextDouble() * Math.PI * 2.0D;
            double distance = minDistance + this.mob.getRandom().nextDouble() * (maxDistance - minDistance);
            Vec3 pos = center.add(Math.cos(angle) * distance, (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * height, Math.sin(angle) * distance);
            BlockPos blockPos = BlockPos.containing(pos);
            if (this.mob.level().isEmptyBlock(blockPos) && this.mob.level().isEmptyBlock(blockPos.above())) {
                return pos;
            }
            fallback = pos;
        }
        return fallback;
    }
}
