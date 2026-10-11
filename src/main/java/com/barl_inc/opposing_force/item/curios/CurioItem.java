package com.barl_inc.opposing_force.item.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLLoader;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CurioItem extends AbstractCurioItem {

    public CurioItem(Properties properties) {
        super(properties);
    }

    public void addTooltipLines(Consumer<Component> consumer) {
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
