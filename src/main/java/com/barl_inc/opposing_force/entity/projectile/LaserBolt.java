package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import java.util.Arrays;

public class LaserBolt extends Projectile {

    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(LaserBolt.class, EntityDataSerializers.FLOAT);

    private static final byte HIT_EFFECTS = 3;

    private int pierceCount = 0;

    private final Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;

    public LaserBolt(EntityType<? extends LaserBolt> entityType, Level level) {
        super(entityType, level);
    }

    public LaserBolt(Level level, LivingEntity owner, double x, double y, double z) {
        this(OFEntities.LASER_BOLT.get(), level);
        this.setPos(x, y, z);
        this.setOwner(owner);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DAMAGE, 1.0F);
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }
        this.checkInsideBlocks();

        Vec3 deltaMovement = this.getDeltaMovement();
        double x = this.getX() + deltaMovement.x;
        double y = this.getY() + deltaMovement.y;
        double z = this.getZ() + deltaMovement.z;
        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
        this.setPos(x, y, z);

        this.level().addParticle(OFParticleTypes.LASER_DUST.get(), this.getX(), this.getY() + 0.2F, this.getZ(), 0, 0, 0);
        if (this.tickCount > 160 || this.getBlockY() > this.level().getMaxBuildHeight() + 30) {
            if (!this.level().isClientSide) {
                this.level().broadcastEntityEvent(this, HIT_EFFECTS);
                this.playImpactSound(this.getX(), this.getY(), this.getZ());
                this.discard();
            }
        }
        this.tickTrail();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        DamageSource damageSource = OFDamageTypes.causeLaserBoltDamage(this.level().registryAccess(), this.getOwner());
        if (!this.level().isClientSide) {
            if (entity.hurt(damageSource, this.getDamage())) {
                this.pierceCount++;
                this.setDamage(this.getDamage() * 0.66F);
                this.playImpactSound(entity.getX(), entity.getY(), entity.getZ());
            }
            this.level().broadcastEntityEvent(this, HIT_EFFECTS);
            if (this.pierceCount > 1) {
                this.discard();
            }
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, HIT_EFFECTS);
            this.playImpactSound(pos.getX(), pos.getY(), pos.getZ());
            this.discard();
        }
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    private void playImpactSound(double x, double y, double z) {
        this.level().playSound(null, x, y, z, OFSoundEvents.LASER_BOLT_IMPACT.get(), SoundSource.NEUTRAL, 1.0F, SinewSoundUtils.randomizePitch(this.level()));
    }

    private void tickTrail() {
        Vec3 trailAt = this.position().add(0.0F, this.getBbHeight() / 2.0F, 0.0F);
        if (this.trailPointer == -1) {
            Arrays.fill(this.trailPositions, trailAt);
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 positions = this.trailPositions[j];
        Vec3 subtracted = this.trailPositions[i].subtract(positions);
        return positions.add(subtracted.scale(partialTick));
    }

    public boolean hasTrail() {
        return trailPointer != -1;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == HIT_EFFECTS) {
            this.level().addParticle(OFParticleTypes.LASER_IMPACT.get(), this.getX(), this.getY() + 0.2F, this.getZ(), 0, 0, 0);
            for (int i = 0; i < 3; i++) {
                this.level().addParticle(OFParticleTypes.LASER_DUST.get(), this.getX(), this.getY() + 0.2F, this.getZ(), 0, 0, 0);
            }
        }
    }
}