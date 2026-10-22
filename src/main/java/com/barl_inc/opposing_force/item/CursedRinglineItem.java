package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.RingProjectile;
import com.barl_inc.opposing_force.item.base.MagicWeaponItem;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.item.DisabledItem;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CursedRinglineItem extends MagicWeaponItem {

    public CursedRinglineItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getManaCost(ItemStack weapon) {
        return 2;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(stack);
        if (this.hasMana(player, stack)) {
            if (player instanceof ServerPlayer serverPlayer) {
                int slot = hand == InteractionHand.OFF_HAND ? Inventory.SLOT_OFFHAND : player.getInventory().selected;
                this.consumeMana(player, stack, 1 + player.getRandom().nextInt(2));
                for (int i = 0; i < 5; i++) {
                    level.addFreshEntity(new RingProjectile(level, player, i, slot));
                }
                DisabledItem.disableItem(serverPlayer, slot, OFItems.DISABLED_CURSED_RINGLINE);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(level));
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.getCooldowns().addCooldown(this, 200);
            result = InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        return result;
    }
}
