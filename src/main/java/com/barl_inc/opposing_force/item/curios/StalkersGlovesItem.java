package com.barl_inc.opposing_force.item.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class StalkersGlovesItem extends CurioItem {

    public StalkersGlovesItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addTooltipLines(Consumer<Component> consumer) {
        consumer.accept(Component.translatable("item.opposing_force.stalkers_gloves.description").withStyle(ChatFormatting.BLUE));
    }
}
