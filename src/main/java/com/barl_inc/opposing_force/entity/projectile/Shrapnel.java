package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.platypushasnohat.sinew.entity.utils.SplitProjectileBonus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class Shrapnel extends Bullet implements SplitProjectileBonus {

    private int projectileCount = 1;

    public Shrapnel(EntityType<? extends Shrapnel> entityType, Level level) {
        super(entityType, level);
    }

    public Shrapnel(Level level, LivingEntity owner, ItemStack stack) {
        super(OFEntities.SHRAPNEL.get(), level, owner, stack);
    }

    public void setProjectileCount(int projectileCount) {
        this.projectileCount = projectileCount;
    }

    @Override
    public int getProjectileCount() {
        return this.projectileCount;
    }

    @Override
    protected DamageSource getDamageSource() {
        return OFDamageTypes.causeShrapnelDamage(this.level().registryAccess(), this, this.getOwner());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        result.getEntity().invulnerableTime = 0;
        super.onHitEntity(result);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ProjectileCount", this.projectileCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.projectileCount = compound.getInt("ProjectileCount");
    }
}
