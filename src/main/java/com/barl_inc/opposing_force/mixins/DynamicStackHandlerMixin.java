package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.registry.datapack.tags.OFItemTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "top.theillusivec4.curios.common.inventory.DynamicStackHandler", remap = false)
public abstract class DynamicStackHandlerMixin extends ItemStackHandler {
    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        return stack.is(OFItemTags.TRINKETS) ? 1 : super.getStackLimit(slot, stack);
    }
}
