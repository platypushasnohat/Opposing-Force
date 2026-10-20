package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Consumer;

public class SpectralGogglesItem extends CurioItem {

    public static final ResourceLocation SPECTRAL_SIGHT = OpposingForce.location("spectral_goggles");

    public SpectralGogglesItem(Properties properties, CurioDefinition definition) {
        super(properties, definition);
    }

    @Override
    public void addTooltipLines(Consumer<Component> consumer) {
        super.addTooltipLines(component -> consumer.accept(component.copy().withStyle(ChatFormatting.ITALIC)));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity entity = context.entity();
        if (!entity.level().isClientSide) {
            SpectralSight.addSource(entity, SPECTRAL_SIGHT);
        }
        super.curioTick(context, stack);
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = context.entity();
        if (!entity.level().isClientSide && !newStack.is(this)) {
            SpectralSight.removeSource(entity, SPECTRAL_SIGHT);
        }
        super.onUnequip(context, newStack, stack);
    }
}