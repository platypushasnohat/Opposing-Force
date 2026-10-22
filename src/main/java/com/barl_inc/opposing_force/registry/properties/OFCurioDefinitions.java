package com.barl_inc.opposing_force.registry.properties;

import com.barl_inc.opposing_force.item.curios.CurioDefinition;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class OFCurioDefinitions {

    public static final CurioDefinition RELIC_FEATHER = new CurioDefinition.Builder()
            .description("item.opposing_force.relic_feather.description")
            .build();

    public static final CurioDefinition BEETLE_HUSK = new CurioDefinition.Builder()
            .attribute(Attributes.ARMOR, 2.0F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(SinewAttributes.FIRE_RESISTANCE, 0.1F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(SinewAttributes.PROJECTILE_RESISTANCE, 0.1F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(SinewAttributes.MAGIC_RESISTANCE, 0.1F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition ENCHANTED_CHISEL = new CurioDefinition.Builder()
            .attribute(Attributes.MINING_EFFICIENCY, 10.0F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition POT_OF_GREEN = new CurioDefinition.Builder()
            .attribute(SinewAttributes.FORTUNE, 1.0F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(SinewAttributes.LOOTING, 1.0F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition THORNY_BRACELET = new CurioDefinition.Builder()
            .attribute(Attributes.ATTACK_DAMAGE, 2.0F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition RANGER_MANUAL = new CurioDefinition.Builder()
            .attribute(SinewAttributes.PROJECTILE_DAMAGE, 1.0F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(Attributes.ATTACK_SPEED, 0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .build();

    public static final CurioDefinition SHARPSHOOTER_COIN = new CurioDefinition.Builder()
            .attribute(SinewAttributes.PROJECTILE_DAMAGE, 2.0F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition TEAR_OF_AQUARIUS = new CurioDefinition.Builder()
            .attribute(Attributes.OXYGEN_BONUS, 1.5F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.4F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition BLACK_BELT = new CurioDefinition.Builder()
            .attribute(SinewAttributes.DODGE_CHANCE, 0.1F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition BLOOD_AMULET = new CurioDefinition.Builder()
            .attribute(SinewAttributes.LIFESTEAL, 0.1F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition FIERY_CARAPACE = new CurioDefinition.Builder()
            .attribute(SinewAttributes.FIRE_RESISTANCE, 0.15F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(Attributes.BURNING_TIME, -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .build();

    public static final CurioDefinition URANIUM_KNUCKLES = new CurioDefinition.Builder()
            .attribute(SinewAttributes.ARMOR_PIERCING, 0.15F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition MOMENTUM_GAUNTLET = new CurioDefinition.Builder()
            .description("item.opposing_force.momentum_gauntlet.description")
            .build();

    public static final CurioDefinition LUCKY_TACKLEBOX = new CurioDefinition.Builder()
            .attribute(SinewAttributes.FISHING_LURE, 5.0F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(SinewAttributes.FISHING_LUCK, 1.0F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition SLIMY_SHIELD = new CurioDefinition.Builder()
            .attribute(Attributes.ARMOR, 3.0F, AttributeModifier.Operation.ADD_VALUE)
            .description("item.opposing_force.slimy_shield.description")
            .build();

    public static final CurioDefinition STALKER_GLOVES = new CurioDefinition.Builder()
            .description("item.opposing_force.stalker_gloves.description")
            .build();

    public static final CurioDefinition MUNITIONS_BOX = new CurioDefinition.Builder()
            .attribute(SinewAttributes.PROJECTILE_DAMAGE, 1.0F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(SinewAttributes.AMMO_CONSERVATION, 0.2F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final CurioDefinition MOON_SHOES = new CurioDefinition.Builder()
            .attribute(SinewAttributes.AIR_SPEED, 0.15F, AttributeModifier.Operation.ADD_VALUE)
            .attribute(Attributes.GRAVITY, -0.4F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .build();

}
