package com.barl_inc.opposing_force.entity.base;

import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothFlyingNavigation;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;

public abstract class BasicFlyingMonster extends AnimatedMonster implements FlyingAnimal {

    public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();

    protected BasicFlyingMonster(EntityType<? extends BasicFlyingMonster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        SmoothFlyingNavigation navigation = new SmoothFlyingNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !level().getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader level) {
        return level.getBlockState(blockPos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround() && this.getDeltaMovement().y < 0.0D && this.getTarget() == null) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.6F, 1.0F));
        }
    }
}
