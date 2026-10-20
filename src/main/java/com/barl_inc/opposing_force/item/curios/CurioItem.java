package com.barl_inc.opposing_force.item.curios;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLLoader;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CurioItem extends AbstractCurioItem {

    private final CurioDefinition definition;

    public CurioItem(Properties properties, CurioDefinition definition) {
        super(properties);
        this.definition = definition;
    }

    public void addTooltipLines(Consumer<Component> consumer) {
        this.definition.descriptions().forEach(key -> consumer.accept(Component.translatable(key).withStyle(ChatFormatting.BLUE)));
    }

    @Override
    public void addAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, SlotContext context, ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(this);
        this.definition.attributes().forEach(entry -> this.addAttributeModifier(map, entry.attribute(), new AttributeModifier(id, entry.value(), entry.operation())));
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, TooltipContext context, ItemStack stack) {
        List<Component> attributesTooltip = super.getAttributesTooltip(tooltips, context, stack);
        List<Component> tooltipLines = new ArrayList<>();
        this.addTooltipLines(tooltipLines::add);
        if (!tooltipLines.isEmpty()) {
            if (attributesTooltip.isEmpty()) {
                attributesTooltip.add(Component.empty());
                final Map<String, ISlotType> itemStackSlots = CuriosApi.getItemStackSlots(stack, FMLLoader.getDist().isClient());
                itemStackSlots.keySet().stream().findFirst().ifPresent(string -> attributesTooltip.add(Component.translatable("curios.modifiers." + string).withStyle(ChatFormatting.GOLD)));
            }
            attributesTooltip.addAll(tooltipLines);
        }
        return attributesTooltip;
    }
}
