package com.barl_inc.opposing_force.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.UUID;

@EventBusSubscriber(modid = "opposing_force")
public class DevInteractEvent {

    private static final UUID TARGET = UUID.fromString("142543af-76fe-45d9-8d53-8f169c86ec64");
    private static final String ITEM_NAME = "Peeko Milk";

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        boolean success = true;
        if (!(event.getTarget() instanceof Player target)) return;
        if (!target.getUUID().equals(TARGET)) return;
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();
        if (!held.is(Items.BUCKET)) return;
        if (!player.level().isClientSide) {
            ItemStack milk = new ItemStack(Items.MILK_BUCKET);
            milk.set(DataComponents.CUSTOM_NAME, Component.literal(ITEM_NAME));
            player.setItemInHand(event.getHand(), ItemUtils.createFilledResult(held, player, milk));
            player.level().playSound(null, target.blockPosition(), SoundEvents.COW_MILK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
        event.setCanceled(true);
    }
}