package com.barl_inc.opposing_force.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public class OFFoodProperties {

    public static final FoodProperties TERROR_LEG = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(0.2F)
            .usingConvertsTo(Items.BONE)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.6F)
            .build();

    public static final FoodProperties FRIED_TERROR_LEG = new FoodProperties.Builder()
            .nutrition(8)
            .saturationModifier(0.7F)
            .usingConvertsTo(Items.BONE)
            .build();

    public static final FoodProperties CRYSTALIZED_HEART = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0.0F)
            .alwaysEdible()
            .build();
}
