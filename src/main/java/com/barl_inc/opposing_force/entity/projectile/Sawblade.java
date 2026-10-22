package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class Sawblade extends Entity implements ItemSupplier {

    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(Sawblade.class, EntityDataSerializers.INT);

    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    private int prevDespawnTime;
    private int despawnTime = -1;

    private int hitCooldown = 0;

    public Sawblade(EntityType<? extends Sawblade> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIFESPAN, 200);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
        }
        this.setLifespan(compoundTag.getInt("Lifespan"));
        if (compoundTag.contains("DespawnTime")) {
            this.despawnTime = compoundTag.getInt("DespawnTime");
        }
        if (compoundTag.contains("HitCooldown")) {
            this.hitCooldown = compoundTag.getInt("HitCooldown");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
        compoundTag.putInt("Lifespan", this.getLifespan());
        compoundTag.putInt("DespawnTime", this.despawnTime);
        compoundTag.putInt("HitCooldown", this.hitCooldown);
    }

    public void setOwner(@Nullable LivingEntity living) {
        this.owner = living;
        this.ownerUUID = living == null ? null : living.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }
        return this.owner;
    }

    public void setLifespan(int lifespan) {
        this.entityData.set(LIFESPAN, lifespan);
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public float getDespawnTime(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevDespawnTime, this.despawnTime);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(OFItems.SAWBLADE.get());
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return !this.isInFluidType() && !this.isSpectator() && !this.isCrouching() && this.isAlive();
    }

    private void hurtEntities() {
        AABB bashBox = this.getBoundingBox();
        DamageSource source = OFDamageTypes.causeSawDamage(this.level().registryAccess(), this.getOwner());
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, bashBox)) {
            if (this.hitCooldown <= 0 && !this.isAlliedTo(entity) && (this.getOwner() != null && !entity.is(this.getOwner()) && !entity.isAlliedTo(this.getOwner()))) {
                if (entity.hurt(source, 4.0F)) {
                    this.hitCooldown = 5;
                    entity.knockback(0.1F, this.getX() - entity.getX(), this.getZ() - entity.getZ());
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.despawnTime == -1) {
            this.despawnTime = this.getLifespan();
        }
        this.prevDespawnTime = this.despawnTime;
        if (this.despawnTime > 0) {
            this.despawnTime--;
        } else if (!this.level().isClientSide) {
            this.discard();
        }

        Vec3 movement = new Vec3(0, 0, -0.01F * 4.0F).yRot((float) -Math.toRadians(this.getYRot()));
        this.setDeltaMovement(this.getDeltaMovement().add(movement));
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9F).add(0.0D, -0.08D, 0.0D));
        }
        if (this.horizontalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.3F, 0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.hitCooldown > 0) {
            this.hitCooldown--;
        }
        this.hurtEntities();
    }
}
