package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.properties.OFCurioDefinitions;
import com.barl_inc.opposing_force.registry.properties.OFItemTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.UUID;

public class OFDebugItems {

    public static final DeferredRegister.Items DEBUG_ITEM = DeferredRegister.createItems(OpposingForce.MOD_ID);

    private static final Set<UUID> DEBUG_PLAYERS = Set.of(
            UUID.fromString("380df991-f603-344c-a090-369bad2a924a"),
            UUID.fromString("3562ab33-f01b-4801-aab5-807f3750ded1")
    );

    public static final DeferredItem<Item> RINGMASTER = DEBUG_ITEM.register("ringmaster", () -> new RingmasterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KNIGHTSOUL = DEBUG_ITEM.register("knightsoul", () -> new KnightsoulItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SLIMEWARD = DEBUG_ITEM.register("slimeward", () -> new SlimewardItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.SLIMY_SHIELD));
    public static final DeferredItem<Item> SOULCLEAVE = DEBUG_ITEM.register("soulcleave", () -> new SoulcleaveItem(OFItemTiers.SPECTRAL_CLEAVER, new Item.Properties().attributes(SwordItem.createAttributes(OFItemTiers.SPECTRAL_CLEAVER, 3.5F, -2.4F)).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));

    public static void register(IEventBus modEventBus) {
        DEBUG_ITEM.register(modEventBus);
    }

    public static boolean isDebugItem(Item item) {
        return DEBUG_ITEM.getEntries().stream().anyMatch(entry -> entry.get() == item);
    }

    public static boolean isDebugPlayer(Player player) {
        return DEBUG_PLAYERS.contains(player.getUUID());
    }

    public static Component debugTooltip() {
        return Component.translatable("item.opposing_force.tooltip.debug_item").withStyle(ChatFormatting.RED);
    }
}
