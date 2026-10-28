package com.barl_inc.opposing_force.entity.misc;

import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Shockwave extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(Shockwave.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(Shockwave.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> BIG = SynchedEntityData.defineId(Shockwave.class, EntityDataSerializers.BOOLEAN);

    private final Set<Entity> hit = new HashSet<>();
    @Nullable
    private LivingEntity owner;
    private float damage;
    private float push;
    private float lift;
    private boolean ignoresPlayers;

    public Shockwave(EntityType<? extends Shockwave> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public static Shockwave spawn(LivingEntity owner, boolean big, float radius, int duration, float damage, float push, float lift) {
        Shockwave shockwave = new Shockwave(OFEntities.SHOCKWAVE.get(), owner.level());
        shockwave.setPos(owner.position());
        shockwave.owner = owner;
        shockwave.entityData.set(BIG, big);
        shockwave.entityData.set(RADIUS, radius);
        shockwave.entityData.set(DURATION, duration);
        shockwave.damage = damage;
        shockwave.push = push;
        shockwave.lift = lift;
        owner.level().addFreshEntity(shockwave);
        return shockwave;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RADIUS, 2.0F);
        builder.define(DURATION, 10);
        builder.define(BIG, false);
    }

    public void exclude(Entity entity) {
        this.hit.add(entity);
    }

    public void setIgnoresPlayers(boolean ignoresPlayers) {
        this.ignoresPlayers = ignoresPlayers;
    }

    @Override
    public void tick() {
        super.tick();
        float radius = this.entityData.get(RADIUS);
        int duration = this.entityData.get(DURATION);
        if (this.level().isClientSide) {
            if (this.tickCount == 1) {
                this.level().addAlwaysVisibleParticle(this.entityData.get(BIG) ? OFParticleTypes.SHOCKWAVE.get() : OFParticleTypes.SMALL_SHOCKWAVE.get(), true, this.getX(), this.getY() + 0.05D, this.getZ(), radius, duration, 0.0D);
            }
        } else {
            double reach = radius * Math.min(this.tickCount, duration) / duration;
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius + 1.0D, 1.5D, radius + 1.0D), entity -> !this.hit.contains(entity) && EntitySelector.NO_SPECTATORS.test(entity) && (this.owner == null || entity.getType() != this.owner.getType()) && !(this.ignoresPlayers && entity instanceof Player))) {
                double distance = entity.position().subtract(this.position()).horizontalDistance();
                double height = entity.getY() - this.getY();
                if (Math.abs(distance - reach) < 0.8D && height > -1.0D && height < 0.5D) {
                    this.hit.add(entity);
                    if (this.damage > 0.0F) {
                        entity.hurt(this.owner == null ? this.damageSources().generic() : this.damageSources().mobAttack(this.owner), this.damage);
                    }
                    Vec3 away = entity.position().subtract(this.position()).multiply(1.0D, 0.0D, 1.0D).normalize();
                    entity.setDeltaMovement(entity.getDeltaMovement().add(away.x * this.push, this.lift, away.z * this.push));
                    entity.hurtMarked = true;
                }
            }
            if (this.tickCount >= duration) {
                this.discard();
            }
        }
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }
}
