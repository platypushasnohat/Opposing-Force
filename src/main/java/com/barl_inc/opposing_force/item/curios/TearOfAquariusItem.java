package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.OpposingForce;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class TearOfAquariusItem extends CurioItem {

    private static final ResourceLocation ATTRIBUTE_MODIFIER = OpposingForce.location("tear_of_aquarius");

    public TearOfAquariusItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, SlotContext slotContext, ItemStack stack) {
        this.addAttributeModifier(map, Attributes.OXYGEN_BONUS, new AttributeModifier(ATTRIBUTE_MODIFIER, 1.0F, AttributeModifier.Operation.ADD_VALUE));
        this.addAttributeModifier(map, Attributes.WATER_MOVEMENT_EFFICIENCY, new AttributeModifier(ATTRIBUTE_MODIFIER, 0.33F, AttributeModifier.Operation.ADD_VALUE));
    }
}
