package com.barl_inc.opposing_force.item.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Consumer;

public class RelicFeatherItem extends CurioItem {

    public RelicFeatherItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addTooltipLines(Consumer<Component> consumer) {
        consumer.accept(Component.translatable("item.opposing_force.relic_feather.description").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity entity = context.entity();
        AttributeInstance gravity = entity.getAttribute(Attributes.GRAVITY);
        if (gravity != null) {
            if (entity.getDeltaMovement().y() <= -0.06D && !entity.onGround() && !entity.isFallFlying() && !entity.isInFluidType() && !entity.isShiftKeyDown() && gravity.getValue() > 0.0075D) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
                entity.resetFallDistance();
            }
        }
        super.curioTick(context, stack);
    }
}
