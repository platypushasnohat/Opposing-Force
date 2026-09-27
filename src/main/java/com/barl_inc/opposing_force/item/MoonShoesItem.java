package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.registry.OFArmorDefinitions;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.item.SkinLayerHidingArmorItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MoonShoesItem extends SkinLayerHidingArmorItem {

    public MoonShoesItem(Properties properties) {
        super(Type.BOOTS, properties, OFArmorDefinitions.MOON_SHOES);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (entity instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.FEET).is(OFItems.MOON_SHOES.get())) {
            living.resetFallDistance();
            if (level.isClientSide && !living.onGround() && !living.onClimbable() && !living.isInWaterOrBubble() && !living.isPassenger()) {
                if (living.getDeltaMovement().lengthSqr() >= 0.002D && level.getRandom().nextFloat() < 0.4F) {
                    Vec3 position = living.position();
                    level.addParticle(ParticleTypes.CLOUD, position.x, position.y, position.z, (level.getRandom().nextFloat() - 0.5F) / 3.0F, 0.0D, (level.getRandom().nextFloat() - 0.5F) / 3.0F);
                }
            }
        }
    }
}
