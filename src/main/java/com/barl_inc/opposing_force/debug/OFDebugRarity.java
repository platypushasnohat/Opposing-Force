package com.barl_inc.opposing_force.debug;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class OFDebugRarity {

    public static final EnumProxy<Rarity> DEBUG = new EnumProxy<>(Rarity.class, -1, "opposing_force:debug", (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.RED));
}
