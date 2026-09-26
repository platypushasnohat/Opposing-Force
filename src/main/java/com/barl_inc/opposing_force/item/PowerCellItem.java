package com.barl_inc.opposing_force.item;

import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class PowerCellItem extends Item {

    public PowerCellItem(Properties properties) {
        super(properties.stacksTo(1).durability(64));
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(Tags.Items.DUSTS_REDSTONE);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (!tooltipFlag.isAdvanced() && stack.isDamaged()) {
            tooltipComponents.add(Component.translatable("item.opposing_force.power_cell.power", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage()).withColor(stack.getBarColor()));
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.is(Tags.Items.DUSTS_REDSTONE) && stack.isDamaged()) {
            int damageAmount = stack.getDamageValue();
            int count = other.getCount();
            stack.setDamageValue(damageAmount - count);
            other.shrink(damageAmount);
            player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, SinewSoundUtils.randomizePitch(player));
            return true;
        }
        return false;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xff246d;
    }
}
