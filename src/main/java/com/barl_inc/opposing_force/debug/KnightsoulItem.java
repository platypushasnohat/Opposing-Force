package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.item.SpectralShieldItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class KnightsoulItem extends SpectralShieldItem {

    public KnightsoulItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(OFDebugItems.debugTooltip());
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
