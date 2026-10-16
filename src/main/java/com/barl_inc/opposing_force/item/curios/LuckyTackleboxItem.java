package com.barl_inc.opposing_force.item.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class LuckyTackleboxItem extends CurioItem {

    public LuckyTackleboxItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addTooltipLines(Consumer<Component> consumer) {
        consumer.accept(Component.translatable("item.opposing_force.lucky_tacklebox.description.lure").withStyle(ChatFormatting.BLUE));
        consumer.accept(Component.translatable("item.opposing_force.lucky_tacklebox.description.luck").withStyle(ChatFormatting.BLUE));
    }
}
