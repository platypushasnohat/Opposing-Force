package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.registry.OFDataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public abstract class PoweredItem extends Item {

    public PoweredItem(Properties properties, int maxPower) {
        super(properties.stacksTo(1).component(OFDataComponents.MAX_POWER, maxPower).component(OFDataComponents.POWER, 0));
    }

    public void drainPower(ItemStack stack, int amount, @Nullable LivingEntity entity) {
        if (stack.getComponents().has(OFDataComponents.POWER.get()) && stack.getComponents().has(OFDataComponents.MAX_POWER.get())) {
            if (entity == null || !entity.hasInfiniteMaterials()) {
                int i = this.getPower(stack) + amount;
                this.setPower(stack, i);
            }
        }
    }

    public int getPower(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(OFDataComponents.POWER, 0), 0, this.getMaxPower(stack));
    }

    public int getMaxPower(ItemStack stack) {
        return stack.getOrDefault(OFDataComponents.MAX_POWER, 0);
    }

    public boolean isDrained(ItemStack stack) {
        return this.getPower(stack) > 0;
    }

    public void setPower(ItemStack stack, int damage) {
        stack.set(OFDataComponents.POWER, Mth.clamp(damage, 0, this.getMaxPower(stack)));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xff246d;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float) this.getPower(stack) * 13.0F / (float) this.getMaxPower(stack));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.isDrained(stack);
    }
}
