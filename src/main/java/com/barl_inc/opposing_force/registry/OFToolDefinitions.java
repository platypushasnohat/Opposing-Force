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

    public static final ToolDefinition LAPIS_LAZULI = new ToolDefinition.Builder()
            .tier(OFItemTiers.LAPIS_LAZULI)
            .attribute(SinewAttributes.MAGIC_DAMAGE, 2.0F, AttributeModifier.Operation.ADD_VALUE)
            .build();

    public static final ToolDefinition BUZZSAW = new ToolDefinition.Builder()
            .tier(OFItemTiers.BUZZSAW)
            .build();

}
