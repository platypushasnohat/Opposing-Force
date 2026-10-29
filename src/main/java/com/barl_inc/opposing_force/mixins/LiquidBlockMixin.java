package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.item.curios.FluidWalkingCurio;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin {

    @Unique
    private static final VoxelShape[] opposingForce$SURFACES = new VoxelShape[9];

    static {
        for (int amount = 0; amount < opposingForce$SURFACES.length; amount++) {
            opposingForce$SURFACES[amount] = Block.box(0.0D, 0.0D, 0.0D, 16.0D, amount * 16.0D / 9.0D, 16.0D);
        }
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getCollisionShape")
    private VoxelShape opposingForce$getCollisionShape(VoxelShape original, BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        FluidState fluidState = state.getFluidState();
        if (context instanceof EntityCollisionContext entityContext && entityContext.getEntity() instanceof Player player && !level.getFluidState(pos.above()).getType().isSame(fluidState.getType())) {
            VoxelShape surface = opposingForce$SURFACES[fluidState.getAmount()];
            if (context.isAbove(surface, pos, false) && FluidWalkingCurio.canStandOn(player, fluidState)) {
                return surface;
            }
        }
        return original;
    }
}
