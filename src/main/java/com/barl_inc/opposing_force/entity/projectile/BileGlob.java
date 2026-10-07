package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.entity.misc.AcidCloud;
import com.barl_inc.opposing_force.entity.misc.BilePuddle;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BileGlob extends ThrowableItemProjectile {

    public AnimationState projectileAnimationState = new AnimationState();

    public BileGlob(EntityType<? extends BileGlob> entityType, Level level) {
        super(entityType, level);
    }

    public BileGlob(Level level, LivingEntity shooter) {
        super(OFEntities.BILE_GLOB.get(), shooter, level);
    }

    public BileGlob(Level level, double x, double y, double z) {
        super(OFEntities.BILE_GLOB.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TORCH;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(OFParticleTypes.BILE.get(), this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.projectileAnimationState.animateWhen(this.isAlive(), this.tickCount);
            this.level().addParticle(OFParticleTypes.BILE.get(), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (result.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult) result).getEntity())) {
            if (!this.level().isClientSide) {
                double x = this.getX(), y = this.getY(), z = this.getZ();
                if (result instanceof EntityHitResult entityHit) {
                    Entity hit = entityHit.getEntity();
                    x = hit.getX();
                    y = hit.getY();
                    z = hit.getZ();
                }
                BilePuddle bilePuddle = new BilePuddle(this.level(), x, y, z);
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity living) {
                    bilePuddle.setOwner(living);
                }
                bilePuddle.setRadius(1.5F);
                bilePuddle.setDamage(2.0F);
                bilePuddle.setDuration(180);
                this.level().playSound(null, this.blockPosition(), OFSoundEvents.ACID_CHARGE_EXPLODE.get(), SoundSource.NEUTRAL, 1.0F, SinewSoundUtils.randomizePitch(this.level()));
                this.level().broadcastEntityEvent(this, (byte) 3);
                this.level().addFreshEntity(bilePuddle);
                this.discard();
            }
        }
    }
}