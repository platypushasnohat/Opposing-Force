package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.properties.OFCurioDefinitions;
import com.barl_inc.opposing_force.registry.properties.OFItemTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class OFDebugItems {

    public static final DeferredRegister.Items DEBUG_ITEM = DeferredRegister.createItems(OpposingForce.MOD_ID);

    private static final Set<UUID> DEBUG_PLAYERS = Set.of(
            UUID.fromString("380df991-f603-344c-a090-369bad2a924a"),
            UUID.fromString("3562ab33-f01b-4801-aab5-807f3750ded1")
    );

    public static final DeferredItem<Item> RINGMASTER = DEBUG_ITEM.register("ringmaster", () -> new RingmasterItem(new Item.Properties().rarity(OFDebugRarity.DEBUG.getValue()).fireResistant().stacksTo(1)));
    public static final DeferredItem<Item> KNIGHTSOUL = DEBUG_ITEM.register("knightsoul", () -> new KnightsoulItem(new Item.Properties().rarity(OFDebugRarity.DEBUG.getValue()).fireResistant().stacksTo(1)));
    public static final DeferredItem<Item> SLIMEWARD = DEBUG_ITEM.register("slimeward", () -> new SlimewardItem(new Item.Properties().rarity(OFDebugRarity.DEBUG.getValue()).fireResistant().stacksTo(1), OFCurioDefinitions.SLIMY_SHIELD));
    public static final DeferredItem<Item> SOULCLEAVE = DEBUG_ITEM.register("soulcleave", () -> new SoulcleaveItem(OFItemTiers.SPECTRAL_CLEAVER, new Item.Properties().rarity(OFDebugRarity.DEBUG.getValue()).fireResistant().attributes(SwordItem.createAttributes(OFItemTiers.SPECTRAL_CLEAVER, 3.5F, -2.4F)).component(DataComponents.UNBREAKABLE, new Unbreakable(false))));

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

    public static ItemStack revert(ItemStack stack) {
        Item base = getBase(stack.getItem());
        ItemStack reverted = stack;
        if (base != null) {
            reverted = new ItemStack(base.builtInRegistryHolder(), stack.getCount(), stack.getComponentsPatch());
        }
        return reverted;
    }

    public static void revertContainer(Container container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            ItemStack reverted = revert(stack);
            if (reverted != stack) {
                container.setItem(i, reverted);
            }
        }
    }

    public static void revertCurios(LivingEntity entity) {
        CuriosApi.getCuriosInventory(entity).ifPresent(inventory -> inventory.getCurios().values().forEach(handler -> {
            IDynamicStackHandler stacks = handler.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                ItemStack stack = stacks.getStackInSlot(i);
                ItemStack reverted = revert(stack);
                if (reverted != stack) {
                    stacks.setStackInSlot(i, reverted);
                }
            }
        }));
    }

    @Nullable
    private static Item getBase(Item item) {
        Item base = null;
        if (item == RINGMASTER.get()) {
            base = OFItems.CURSED_RINGLINE.get();
        } else if (item == SOULCLEAVE.get()) {
            base = OFItems.SPECTRAL_CLEAVER.get();
        } else if (item == KNIGHTSOUL.get()) {
            base = OFItems.SPECTRAL_SHIELD.get();
        } else if (item == SLIMEWARD.get()) {
            base = OFItems.SLIMY_SHIELD.get();
        }
        return base;
    }
}
