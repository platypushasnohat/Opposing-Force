package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.entity.utils.SplitMagicBonus;
import com.platypushasnohat.sinew.item.DisabledItem;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RingProjectile extends Projectile implements SplitMagicBonus {

    private static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.defineId(RingProjectile.class, EntityDataSerializers.INT);

    private int slot;
    private int launchDelay = -1;

    public RingProjectile(EntityType<? extends RingProjectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public RingProjectile(Level level, Player owner, int index, int slot) {
        this(OFEntities.RING_PROJECTILE.get(), level);
        this.setOwner(owner);
        this.entityData.set(INDEX, index);
        this.slot = slot;
        this.setPos(owner.getX(), owner.getY(0.5D), owner.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(INDEX, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Index", this.getIndex());
        compoundTag.putInt("Slot", this.slot);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(INDEX, compoundTag.getInt("Index"));
        this.slot = compoundTag.getInt("Slot");
    }

    public int getIndex() {
        return this.entityData.get(INDEX);
    }

    @Override
    public int getMagicCount() {
        return 5;
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
            if (this.getOwner() instanceof LivingEntity owner && owner.isAlive() && owner.level() == this.level() && this.tickCount < 190) {
                LivingEntity target = this.getTarget(owner);
                if (target == null) {
                    this.launchDelay = -1;
                } else if (this.launchDelay < 0) {
                    this.launchDelay = this.getIndex() * 3;
                } else if (this.launchDelay > 0) {
                    this.launchDelay--;
                }
                if (target != null && this.launchDelay == 0) {
                    Vec3 direction = target.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(0.9D);
                    this.setDeltaMovement(this.getDeltaMovement().lerp(direction, 0.3D));
                    if (this.getBoundingBox().inflate(0.2D).intersects(target.getBoundingBox())) {
                        target.hurt(OFDamageTypes.causeCursedRingDamage(this.level().registryAccess(), this, owner), 3.0F);
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 0.6F, SinewSoundUtils.randomizePitch(this.level()));
                        this.finish();
                    }
                } else {
                    float angle = owner.tickCount * 0.08F + this.getIndex() * Mth.TWO_PI / 5.0F;
                    Vec3 orbit = owner.position().add(Mth.cos(angle) * 1.1F, owner.getBbHeight() * 0.6F + Mth.sin(this.tickCount * 0.15F + this.getIndex()) * 0.1F, Mth.sin(angle) * 1.1F);
                    this.setDeltaMovement(orbit.subtract(this.position()).scale(0.35D));
                }
            } else {
                this.finish();
            }
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
    }

    @Nullable
    private LivingEntity getTarget(LivingEntity owner) {
        LivingEntity target = owner.getLastHurtMob();
        return target != null && target.isAlive() && target != owner && owner.tickCount - owner.getLastHurtMobTimestamp() < 100 ? target : null;
    }

    private void finish() {
        if (this.getOwner() instanceof ServerPlayer player && this.level().getEntitiesOfClass(RingProjectile.class, this.getBoundingBox().inflate(64.0D), ring -> ring != this && ring.getOwner() == player).isEmpty()) {
            DisabledItem.enableItem(player, this.slot);
            player.getCooldowns().addCooldown(OFItems.CURSED_RINGLINE.get(), 60);
        }
        this.discard();
    }
}
