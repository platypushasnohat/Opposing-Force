package com.barl_inc.opposing_force.item.curios;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public interface FluidWalkingCurio {

    List<TagKey<Fluid>> getWalkableFluids();

    default boolean canWalkOn(FluidState fluidState) {
        return this.getWalkableFluids().stream().anyMatch(fluidState::is);
    }

    default void floatToSurface(LivingEntity entity) {
        if (!entity.isDescending() && this.getWalkableFluids().stream().anyMatch(fluid -> entity.getFluidHeight(fluid) > 0.0D)) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.5D, 1.0D).add(0.0D, 0.05D, 0.0D));
        }
    }

    static boolean canStandOn(LivingEntity entity, FluidState fluidState) {
        return !entity.isDescending() && CuriosApi.getCuriosInventory(entity).map(inventory -> inventory.isEquipped(stack -> stack.getItem() instanceof FluidWalkingCurio curio && curio.canWalkOn(fluidState))).orElse(false);
    }
}
