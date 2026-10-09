package com.barl_inc.opposing_force.entity.ai.goal;

import com.platypushasnohat.sinew.entity.utils.AnimatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class JukeboxDanceGoal<T extends PathfinderMob & AnimatedEntity> extends Goal {

    private final T mob;
    private final int danceAnimation;
    private final int range;
    private int scanCooldown;

    @Nullable
    private BlockPos jukeboxPos;

    public JukeboxDanceGoal(T mob, int danceAnimation) {
        this(mob, danceAnimation, 3);
    }

    public JukeboxDanceGoal(T mob, int danceAnimation, int range) {
        this.mob = mob;
        this.danceAnimation = danceAnimation;
        this.range = range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean isDancing() {
        return this.jukeboxPos != null;
    }

    @Override
    public boolean canUse() {
        this.updateJukebox();
        return this.isDancing();
    }

    @Override
    public boolean canContinueToUse() {
        this.updateJukebox();
        return this.isDancing();
    }

    @Override
    public void start() {
        this.mob.setTarget(null);
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
        this.mob.setAnimationState(this.danceAnimation);
    }

    @Override
    public void stop() {
        if (this.mob.getAnimationState() == this.danceAnimation) {
            this.mob.setAnimationState(0);
        }
    }

    private void updateJukebox() {
        if (--this.scanCooldown > 0) {
            return;
        }
        this.scanCooldown = 10;

        if (this.jukeboxPos != null && (!this.jukeboxPos.closerToCenterThan(this.mob.position(), this.range + 0.5D) || !isPlayingJukebox(this.mob.level(), this.jukeboxPos))) {
            this.jukeboxPos = null;
        }
        if (this.jukeboxPos == null) {
            BlockPos center = this.mob.blockPosition();
            for (BlockPos pos : BlockPos.betweenClosed(center.offset(-this.range, -this.range, -this.range), center.offset(this.range, this.range, this.range))) {
                if (isPlayingJukebox(this.mob.level(), pos)) {
                    this.jukeboxPos = pos.immutable();
                    break;
                }
            }
        }
    }

    private static boolean isPlayingJukebox(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(Blocks.JUKEBOX) && state.getValue(JukeboxBlock.HAS_RECORD)
                && level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox
                && jukebox.getSongPlayer().isPlaying();
    }
}
