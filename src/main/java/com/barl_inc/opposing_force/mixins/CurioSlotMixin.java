package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.registry.datapack.tags.OFItemTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "top.theillusivec4.curios.common.inventory.CurioSlot", remap = false)
public abstract class CurioSlotMixin extends SlotItemHandler {

    public CurioSlotMixin(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.is(OFItemTags.TRINKETS) ? 1 : super.getMaxStackSize(stack);
    }
}