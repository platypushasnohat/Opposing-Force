package com.barl_inc.opposing_force.entity.misc;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.Dicer;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DicerLaser extends Entity {

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(DicerLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(DicerLaser.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(DicerLaser.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(DicerLaser.class, EntityDataSerializers.INT);

    public static final double RADIUS = 30;
    public static final float DAMAGE = 4.0F;

    public LivingEntity caster;
    public double endPosX;
    public double endPosY;
    public double endPosZ;
    public double collidePosX;
    public double collidePosY;
    public double collidePosZ;
    public double prevCollidePosX;
    public double prevCollidePosY;
    public double prevCollidePosZ;
    public float prevYaw;
    public float renderYaw;
    public float prevPitch;
    public float renderPitch;
    public int timer;
    public boolean active = true;
    public Direction blockSide = null;

    public DicerLaser(EntityType<? extends DicerLaser> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public DicerLaser(EntityType<? extends DicerLaser> entityType, Level level, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(entityType, level);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos();
        if (!level.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevCollidePosX = this.collidePosX;
        this.prevCollidePosY = this.collidePosY;
        this.prevCollidePosZ = this.collidePosZ;
        this.prevYaw = this.renderYaw;
        this.prevPitch = this.renderPitch;

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (this.tickCount == 1 && this.level().isClientSide) {
            this.caster = (LivingEntity) this.level().getEntity(this.getCasterID());
        }

        if (!this.level().isClientSide) {
            if (this.caster instanceof Dicer) {
                this.updateWithDicer();
            }
        }

        if (this.caster != null) {
            this.renderYaw = (this.caster.yHeadRot + 90.0F) * Mth.DEG_TO_RAD;
            this.renderPitch = -this.caster.getXRot() * Mth.DEG_TO_RAD;
        }

        if (!this.active && this.timer == 0) {
            this.discard();
        }
        if (this.active && this.tickCount > 3) {
            if (this.timer < 3) {
                this.timer++;
            }
        }
        else {
            if (this.timer > 0) {
                this.timer--;
            }
        }


        if (this.caster != null && !this.caster.isAlive()) {
            this.discard();
        }

        if (this.tickCount > 3) {
            OpposingForce.PROXY.playSound(this, (byte) 0);
            this.calculateEndPos();
            List<Entity> hit = this.raytraceEntities(this.level(), new Vec3(this.getX(), this.getY(), this.getZ()), new Vec3(this.endPosX, this.endPosY, this.endPosZ)).entities;
            if (this.blockSide != null) {
                this.spawnExplosionParticles();
            }
            if (!this.level().isClientSide) {
                for (Entity target : hit) {
                    if (!(target instanceof Mob) && !(target instanceof Player)) {
                        continue;
                    }
                    target.hurt(OFDamageTypes.causeLaserDamage(this.level().registryAccess(), this.caster), DAMAGE);
                }
            }
        }
        if (this.tickCount - 3 > this.getDuration()) {
            this.active = false;
        }
    }

    private void spawnExplosionParticles() {
        float velocity = 0.1F;
        float yaw = (float) (this.getRandom().nextFloat() * 2 * Math.PI);
        float motionY = this.getRandom().nextFloat() * 0.08F;
        float motionX = velocity * Mth.cos(yaw);
        float motionZ = velocity * Mth.sin(yaw);
        this.level().addParticle(OFParticleTypes.LASER_DUST.get(), this.collidePosX, this.collidePosY + 0.1F, this.collidePosZ, motionX, motionY, motionZ);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(YAW, 0.0F);
        builder.define(PITCH, 0.0F);
        builder.define(DURATION, 0);
        builder.define(CASTER, -1);
    }

    public float getYaw() {
        return getEntityData().get(YAW);
    }

    public void setYaw(float yaw) {
        getEntityData().set(YAW, yaw);
    }

    public float getPitch() {
        return getEntityData().get(PITCH);
    }

    public void setPitch(float pitch) {
        getEntityData().set(PITCH, pitch);
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }

    public int getCasterID() {
        return getEntityData().get(CASTER);
    }

    public void setCasterID(int id) {
        getEntityData().set(CASTER, id);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    private void calculateEndPos() {
        double radius = RADIUS;
        if (this.level().isClientSide) {
            this.endPosX = this.getX() + radius * Math.cos(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosZ = this.getZ() + radius * Math.sin(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosY = this.getY() + radius * Math.sin(this.renderPitch);
        }
        else {
            this.endPosX = this.getX() + radius * Math.cos(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosZ = this.getZ() + radius * Math.sin(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosY = this.getY() + radius * Math.sin(this.getPitch());
        }
    }

    public LaserHitResult raytraceEntities(Level world, Vec3 from, Vec3 to) {
        LaserHitResult result = new LaserHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            this.collidePosX = hitVec.x;
            this.collidePosY = hitVec.y;
            this.collidePosZ = hitVec.z;
            this.blockSide = result.blockHit.getDirection();
        } else {
            this.collidePosX = this.endPosX;
            this.collidePosY = this.endPosY;
            this.collidePosZ = this.endPosZ;
            this.blockSide = null;
        }
        List<Entity> entities = world.getEntitiesOfClass(Entity.class, new AABB(Math.min(getX(), this.collidePosX), Math.min(getY(), this.collidePosY), Math.min(getZ(), this.collidePosZ), Math.max(getX(), this.collidePosX), Math.max(getY(), this.collidePosY), Math.max(getZ(), this.collidePosZ)).inflate(1, 1, 1));
        for (Entity entity : entities) {
            if (entity == this.caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.25F;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    @Override
    public void remove(RemovalReason reason) {
        OpposingForce.PROXY.clearSoundCacheFor(this);
        super.remove(reason);
    }

    private void updateWithDicer() {
        this.setYaw((this.caster.yHeadRot + 90.0F) * Mth.DEG_TO_RAD);
        this.setPitch(-this.caster.getXRot() * Mth.DEG_TO_RAD);
        Vec3 vecOffset1 = new Vec3(0.0D, 0.0D, 0.3D).yRot((float) Math.toRadians(-this.caster.getYRot()));
        Vec3 vecOffset2 = new Vec3(0.6D, 0.0D, 0.0D).yRot(-this.getYaw()).xRot(this.getPitch());
        this.setPos(this.caster.getX() + vecOffset1.x + vecOffset2.x, this.caster.getEyeY(), this.caster.getZ() + vecOffset1.z + vecOffset2.z);
    }

    public static class LaserHitResult {

        private BlockHitResult blockHit;

        private final List<Entity> entities = new ArrayList<>();

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
                this.blockHit = (BlockHitResult) rayTraceResult;
            }
        }

        public void addEntityHit(Entity entity) {
            this.entities.add(entity);
        }
    }
}