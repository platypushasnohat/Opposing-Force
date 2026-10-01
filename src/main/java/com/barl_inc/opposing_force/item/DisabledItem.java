package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.registry.OFDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/*
 * This Source Code Form is subject to the terms of the GNU Lesser General Public License v3.0 or later
 * If a copy of the LGPL-3.0-or-later was not distributed with this file, You can obtain one at
 * https://www.gnu.org/licenses/lgpl-3.0.md
 *
 * Source: Malum - https://github.com/SammySemicolon/Malum-Mod/tree/1.21.1
 * Modifications by: Opposing Force - 10/1/2026
 */

public class DisabledItem extends Item {

    public DisabledItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof ServerPlayer player) {
            DisabledComponent disabledComponent = stack.get(OFDataComponents.DISABLED);
            if (disabledComponent != null) {
                long time = disabledComponent.time();
                if (level.getGameTime() >= time) {
                    enableItem(player, slotId);
                    return;
                }
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    public static void disableItem(ServerPlayer player, int slot, Supplier<Item> disabledItem) {
        Inventory inventory = player.getInventory();
        ItemStack disabled = disabledItem.get().getDefaultInstance();
        disabled.set(OFDataComponents.DISABLED, new DisabledComponent(inventory.getItem(slot), player.level().getGameTime() + 200));
        inventory.setItem(slot, disabled);
    }

    public static void enableItem(ServerPlayer player, int slot) {
        Inventory inventory = player.getInventory();
        ItemStack disabledItem = inventory.getItem(slot);
        DisabledComponent disabledComponent = disabledItem.get(OFDataComponents.DISABLED);
        if (disabledComponent != null) {
            ItemStack original = disabledComponent.item();
            if (!original.isEmpty()) {
                inventory.setItem(slot, original);
            }
        }
    }

    public record DisabledComponent(ItemStack item, long time) {

        public static Codec<DisabledComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(DisabledComponent::item),
                Codec.LONG.fieldOf("time").forGetter(DisabledComponent::time)
        ).apply(instance, DisabledComponent::new));

        public static StreamCodec<RegistryFriendlyByteBuf, DisabledComponent> STREAM_CODEC = StreamCodec.composite(
                ItemStack.OPTIONAL_STREAM_CODEC,
                disabledComponent -> disabledComponent.item,
                ByteBufCodecs.VAR_LONG,
                disabledComponent -> disabledComponent.time,
                DisabledComponent::new
        );
    }
}