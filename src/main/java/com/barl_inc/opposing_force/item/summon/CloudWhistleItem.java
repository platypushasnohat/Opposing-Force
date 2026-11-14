package com.barl_inc.opposing_force.item.summon;

import com.barl_inc.opposing_force.item.base.SummonWeaponItem;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.platypushasnohat.sinew.entity.base.SummonableMonster;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CloudWhistleItem extends SummonWeaponItem {

    public CloudWhistleItem(Properties properties) {
        super(properties, OFEntities.KIRIN, 15.0D, 600.0D, 20);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && SummonableMonster.summon(OFEntities.KIRIN.get(), serverLevel, player, stack, hand, player.position()) != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            player.getCooldowns().addCooldown(this, 20);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
