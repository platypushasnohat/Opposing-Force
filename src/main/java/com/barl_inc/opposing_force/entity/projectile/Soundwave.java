package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.entity.Pipsqueak;
import com.barl_inc.opposing_force.registry.OFEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Soundwave extends Projectile {

    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(Soundwave.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FADING = SynchedEntityData.defineId(Soundwave.class, EntityDataSerializers.BOOLEAN);

    private final List<Vec3> trail = new ArrayList<>();
    private final Set<UUID> hitEntities = new HashSet<>();
    private int phase;
    private float targetSize = 1.6F;
    private float shrinkSpeed = 0.04F;
    private boolean shrinkPending;
    private double distanceSinceBounce;
    private int fadeTicks;

    public Soundwave(EntityType<? extends Soundwave> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public Soundwave(Level level, LivingEntity owner, Vec3 pos, Vec3 direction) {
        this(OFEntities.SOUNDWAVE.get(), level);
        this.setOwner(owner);
        this.setPos(pos);
        this.setDeltaMovement(direction.normalize().scale(this.getSpeed()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIZE, 1.6F);
        builder.define(FADING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Phase", this.phase);
        compoundTag.putFloat("Size", this.getSize());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.phase = compoundTag.getInt("Phase");
        this.entityData.set(SIZE, compoundTag.getFloat("Size"));
        this.targetSize = this.getSize();
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    public boolean isFading() {
        return this.entityData.get(FADING);
    }

    public float getAlpha(float partialTicks) {
        float alpha = 1.0F;
        if (this.isFading()) {
            alpha = Mth.clamp(1.0F - (this.fadeTicks + partialTicks) / 10.0F, 0.0F, 1.0F);
        }
        return alpha;
    }

    private float getStageValue(float... values) {
        return values[Mth.clamp(this.phase, 0, values.length - 1)];
    }

    private float getSpeed() {
        return this.getStageValue(0.45F, 0.6F, 0.75F, 0.9F, 1.05F, 1.2F);
    }

    private float getStageSize() {
        return this.getStageValue(1.6F, 1.35F, 1.1F, 0.85F, 0.6F, 0.35F);
    }

    private float getDamage() {
        return this.getStageValue(6.0F, 4.0F, 4.0F, 2.0F, 2.0F, 1.0F);
    }

    private double getSpacing() {
        return 0.225D * this.getSize();
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
        if (this.isFading()) {
            this.fadeTicks++;
            if (!this.level().isClientSide && this.fadeTicks >= 10) {
                this.discard();
            }
        } else {
            this.trail.addFirst(this.position());
            if (this.trail.size() > 30) {
                this.trail.removeLast();
            }
            this.move();
            if (!this.level().isClientSide) {
                this.hitEntities();
                this.updateSize();
                if (this.tickCount > 300) {
                    this.entityData.set(FADING, true);
                }
            }
        }
    }

    private void move() {
        Vec3 start = this.position();
        Vec3 velocity = this.getDeltaMovement();
        BlockHitResult hitResult = this.level().clip(new ClipContext(start, start.add(velocity), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            Vec3 normal = Vec3.atLowerCornerOf(hitResult.getDirection().getNormal());
            this.setPos(hitResult.getLocation().add(normal.scale(0.05D)));
            if (!this.level().isClientSide) {
                this.bounce(velocity, normal);
            }
        } else {
            this.setPos(start.add(velocity));
        }
        this.distanceSinceBounce += velocity.length();
    }

    private void bounce(Vec3 velocity, Vec3 normal) {
        if (this.shrinkPending || this.getSize() > this.getStageSize()) {
            this.targetSize = this.getStageSize();
            this.shrinkSpeed = 0.2F;
        }
        this.phase++;
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_AMBIENT, SoundSource.HOSTILE, 0.8F * (float) Math.pow(0.7D, this.phase - 1), 1.2F);
        if (this.phase >= 6) {
            this.setDeltaMovement(Vec3.ZERO);
            this.entityData.set(FADING, true);
        } else {
            Vec3 reflected = velocity.subtract(normal.scale(2.0D * velocity.dot(normal)));
            Vec3 twisted = this.twist(reflected, 15.0F, 40.0F);
            if (twisted.dot(normal) <= 0.0D) {
                twisted = reflected;
            }
            this.setDeltaMovement(twisted.normalize().scale(this.getSpeed()));
            this.hitEntities.clear();
            this.shrinkPending = true;
            this.distanceSinceBounce = 0.0D;
        }
    }

    private Vec3 twist(Vec3 direction, float minAngle, float maxAngle) {
        Vec3 normal = direction.normalize();
        Vec3 side = normal.cross(new Vec3(this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian())).normalize();
        float angle = Mth.nextFloat(this.random, minAngle, maxAngle) * Mth.DEG_TO_RAD;
        return normal.scale(Mth.cos(angle)).add(side.scale(Mth.sin(angle))).scale(direction.length());
    }

    private void updateSize() {
        if (this.shrinkPending && this.distanceSinceBounce >= this.getSpacing() * 2.0D) {
            this.shrinkPending = false;
            this.targetSize = this.getStageSize();
        }
        float size = this.getSize();
        if (size > this.targetSize) {
            size = Math.max(this.targetSize, size - this.shrinkSpeed);
            this.entityData.set(SIZE, size);
        } else {
            this.shrinkSpeed = 0.04F;
        }
    }

    private void hitEntities() {
        Entity owner = this.getOwner();
        LivingEntity attacker = null;
        if (owner instanceof LivingEntity livingOwner) {
            attacker = livingOwner;
        }
        DamageSource source = this.damageSources().mobProjectile(this, attacker);
        for (Vec3 point : this.getSegmentPoints(this.position())) {
            AABB box = new AABB(point, point).inflate(0.4D * this.getSize());
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, box, entity -> entity.isAlive() && entity != owner && !(entity instanceof Pipsqueak) && !this.hitEntities.contains(entity.getUUID()))) {
                this.hitEntities.add(entity.getUUID());
                entity.hurt(source, this.getDamage());
                if (entity instanceof Creeper creeper) {
                    creeper.ignite();
                }
            }
        }
    }

    public List<Vec3> getSegmentPoints(Vec3 head) {
        List<Vec3> points = new ArrayList<>();
        points.add(head);
        double spacing = this.getSpacing();
        double needed = spacing;
        Vec3 previous = head;
        int index = 0;
        while (points.size() < 3 && index < this.trail.size()) {
            Vec3 next = this.trail.get(index);
            double length = previous.distanceTo(next);
            if (length >= needed) {
                previous = previous.add(next.subtract(previous).scale(needed / length));
                points.add(previous);
                needed = spacing;
            } else {
                needed -= length;
                previous = next;
                index++;
            }
        }
        Vec3 back = this.getDeltaMovement().normalize().scale(-needed);
        while (points.size() < 3) {
            previous = previous.add(back);
            points.add(previous);
            back = back.normalize().scale(spacing);
        }
        return points;
    }
}
