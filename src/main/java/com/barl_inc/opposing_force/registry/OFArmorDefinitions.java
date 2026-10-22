package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.item.ArmorDefinition;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class OFArmorDefinitions {

    public static final ArmorDefinition EMERALD_ARMOR = new ArmorDefinition.Builder()
            .material(OFArmorMaterials.EMERALD)
            .attribute(SinewAttributes.EXPERIENCE_BOOST, 0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .texture(slot -> slot == EquipmentSlot.LEGS ? OpposingForce.location("textures/models/armor/emerald_layer_2.png") : OpposingForce.location("textures/models/armor/emerald_layer_1.png"))
            .build();

}
