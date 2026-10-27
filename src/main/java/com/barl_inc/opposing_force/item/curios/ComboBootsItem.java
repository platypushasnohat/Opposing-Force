package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.registry.properties.OFCurioDefinitions;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class ComboBootsItem extends MoonShoesItem implements FluidWalkingCurio {

    public ComboBootsItem(Properties properties) {
        super(properties, OFCurioDefinitions.COMBO_BOOTS);
    }

    @Override
    public List<TagKey<Fluid>> getWalkableFluids() {
        return List.of(FluidTags.WATER, FluidTags.LAVA);
    }

    @Override
    protected boolean conflictsWith(ItemStack other) {
        return other.getItem() instanceof FluidWalkerItem || other.getItem() instanceof MoonShoesItem && !(other.getItem() instanceof ComboBootsItem);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        super.curioTick(context, stack);
        this.floatToSurface(context.entity());
    }
}
