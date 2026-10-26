package com.barl_inc.opposing_force.entity.misc;

import com.barl_inc.opposing_force.entity.Grell;
import com.barl_inc.opposing_force.item.curios.SpectralGogglesItem;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class GrellClone extends Grell {

    private static final EntityDataAccessor<Boolean> REAL = SynchedEntityData.defineId(GrellClone.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Grell owner;
    private int index;
    private int count = 1;

    public GrellClone(EntityType<? extends GrellClone> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        this.moveControl = new MoveControl(this);
        this.setNoGravity(true);
        this.setAnimationState(CONJURE_ANIMATION);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Grell.registerAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(REAL, false);
    }

    public void setup(Grell owner, int index, int count, boolean real) {
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
        this.index = index;
        this.count = count;
        this.entityData.set(REAL, real);
        this.setEnraged(owner.isEnraged());
        if (owner.isElite()) {
            this.makeElite();
        }
    }

    public boolean isReal() {
        return this.entityData.get(REAL);
    }

    @Nullable
    public Grell getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel && serverLevel.getEntity(this.ownerUUID) instanceof Grell grell) {
            this.owner = grell;
        }
        return this.owner;
    }

    public Vec3 getRingPosition(LivingEntity target) {
        Grell owner = this.getOwner();
        float angle = this.index * Mth.TWO_PI / this.count;
        if (owner != null) {
            angle += owner.getIllusionAngle();
        }
        Vec3 center = new Vec3(target.getX(), target.getEyeY() - this.getBbHeight() * 0.5D, target.getZ());
        Vec3 ring = center.add(Mth.cos(angle) * 3.0D, 0.0D, Mth.sin(angle) * 3.0D);
        HitResult hit = this.level().clip(new ClipContext(center, ring, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hit.getType() != HitResult.Type.MISS) {
            ring = hit.getLocation().subtract(ring.subtract(center).normalize().scale(this.getBbWidth() * 0.5D));
        }
        return ring;
    }

    @Override
    protected void tickSpectral() {
        Grell owner = this.getOwner();
        LivingEntity target = owner != null ? owner.getTarget() : null;
        if (owner == null || !owner.isAlive() || !owner.isHidden() || target == null) {
            this.discard();
        } else {
            this.circle(target);
            this.tickParticles();
        }
    }

    private void circle(LivingEntity target) {
        Vec3 ring = this.getRingPosition(target);
        this.setDeltaMovement(ring.subtract(this.position()).scale(0.35D));
        float yaw = (float) (Mth.atan2(target.getZ() - this.getZ(), target.getX() - this.getX()) * Mth.RAD_TO_DEG) - 90.0F;
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
        double gap = target.distanceTo(this);
        if (gap < 2.5D) {
            Vec3 push = target.position().subtract(this.position()).multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.04D);
            target.push(push.x, 0.0D, push.z);
            target.hurtMarked = true;
        }
    }

    private void tickParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            double x = this.getX();
            double y = this.getY(0.5D);
            double z = this.getZ();
            int count = 3;
            if (this.isEnraged()) {
                count = 1;
            }
            for (int i = 0; i < count; i++) {
                if (this.isReal()) {
                    double dx = this.random.nextGaussian() * 1.5D;
                    double dy = this.random.nextGaussian() * 1.5D;
                    double dz = this.random.nextGaussian() * 1.5D;
                    SpectralSight.sendParticlesToPerceivers(serverLevel, this, ParticleTypes.PORTAL, x, y, z, 0, dx, dy, dz, 1.0D);
                } else {
                    double dx = this.random.nextGaussian() * 0.08D;
                    double dz = this.random.nextGaussian() * 0.08D;
                    SpectralSight.sendParticlesToPerceivers(serverLevel, this, ParticleTypes.REVERSE_PORTAL, x, y, z, 0, dx, 0.06D, dz, 1.0D);
                }
            }
        }
    }

    @Override
    public boolean isPerceivedBy(Entity viewer) {
        Grell owner = this.getOwner();
        return owner != null && owner.isRevealedTo(viewer);
    }

    @Override
    protected void enrage() {
    }

    @Override
    protected boolean isInvulnerablePhase() {
        return false;
    }

    @Override
    public float getMaterialize(float partialTicks) {
        Player player = Sinew.PROXY.getClientSidePlayer();
        float alpha = 0.5F;
        if (this.isReal() && player != null && SpectralSight.hasSource(player, SpectralGogglesItem.SPECTRAL_SIGHT)) {
            alpha = 1.0F;
        } else if (this.isReal() && this.isEnraged()) {
            alpha = 0.53F;
        } else if (this.isReal()) {
            alpha = 0.56F;
        }
        return alpha;
    }

    @Override
    public void die(DamageSource source) {
        if (this.isReal()) {
            this.playSound(SoundEvents.GLASS_BREAK, 1.5F, 0.6F);
        } else {
            this.playSound(SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, 1.5F, 1.2F);
        }
        super.die(source);
        Grell owner = this.getOwner();
        if (owner != null && !this.level().isClientSide) {
            owner.onCloneKilled(this, source.getEntity());
        }
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
