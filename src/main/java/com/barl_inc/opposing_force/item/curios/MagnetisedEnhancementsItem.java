package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class MagnetisedEnhancementsItem extends CurioItem {

    private static final ResourceLocation MINING_MOMENTUM = OpposingForce.location("mining_momentum");

    public MagnetisedEnhancementsItem(Properties properties, CurioDefinition definition) {
        super(properties, definition);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        LivingEntity entity = context.entity();
        if (!entity.level().isClientSide) {
            CompoundTag data = entity.getPersistentData();
            int momentum = data.getInt("MiningMomentum");
            int timeout = (int) Mth.lerp((float) momentum / 15, 60.0F, 20.0F);
            if (momentum > 0 && entity.level().getGameTime() - data.getLong("MiningMomentumTime") > timeout) {
                setMomentum(entity, 0);
            }
        }
        super.curioTick(context, stack);
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        if (!context.entity().level().isClientSide) {
            setMomentum(context.entity(), 0);
        }
        super.onUnequip(context, newStack, stack);
    }

    public static void addMomentum(LivingEntity entity) {
        if (CuriosApi.getCuriosInventory(entity).map(inventory -> inventory.isEquipped(OFItems.MAGNETISED_ENHANCEMENTS.get())).orElse(false)) {
            setMomentum(entity, Math.min(entity.getPersistentData().getInt("MiningMomentum") + 1, 15));
            entity.getPersistentData().putLong("MiningMomentumTime", entity.level().getGameTime());
        }
    }

    private static void setMomentum(LivingEntity entity, int momentum) {
        entity.getPersistentData().putInt("MiningMomentum", momentum);
        AttributeInstance breakSpeed = entity.getAttribute(Attributes.BLOCK_BREAK_SPEED);
        if (breakSpeed != null) {
            breakSpeed.removeModifier(MINING_MOMENTUM);
            if (momentum > 0) {
                breakSpeed.addTransientModifier(new AttributeModifier(MINING_MOMENTUM, 0.1F * momentum, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }
    }
}
