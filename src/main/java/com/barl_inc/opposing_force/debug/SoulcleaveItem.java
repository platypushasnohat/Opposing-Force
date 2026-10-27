package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.item.melee.SpectralCleaverItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SoulcleaveItem extends SpectralCleaverItem {

    public SoulcleaveItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public int getSoulCount() {
        return 5;
    }

    @Override
    public int getSoulCooldown() {
        return 2;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(OFDebugItems.debugTooltip());
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
