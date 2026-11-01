package com.barl_inc.opposing_force.registry.misc;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.awt.*;
import java.util.function.UnaryOperator;

public class OFEnumProxy {

    public static final EnumProxy<Rarity> RARITY_IRIDESCENT = new EnumProxy<>(
            Rarity.class,
            -1,
            OpposingForce.MOD_ID + ":rarity_iridescent",
            (UnaryOperator<Style>) style -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 10000) / 10000.0F, 1.0F, 1.0F))
    );

    public static final EnumProxy<Rarity> RARITY_SPECTRAL = new EnumProxy<>(
            Rarity.class,
            -1,
            OpposingForce.MOD_ID + ":rarity_spectral",
            (UnaryOperator<Style>) style -> style.withColor(0x68d3dc)
    );

}
