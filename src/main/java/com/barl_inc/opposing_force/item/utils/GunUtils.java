package com.barl_inc.opposing_force.item.utils;

import com.barl_inc.opposing_force.registry.OFDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class GunUtils {

    public static void applyDualWieldCooldown(Player player, ItemStack item, InteractionHand hand, Predicate<ItemStack> predicate, int cooldown) {
        if (cooldown <= 0) {
            return;
        }
        boolean gunInOtherHand = predicate.test(player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND));
        player.getCooldowns().addCooldown(item.getItem(), gunInOtherHand ? cooldown * 2 / 3 : cooldown);
    }

    public static boolean shouldSwap(Player player, ItemStack item, InteractionHand hand, Predicate<ItemStack> predicate) {
        boolean hasSwap = item.has(OFDataComponents.SWAP_HAND);
        boolean mainHand = hand == InteractionHand.MAIN_HAND;
        boolean gunInOtherHand = predicate.test(player.getItemInHand(mainHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND));
        if (mainHand && hasSwap && gunInOtherHand) {
            return true;
        }
        if (mainHand && !hasSwap && gunInOtherHand) {
            item.set(OFDataComponents.SWAP_HAND, true);
        }
        if (!mainHand && hasSwap) {
            item.remove(OFDataComponents.SWAP_HAND);
        }
        if (!mainHand && gunInOtherHand) {
            player.getItemInHand(InteractionHand.MAIN_HAND).remove(OFDataComponents.SWAP_HAND);
        }
        player.startUsingItem(hand);
        return false;
    }
}
