package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.entity.misc.AcidCloud;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class AcidCharge extends ThrowableItemProjectile {

    public AnimationState projectileAnimationState = new AnimationState();

    public AcidCharge(EntityType<? extends AcidCharge> entityType, Level level) {
        super(entityType, level);
    }

    public AcidCharge(Level level, LivingEntity shooter) {
        super(OFEntities.ACID_CHARGE.get(), shooter, level);
    }

    public AcidCharge(Level level, double x, double y, double z) {
        super(OFEntities.ACID_CHARGE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return OFItems.ACID_CHARGE.get();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(OFParticleTypes.ACID.get(), this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.projectileAnimationState.animateWhen(this.isAlive(), this.tickCount);
            this.level().addParticle(OFParticleTypes.ACID.get(), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (result.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult) result).getEntity())) {
            if (!this.level().isClientSide) {
                AcidCloud acidCloud = new AcidCloud(this.level(), this.getX(), this.getY(), this.getZ());
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity living) {
                    acidCloud.setOwner(living);
                }
                acidCloud.setRadius(2.5F);
                acidCloud.setDamage(2.0F);
                acidCloud.setDuration(300);
                this.level().playSound(null, this.blockPosition(), OFSoundEvents.ACID_CHARGE_EXPLODE.get(), SoundSource.NEUTRAL, 1.0F, SinewSoundUtils.randomizePitch(this.level()));
                this.level().broadcastEntityEvent(this, (byte) 3);
                this.level().addFreshEntity(acidCloud);
                this.discard();
            }
        }
    }
}