package com.barl_inc.opposing_force.item.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class RelicFeatherItem extends CurioItem {

    public RelicFeatherItem(Properties properties, CurioDefinition definition) {
        super(properties, definition);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity entity = context.entity();
        AttributeInstance gravity = entity.getAttribute(Attributes.GRAVITY);
        if (gravity != null) {
            if (entity.getDeltaMovement().y() <= -0.06D && !entity.onGround() && !entity.isFallFlying() && !entity.isInFluidType() && !entity.isShiftKeyDown() && gravity.getValue() > 0.0075D) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.75D, 1.0D));
                entity.resetFallDistance();
            }
        }
        super.curioTick(context, stack);
    }
}
