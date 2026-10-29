package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.registry.properties.OFCurioDefinitions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class MoonShoesItem extends CurioItem {

    public MoonShoesItem(Properties properties) {
        this(properties, OFCurioDefinitions.MOON_SHOES);
    }

    protected MoonShoesItem(Properties properties, CurioDefinition definition) {
        super(properties, definition);
    }

    protected boolean conflictsWith(ItemStack other) {
        return other.getItem() instanceof ComboBootsItem;
    }

    @Override
    public boolean canEquip(SlotContext context, ItemStack stack) {
        return super.canEquip(context, stack) && CuriosApi.getCuriosInventory(context.entity()).map(inventory -> !inventory.isEquipped(this::conflictsWith)).orElse(true);
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        super.curioTick(context, stack);
        LivingEntity living = context.entity();
        Level level = living.level();
        living.resetFallDistance();
        if (level.isClientSide && !living.onGround() && !living.onClimbable() && !living.isInWaterOrBubble() && !living.isPassenger()) {
            if (living.getDeltaMovement().lengthSqr() >= 0.002D && level.getRandom().nextFloat() < 0.35F) {
                Vec3 position = living.position();
                level.addParticle(ParticleTypes.CLOUD, position.x, position.y, position.z, (level.getRandom().nextFloat() - 0.5F) / 3.0F, 0.0D, (level.getRandom().nextFloat() - 0.5F) / 3.0F);
            }
        }
    }
}
