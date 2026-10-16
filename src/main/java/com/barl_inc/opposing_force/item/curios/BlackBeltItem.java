package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.OpposingForce;
import com.google.common.collect.Multimap;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class BlackBeltItem extends CurioItem {

    private static final ResourceLocation ATTRIBUTE_MODIFIER = OpposingForce.location("black_belt");

    public BlackBeltItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, SlotContext slotContext, ItemStack stack) {
        this.addAttributeModifier(map, SinewAttributes.DODGE_CHANCE, new AttributeModifier(ATTRIBUTE_MODIFIER, 0.1F, AttributeModifier.Operation.ADD_VALUE));
    }
}
