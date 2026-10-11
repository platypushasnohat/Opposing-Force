package com.barl_inc.opposing_force.item.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PotOfGreenItem extends CurioItem {

    public PotOfGreenItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addTooltipLines(Consumer<Component> consumer) {
        consumer.accept(Component.translatable("item.opposing_force.pot_of_green.description").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        return 1;
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, @Nullable LootContext lootContext, ItemStack stack) {
        return 1;
    }
}
