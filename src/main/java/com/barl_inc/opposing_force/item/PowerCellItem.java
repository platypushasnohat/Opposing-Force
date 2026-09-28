package com.barl_inc.opposing_force.item;

import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class PowerCellItem extends PoweredItem {

    public PowerCellItem(Properties properties) {
        super(properties, 64);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("Refill using Redstone Dust").withStyle(ChatFormatting.GRAY));
        if (this.isDrained(stack)) {
            tooltipComponents.add(Component.translatable("item.opposing_force.powered_item.power", this.getMaxPower(stack) - this.getPower(stack), this.getMaxPower(stack)).withColor(stack.getBarColor()));
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.is(Tags.Items.DUSTS_REDSTONE) && this.isDrained(stack)) {
            final int efficiencyRate = 2;
            int power = this.getPower(stack);
            int powerAmount = other.getCount() * efficiencyRate;
            this.setPower(stack, power - powerAmount);
            if (power <= efficiencyRate / 2) {
                other.shrink(Math.round((float) power / ((float) efficiencyRate / 2)));
            } else {
                other.shrink(Math.round((float) power / efficiencyRate));
            }
            player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, SinewSoundUtils.randomizePitch(player));
            return true;
        }
        return false;
    }
}
