package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.item.magic.CursedRinglineItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RingmasterItem extends CursedRinglineItem {

    public RingmasterItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasMana(Player player, ItemStack weapon) {
        return true;
    }

    @Override
    public void consumeMana(Player player, ItemStack weapon, int amount) {
    }

    @Override
    public int getRingCount(ItemStack weapon) {
        return 30;
    }

    @Override
    public int getRingLifetime(ItemStack weapon) {
        return 400;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(OFDebugItems.debugTooltip());
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
