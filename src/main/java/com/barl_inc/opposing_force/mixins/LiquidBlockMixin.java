package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.item.curios.FluidWalkingCurio;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin {

    @ModifyReturnValue(at = @At("RETURN"), method = "getCollisionShape")
    private VoxelShape opposingForce$getCollisionShape(VoxelShape original, BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        FluidState fluidState = state.getFluidState();
        if (context instanceof EntityCollisionContext entityContext && entityContext.getEntity() instanceof LivingEntity living && !level.getFluidState(pos.above()).getType().isSame(fluidState.getType())) {
            VoxelShape surface = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, fluidState.getOwnHeight(), 1.0D);
            if (context.isAbove(surface, pos, false) && FluidWalkingCurio.canStandOn(living, fluidState)) {
                return surface;
            }
        }
        return original;
    }
}
