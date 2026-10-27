package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.item.curios.CurioDefinition;
import com.barl_inc.opposing_force.item.curios.CurioItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SlimewardItem extends CurioItem {

    public SlimewardItem(Properties properties, CurioDefinition definition) {
        super(properties, definition);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(OFDebugItems.debugTooltip());
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
