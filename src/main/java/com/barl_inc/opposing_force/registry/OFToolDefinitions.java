package com.barl_inc.opposing_force.registry;

import com.platypushasnohat.sinew.item.ToolDefinition;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class OFToolDefinitions {

    public static final ToolDefinition EMERALD = new ToolDefinition.Builder()
            .tier(OFItemTiers.EMERALD)
            .attribute(SinewAttributes.EXPERIENCE_BOOST, 0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .build();

    public static final ToolDefinition CHITIN = new ToolDefinition.Builder()
            .tier(OFItemTiers.CHITIN)
            .build();
}
