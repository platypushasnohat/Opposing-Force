package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.OpposingForce;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Consumer;

public class FieryCarapaceItem extends CurioItem {

    private static final ResourceLocation ATTRIBUTE_MODIFIER = OpposingForce.location("fiery_carapace");

    public FieryCarapaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addTooltipLines(Consumer<Component> consumer) {
        consumer.accept(Component.translatable("item.opposing_force.fiery_carapace.description").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public void addAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, SlotContext slotContext, ItemStack stack) {
        this.addAttributeModifier(map, Attributes.ARMOR, new AttributeModifier(ATTRIBUTE_MODIFIER, 1.0F, AttributeModifier.Operation.ADD_VALUE));
        this.addAttributeModifier(map, Attributes.BURNING_TIME, new AttributeModifier(ATTRIBUTE_MODIFIER, -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }
}
