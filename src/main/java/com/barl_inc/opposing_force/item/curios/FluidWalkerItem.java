package com.barl_inc.opposing_force.item.curios;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class FluidWalkerItem extends CurioItem implements FluidWalkingCurio {

    private final List<TagKey<Fluid>> fluids;

    @SafeVarargs
    public FluidWalkerItem(Properties properties, CurioDefinition definition, TagKey<Fluid>... fluids) {
        super(properties, definition);
        this.fluids = List.of(fluids);
    }

    @Override
    public List<TagKey<Fluid>> getWalkableFluids() {
        return this.fluids;
    }

    @Override
    public boolean canEquip(SlotContext context, ItemStack stack) {
        return super.canEquip(context, stack) && CuriosApi.getCuriosInventory(context.entity()).map(inventory -> !inventory.isEquipped(other -> other.getItem() instanceof ComboBootsItem)).orElse(true);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        super.curioTick(context, stack);
        this.floatToSurface(context.entity());
    }
}
