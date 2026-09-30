package com.barl_inc.opposing_force.entity.misc;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class AreaDamageCloud extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AreaDamageCloud.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AreaDamageCloud.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(AreaDamageCloud.class, EntityDataSerializers.INT);

    @Nullable
    private LivingEntity owner;

    @Nullable
    private UUID ownerUUID;

    public AreaDamageCloud(EntityType<? extends AreaDamageCloud> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public AreaDamageCloud(EntityType<? extends AreaDamageCloud> entityType, Level level, double x, double y, double z) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RADIUS, 2.0F);
        builder.define(DAMAGE, 2.0F);
        builder.define(DURATION, 300);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (RADIUS.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setRadius(compoundTag.getFloat("Radius"));
        this.setDamage(compoundTag.getFloat("Damage"));
        this.setDuration(compoundTag.getInt("Duration"));
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("Radius", this.getRadius());
        compoundTag.putFloat("Damage", this.getDamage());
        compoundTag.putInt("Duration", this.getDuration());
    }

    public float getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(RADIUS, Mth.clamp(radius, 0.0F, 32.0F));
    }

    public float getDamage() {
        return this.getEntityData().get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.getEntityData().set(DAMAGE, damage);
    }

    public int getDuration() {
        return this.getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        this.getEntityData().set(DURATION, duration);
    }

    public DamageSource getDamageSource() {
        return this.damageSources().generic();
    }

    public ParticleOptions getParticle() {
        return ParticleTypes.CLOUD;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity living) {
                this.owner = living;
            }
        }
        return this.owner;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    @Override
    public void refreshDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPos(x, y, z);
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void tick() {
        super.tick();
        float radius = this.getRadius();
        if (this.level().isClientSide) {
            int i = Mth.ceil((float) Math.PI * radius * radius);
            ParticleOptions particle = this.getParticle();
            for (int j = 0; j < i; j++) {
                float pi = this.getRandom().nextFloat() * (float) (Math.PI * 2.0F);
                float sqrt = Mth.sqrt(this.getRandom().nextFloat()) * radius;
                double x = this.getX() + (double) (Mth.cos(pi) * sqrt);
                double y = this.getY();
                double z = this.getZ() + (double) (Mth.sin(pi) * sqrt);
                this.level().addAlwaysVisibleParticle(particle, x, y, z, (0.5D - this.getRandom().nextDouble()) * 0.15D, 0.01F, (0.5D - this.getRandom().nextDouble()) * 0.15D);
            }
        } else {
            if (this.tickCount >= this.getDuration()) {
                this.discard();
                return;
            }
            if (this.tickCount % 10 == 0) {
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                if (!entities.isEmpty()) {
                    for (LivingEntity entity : entities) {
                        entity.hurt(this.getDamageSource(), this.getDamage());
                    }
                }
            }
        }
    }
}
