package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class Soul extends Projectile {

    @Nullable
    private LivingEntity target;
    private float damage = 3.0F;
    private float speed = 0.4F;

    public Soul(EntityType<? extends Soul> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public Soul(Level level, Player owner, LivingEntity target, Vec3 position, float damage, float speed) {
        this(OFEntities.SOUL.get(), level);
        this.setOwner(owner);
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.setPos(position);
        this.setDeltaMovement(0.0D, 0.1D, 0.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.target != null && this.target.isAlive() && this.target.level() == this.level() && this.getOwner() instanceof LivingEntity owner && this.tickCount < 200) {
                Vec3 direction = this.target.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(this.speed);
                this.setDeltaMovement(this.getDeltaMovement().lerp(direction, 0.15D));
                if (this.getBoundingBox().inflate(0.2D).intersects(this.target.getBoundingBox())) {
                    this.target.hurt(OFDamageTypes.causeSoulDamage(this.level().registryAccess(), this, owner), this.damage);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SOUL_SAND_BREAK, SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(this.level()));
                    this.discard();
                }
            } else {
                this.discard();
            }
        } else {
            Vec3 drift = this.getDeltaMovement().scale(-0.15D);
            for (int i = 0; i < 3; i++) {
                Vec3 trail = this.position().subtract(this.getDeltaMovement().scale(this.random.nextDouble())).add(this.random.nextGaussian() * 0.08D, 0.1875D + this.random.nextGaussian() * 0.08D, this.random.nextGaussian() * 0.08D);
                this.level().addParticle(OFParticleTypes.SMALL_SOUL_FIRE_FLAME.get(), trail.x, trail.y, trail.z, drift.x + this.random.nextGaussian() * 0.02D, drift.y + this.random.nextGaussian() * 0.02D, drift.z + this.random.nextGaussian() * 0.02D);
            }
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
        this.updateRotation();
    }
}
