package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Sawblade extends AbstractArrow implements ItemSupplier {

    public Sawblade(EntityType<? extends Sawblade> type, Level level) {
        super(type, level);
    }

    public Sawblade(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(OFEntities.SAWBLADE.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    public Sawblade(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(OFEntities.SAWBLADE.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(OFItems.SAWBLADE.get());
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(OFItems.SAWBLADE.get());
    }
}
