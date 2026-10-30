package com.barl_inc.opposing_force.item.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class OFTooltips {

    public static Component description(String key) {
        return Component.translatable(key).withStyle(ChatFormatting.GRAY);
    }

    public static Component stat(String key, float value) {
        return CommonComponents.space().append(Component.translatable(key, ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value))).withStyle(ChatFormatting.DARK_GREEN);
    }

    public static Component magicDamage(float damage, int count) {
        return damage("magic", damage, count);
    }

    public static Component rangedDamage(float damage, int count) {
        return damage("ranged", damage, count);
    }

    private static Component damage(String type, float damage, int count) {
        if (count > 1) {
            return CommonComponents.space().append(Component.translatable("item.opposing_force.tooltip." + type + "_multi_damage", ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(damage), count)).withStyle(ChatFormatting.DARK_GREEN);
        } else {
            return stat("item.opposing_force.tooltip." + type + "_damage", damage);
        }
    }

    public static void addStats(List<Component> tooltip, String header, Component... stats) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(description(header));
        tooltip.addAll(List.of(stats));
    }
}