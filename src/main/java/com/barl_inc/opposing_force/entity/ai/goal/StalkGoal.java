package com.barl_inc.opposing_force.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class StalkGoal extends Goal {

    private final PathfinderMob mob;
    private final double playerSearchRange;
    private final double minPlayerDistance;
    private final double maxPlayerDistance;
    private final double speed;
    @Nullable
    private Player player;
    private int pauseTicks;

    public StalkGoal(PathfinderMob mob) {
        this(mob, 48.0D, 12.0D, 22.0D, 1.0D);
    }

    public StalkGoal(PathfinderMob mob, double playerSearchRange, double minPlayerDistance, double maxPlayerDistance, double speed) {
        this.mob = mob;
        this.playerSearchRange = playerSearchRange;
        this.minPlayerDistance = minPlayerDistance;
        this.maxPlayerDistance = maxPlayerDistance;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = this.mob.level().getNearestPlayer(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.playerSearchRange, EntitySelector.NO_SPECTATORS);
        return this.player != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.player != null && this.player.isAlive() && !this.player.isSpectator() && this.mob.distanceToSqr(this.player) < this.playerSearchRange * this.playerSearchRange;
    }

    @Override
    public void start() {
        this.pauseTicks = 0;
    }

    @Override
    public void stop() {
        this.player = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.player != null) {
            this.mob.getLookControl().setLookAt(this.player, 30.0F, 30.0F);
            if (this.mob.getNavigation().isDone()) {
                this.mob.lookAt(this.player, 30.0F, 30.0F);
                if (this.pauseTicks > 0) {
                    this.pauseTicks--;
                } else {
                    Vec3 spot = this.findSpot(this.player);
                    if (spot != null) {
                        this.mob.getNavigation().moveTo(spot.x, spot.y, spot.z, this.speed);
                    }
                    this.pauseTicks = 40 + this.mob.getRandom().nextInt(80);
                }
            }
        }
    }

    @Nullable
    private Vec3 findSpot(Player player) {
        Level level = this.mob.level();
        Vec3 fallback = null;
        for (int i = 0; i < 10; i++) {
            float angle = this.mob.getRandom().nextFloat() * Mth.TWO_PI;
            double distance = this.minPlayerDistance + this.mob.getRandom().nextDouble() * (this.maxPlayerDistance - this.minPlayerDistance);
            int x = Mth.floor(player.getX() + Mth.cos(angle) * distance);
            int z = Mth.floor(player.getZ() + Mth.sin(angle) * distance);
            BlockPos pos = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
            if (level.getFluidState(pos).isEmpty() && level.getFluidState(pos.below()).isEmpty()) {
                if (this.isByTree(pos)) {
                    return Vec3.atBottomCenterOf(pos);
                }
                if (fallback == null) {
                    fallback = Vec3.atBottomCenterOf(pos);
                }
            }
        }
        return fallback;
    }

    private boolean isByTree(BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.mob.level().getBlockState(pos.relative(direction)).is(BlockTags.LOGS)) {
                return true;
            }
        }
        return false;
    }
}