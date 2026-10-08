package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.entity.misc.BilePuddle;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class BileBombArrow extends AbstractArrow {
    public BileBombArrow(EntityType<? extends BileBombArrow> entityType, Level level) {
        super(entityType, level);
    }

    public BileBombArrow(Level level, LivingEntity shooter, ItemStack pickup, @Nullable ItemStack weapon) {
        super(OFEntities.BILE_BOMB_ARROW.get(), shooter, level, pickup, weapon);
    }

    public BileBombArrow(Level level, double x, double y, double z, ItemStack pickup, @Nullable ItemStack weapon) {
        super(OFEntities.BILE_BOMB_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(OFItems.BILE_BOMB_ARROW.get());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(OFParticleTypes.BILE.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hit = result.getEntity();
        double x = hit.getX(), y = hit.getY(), z = hit.getZ();
        super.onHitEntity(result);
        if (hit.getType() == EntityType.ENDERMAN) {
            return;
        }
        this.burst(x, y, z);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.burst(this.getX(), this.getY(), this.getZ());
    }

    private void burst(double x, double y, double z) {
        if (this.level().isClientSide) {
            return;
        }
        BilePuddle bilePuddle = new BilePuddle(this.level(), x, y, z);
        if (this.getOwner() instanceof LivingEntity living) {
            bilePuddle.setOwner(living);
        }
        bilePuddle.setRadius(1.5F);
        bilePuddle.setDamage(2.0F);
        bilePuddle.setDuration(180);
        this.level().playSound(null, this.blockPosition(), OFSoundEvents.ACID_CHARGE_EXPLODE.get(), SoundSource.NEUTRAL, 1.0F, SinewSoundUtils.randomizePitch(this.level()));
        this.level().addFreshEntity(bilePuddle);
        this.discard();
    }
}
