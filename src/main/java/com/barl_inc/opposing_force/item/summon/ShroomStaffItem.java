package com.barl_inc.opposing_force.item.summon;

import com.barl_inc.opposing_force.item.base.SummonWeaponItem;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.platypushasnohat.sinew.entity.base.SummonableMonster;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ShroomStaffItem extends SummonWeaponItem {

    public ShroomStaffItem(Properties properties) {
        super(properties, OFEntities.MUSHY, 7.0D, 30.0D, 20);
    }

    @Override
    protected EntityType<? extends SummonableMonster> getSummonType(Player player) {
        if (player.isShiftKeyDown()) {
            return OFEntities.BRICK.get();
        }
        else {
            return super.getSummonType(player);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable(this.getDescriptionId() + ".sneak_description").withStyle(ChatFormatting.GRAY));
    }
}
