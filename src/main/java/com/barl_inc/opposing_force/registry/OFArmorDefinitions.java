package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.item.ArmorDefinition;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterials;

public class OFArmorDefinitions {

    public static final ArmorDefinition MOON_SHOES = new ArmorDefinition.Builder()
            .material(ArmorMaterials.DIAMOND)
            .attribute(Attributes.MOVEMENT_SPEED, 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, EquipmentSlotGroup.FEET)
            .attribute(Attributes.GRAVITY, -0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, EquipmentSlotGroup.FEET)
            .texture(slot ->OpposingForce.location("textures/models/armor/moon_shoes.png"))
            .build();
}
