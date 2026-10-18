package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Shrapnel extends Bullet {

    public Shrapnel(EntityType<? extends Shrapnel> entityType, Level level) {
        super(entityType, level);
    }

    public Shrapnel(Level level, LivingEntity owner, ItemStack stack) {
        super(OFEntities.SHRAPNEL.get(), level, owner, stack);
    }

    @Override
    protected DamageSource getDamageSource() {
        return OFDamageTypes.causeShrapnelDamage(this.level().registryAccess(), this, this.getOwner());
    }
}
