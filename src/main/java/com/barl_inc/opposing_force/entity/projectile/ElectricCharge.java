package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ElectricCharge extends ThrowableProjectile {

    private static final EntityDataAccessor<Boolean> DISCHARGING = SynchedEntityData.defineId(ElectricCharge.class, EntityDataSerializers.BOOLEAN);

    public static final int DISCHARGE_DURATION = 8;
    public static final float IMPACT_RADIUS = 3.0F;
    public static final float WATER_RADIUS = 4.0F;
    public static final int MAX_LIFETIME = 140;

    private int dischargeTicks;

    public List<List<Vec3>> arcs = List.of();
    public int lastArcTick = -1;

    public ElectricCharge(EntityType<? extends ElectricCharge> entityType, Level level) {
        super(entityType, level);
    }

    public ElectricCharge(Level level, LivingEntity shooter) {
        super(OFEntities.ELECTRIC_CHARGE.get(), shooter, level);
    }

    public ElectricCharge(Level level, double x, double y, double z) {
        super(OFEntities.ELECTRIC_CHARGE.get(), x, y, z, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DISCHARGING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Discharging", this.isDischarging());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setDischarging(compoundTag.getBoolean("Discharging"));
    }

    public boolean isDischarging() {
        return this.entityData.get(DISCHARGING);
    }

    public void setDischarging(boolean discharging) {
        this.entityData.set(DISCHARGING, discharging);
    }

    public int getDischargeTicks() {
        return this.dischargeTicks;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Override
    public void tick() {
        if (this.isDischarging()) {
            this.tickDischarge();
        } else {
            this.tickFlight();
        }
    }

    private void tickDischarge() {
        this.dischargeTicks++;
        this.setDeltaMovement(Vec3.ZERO);
        if (!this.level().isClientSide && this.dischargeTicks >= DISCHARGE_DURATION) {
            this.discard();
        }
    }

    private void tickFlight() {
        super.tick();

        if (!this.isRemoved() && !this.isDischarging()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(1.0F / 0.99F));

            if (this.level().isClientSide) {
                OpposingForce.PROXY.playSound(this, (byte) 5);
            } else if (this.level().getFluidState(this.blockPosition()).is(FluidTags.WATER)) {
                this.discharge(WATER_RADIUS);
            } else if (this.tickCount >= MAX_LIFETIME || this.getY() > this.level().getMaxBuildHeight() + 30) {
                this.discharge(IMPACT_RADIUS);
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return !this.isDischarging() && super.canHitEntity(target);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide && !this.isDischarging()) {
            if (result.getType() == HitResult.Type.BLOCK) {
                Vec3 location = result.getLocation();
                this.setPos(location.x, location.y - this.getBbHeight() / 2.0F, location.z);
            }
            this.discharge(IMPACT_RADIUS);
        }
    }

    public void discharge(float radius) {
        this.setDischarging(true);
        this.setDeltaMovement(Vec3.ZERO);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), OFSoundEvents.ELECTRIC_CHARGE_DISSIPATE.get(), SoundSource.NEUTRAL, 3.0F, (1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F) * 0.7F);
        this.shock(radius);
    }

    private void shock(float radius) {
        Vec3 center = this.getBoundingBox().getCenter();
        double reach = radius * 2.0D;
        Entity owner = this.getOwner();

        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(reach))) {
            double distance = Math.sqrt(living.distanceToSqr(center)) / reach;
            Vec3 push = new Vec3(living.getX() - center.x, living.getEyeY() - center.y, living.getZ() - center.z);
            if (!living.isSpectator() && living.isAlive() && distance <= 1.0D && push.lengthSqr() > 0.0D) {
                this.shockEntity(living, center, push, distance, reach, owner);
            }
        }
    }

    private void shockEntity(LivingEntity living, Vec3 center, Vec3 push, double distance, double reach, Entity owner) {
        double impact = (1.0D - distance) * Explosion.getSeenPercent(center, living);
        float damage = (float) ((int) ((impact * impact + impact) / 2.0D * 5.0D * reach + 1.0D));
        living.hurt(OFDamageTypes.causeElectricDamage(this.level().registryAccess(), this, owner), damage);

        double knockback = impact * (1.0D - living.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
        living.setDeltaMovement(living.getDeltaMovement().add(push.normalize().scale(knockback)));
        living.hurtMarked = true;

        living.addEffect(new MobEffectInstance(OFMobEffects.ELECTRIFIED, 300, 0), owner);
    }

    @Override
    public void remove(RemovalReason reason) {
        OpposingForce.PROXY.clearSoundCacheFor(this);
        super.remove(reason);
    }
}
