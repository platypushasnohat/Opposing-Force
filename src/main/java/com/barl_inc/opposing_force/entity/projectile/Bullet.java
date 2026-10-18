package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.entity.utils.SplitProjectileBonus;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Bullet extends ThrowableItemProjectile implements SplitProjectileBonus {

    private float damage = 1.0F;
    private int lifespan = 40;
    private int projectileCount = 1;

    public Bullet(EntityType<? extends Bullet> entityType, Level level) {
        super(entityType, level);
    }

    public Bullet(Level level, LivingEntity owner, ItemStack stack) {
        this(OFEntities.BULLET.get(), level, owner, stack);
    }

    protected Bullet(EntityType<? extends Bullet> entityType, Level level, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, level);
        this.setItem(stack);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public void setProjectileCount(int projectileCount) {
        this.projectileCount = projectileCount;
    }

    @Override
    public int getProjectileCount() {
        return this.projectileCount;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.tickCount >= 2 && this.tickCount % 3 == 0) {
            this.level().addAlwaysVisibleParticle(OFParticleTypes.BULLET_TRAIL.get(), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
        }
        if (!this.level().isClientSide && this.tickCount > this.lifespan) {
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return OFItems.IRON_BULLET.get();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.02D;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.projectileCount > 1) {
            result.getEntity().invulnerableTime = 0;
        }
        result.getEntity().hurt(this.getDamageSource(), this.damage);
    }

    protected DamageSource getDamageSource() {
        return OFDamageTypes.causeBulletDamage(this.level().registryAccess(), this, this.getOwner());
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
            for (int i = 0; i < 6; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), (this.random.nextDouble() - 0.5D) * 0.1D, this.random.nextDouble() * 0.1D, (this.random.nextDouble() - 0.5D) * 0.1D);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Damage", this.damage);
        compound.putInt("Lifespan", this.lifespan);
        compound.putInt("ProjectileCount", this.projectileCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damage = compound.getFloat("Damage");
        this.lifespan = compound.getInt("Lifespan");
        this.projectileCount = Math.max(1, compound.getInt("ProjectileCount"));
    }
}
