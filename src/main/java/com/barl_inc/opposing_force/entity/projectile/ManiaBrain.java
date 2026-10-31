package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.entity.utils.SplitMagicBonus;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManiaBrain extends Projectile implements SplitMagicBonus {

    public static final float DAMAGE = 2.0F;

    private static final EntityDataAccessor<Boolean> ELITE = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TARGETING = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COUNT = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SWAPS = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.INT);

    private ItemStack weapon = ItemStack.EMPTY;
    private int index;
    private int maniaAmplifier;
    private float speed = 1.0F;
    private int targetingTicks;
    private int spentTicks;
    @Nullable
    private LivingEntity target;
    private final Set<Integer> hitEntities = new HashSet<>();

    public int glitchTicks;
    public long glitchSeed;
    public int snapTicks;
    public float facingYaw;
    public float prevFacingYaw;
    public float facingPitch;
    public float prevFacingPitch;

    public ManiaBrain(EntityType<? extends ManiaBrain> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public ManiaBrain(Level level, Player owner, ItemStack weapon, int index, int count, boolean elite, int maniaAmplifier) {
        this(OFEntities.MANIA_BRAIN.get(), level);
        this.setOwner(owner);
        this.weapon = weapon.copy();
        this.index = index;
        this.entityData.set(COUNT, count);
        this.maniaAmplifier = maniaAmplifier;
        this.speed = 0.8F + this.random.nextFloat() * 0.4F;
        this.entityData.set(ELITE, elite);
        Vec3 look = owner.getLookAngle();
        this.setPos(owner.getEyePosition().add(look.scale(0.6D)).subtract(0.0D, 0.15D, 0.0D));
        this.setDeltaMovement(look.scale(1.2D * this.speed));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ELITE, false);
        builder.define(TARGETING, false);
        builder.define(COUNT, 6);
        builder.define(SWAPS, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (SWAPS.equals(key)) {
            this.snapTicks = 2;
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Elite", this.isElite());
        compoundTag.putInt("Index", this.index);
        compoundTag.putInt("Count", this.getCount());
        compoundTag.putInt("ManiaAmplifier", this.maniaAmplifier);
        compoundTag.putFloat("Speed", this.speed);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(ELITE, compoundTag.getBoolean("Elite"));
        this.index = compoundTag.getInt("Index");
        this.entityData.set(COUNT, Math.max(1, compoundTag.getInt("Count")));
        this.maniaAmplifier = compoundTag.getInt("ManiaAmplifier");
        this.speed = Math.max(0.1F, compoundTag.getFloat("Speed"));
    }

    public boolean isElite() {
        return this.entityData.get(ELITE);
    }

    public int getCount() {
        return this.entityData.get(COUNT);
    }

    public float getBrainScale() {
        return 0.8F / (1.0F + (this.getCount() - 6) * 0.04F);
    }

    public boolean isTargeting() {
        return this.entityData.get(TARGETING);
    }

    @Override
    public int getMagicCount() {
        return this.getCount();
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
        if (this.level().isClientSide) {
            this.tickClient();
        } else if (this.getOwner() instanceof LivingEntity owner && owner.isAlive() && owner.level() == this.level() && this.distanceToSqr(owner) < 48.0D * 48.0D) {
            this.tickServer(owner);
        } else {
            this.discard();
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
    }

    private void tickServer(LivingEntity owner) {
        if (this.spentTicks > 0) {
            this.checkHits();
            if (++this.spentTicks > 10) {
                this.discard();
            }
        } else {
            if (this.target != null && !this.isValidTarget(owner, this.target)) {
                this.target = null;
            }
            if (this.target == null && this.tickCount >= 4) {
                this.target = this.findTarget(owner);
            }
            this.entityData.set(TARGETING, this.target != null);
            if (this.target != null) {
                Vec3 direction = this.target.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(1.8D * this.speed);
                this.setDeltaMovement(this.getDeltaMovement().lerp(direction, 0.4D));
                this.checkHits();
                if (++this.targetingTicks > 60) {
                    this.discard();
                }
            } else {
                this.targetingTicks = 0;
                Vec3 offset = this.getSlot(owner).subtract(this.position()).scale(0.35D * this.speed);
                if (offset.length() > 1.2D * this.speed) {
                    offset = offset.normalize().scale(1.2D * this.speed);
                }
                this.setDeltaMovement(offset);
                if (this.tickCount > 300) {
                    this.discard();
                }
            }
        }
        if (!this.isRemoved() && this.random.nextFloat() < (this.isTargeting() ? 0.03F : 0.01F)) {
            this.swap(owner);
        }
    }

    private void tickClient() {
        if (this.snapTicks > 0) {
            this.snapTicks--;
        }
        if (this.glitchTicks > 0) {
            this.glitchTicks--;
        } else if (this.random.nextFloat() < (this.isTargeting() ? 0.15F : 0.04F)) {
            this.glitchTicks = 2 + this.random.nextInt(3);
            this.glitchSeed = this.random.nextLong();
        }
        Vec3 motion = this.getDeltaMovement();
        Vec3 facing = motion;
        if (motion.lengthSqr() < 0.01D && this.getOwner() != null) {
            facing = this.getOwner().getLookAngle();
        }
        this.prevFacingYaw = this.facingYaw;
        this.prevFacingPitch = this.facingPitch;
        this.facingYaw = Mth.rotLerp(0.4F, this.facingYaw, (float) (Mth.atan2(facing.x, facing.z) * Mth.RAD_TO_DEG));
        this.facingPitch = Mth.lerp(0.4F, this.facingPitch, (float) (-Mth.atan2(facing.y, facing.horizontalDistance()) * Mth.RAD_TO_DEG));
        if (motion.lengthSqr() > 0.04D) {
            ParticleOptions sparkle = this.isElite() ? OFParticleTypes.ELITE_MANIA_SPARKLE.get() : OFParticleTypes.MANIA_SPARKLE.get();
            Vec3 drift = motion.scale(-0.1D);
            for (int i = 0; i < 2; i++) {
                Vec3 trail = this.position().subtract(motion.scale(this.random.nextDouble())).add(this.random.nextGaussian() * 0.08D, this.getBbHeight() * 0.5D + this.random.nextGaussian() * 0.08D, this.random.nextGaussian() * 0.08D);
                this.level().addParticle(sparkle, trail.x, trail.y, trail.z, drift.x + this.random.nextGaussian() * 0.02D, drift.y + this.random.nextGaussian() * 0.02D, drift.z + this.random.nextGaussian() * 0.02D);
            }
        }
    }

    private Vec3 getSlot(LivingEntity owner) {
        float yaw = owner.getYRot() * Mth.DEG_TO_RAD;
        Vec3 right = new Vec3(-Mth.cos(yaw), 0.0D, -Mth.sin(yaw));
        Vec3 forward = new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
        int side = 1;
        if (this.index % 2 == 1) {
            side = -1;
        }
        int perSide = (this.getCount() + 1) / 2;
        float angle = ((this.index / 2 + 0.5F) / perSide * 2.0F - 1.0F) * 1.1F;
        double height = 0.6D + perSide * 0.08D;
        return owner.getEyePosition().add(forward.scale(1.4D)).add(right.scale(side * (1.0D + Mth.cos(angle) * 0.5D))).add(0.0D, Mth.sin(angle) * height - 0.3D, 0.0D);
    }

    @Nullable
    private LivingEntity findTarget(LivingEntity owner) {
        LivingEntity found = null;
        Vec3 eye = owner.getEyePosition();
        Vec3 end = eye.add(owner.getLookAngle().scale(8.0D));
        BlockHitResult blockHit = this.level().clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(owner, eye, end, new AABB(eye, end).inflate(1.0D), entity -> entity instanceof LivingEntity living && this.isValidTarget(owner, living), eye.distanceToSqr(end));
        if (entityHit != null) {
            found = (LivingEntity) entityHit.getEntity();
        } else {
            LivingEntity lastHurt = owner.getLastHurtMob();
            if (lastHurt != null && owner.tickCount - owner.getLastHurtMobTimestamp() < 100 && this.isValidTarget(owner, lastHurt)) {
                found = lastHurt;
            }
        }
        return found;
    }

    private boolean isValidTarget(LivingEntity owner, LivingEntity entity) {
        return entity.isAlive() && entity != owner && !entity.isSpectator() && !entity.isAlliedTo(owner) && !owner.isAlliedTo(entity) && !this.hitEntities.contains(entity.getId()) && entity.distanceToSqr(this) < 48.0D * 48.0D;
    }

    private void swap(LivingEntity owner) {
        boolean targeting = this.isTargeting();
        List<ManiaBrain> brains = this.level().getEntitiesOfClass(ManiaBrain.class, this.getBoundingBox().inflate(8.0D), brain -> brain != this && brain.getOwner() == owner && brain.isTargeting() == targeting);
        if (!brains.isEmpty()) {
            ManiaBrain other = brains.get(this.random.nextInt(brains.size()));
            Vec3 position = this.position();
            this.setPos(other.position());
            other.setPos(position);
            this.entityData.set(SWAPS, this.entityData.get(SWAPS) + 1);
            other.entityData.set(SWAPS, other.entityData.get(SWAPS) + 1);
        }
    }

    private void checkHits() {
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());
        BlockHitResult blockHit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0D), this::canHitEntity)) {
            AABB box = entity.getBoundingBox().inflate(this.getBbWidth() * 0.5F);
            if (box.contains(start) || box.clip(start, end).isPresent()) {
                this.onHit(new EntityHitResult(entity));
            }
        }
        if (blockHit.getType() != HitResult.Type.MISS) {
            this.onHit(blockHit);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level() instanceof ServerLevel serverLevel && !this.isRemoved()) {
            Entity entity = result.getEntity();
            DamageSource source = OFDamageTypes.causeManiaDamage(serverLevel.registryAccess(), this, this.getOwner());
            float damage = DAMAGE;
            if (!this.weapon.isEmpty()) {
                damage = EnchantmentHelper.modifyDamage(serverLevel, this.weapon, entity, source, DAMAGE);
            }
            entity.hurt(source, damage);
            if (entity instanceof LivingEntity living && living.isAlive()) {
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2), this.getOwner());
                living.addEffect(new MobEffectInstance(OFMobEffects.MANIA, 100, this.maniaAmplifier), this.getOwner());
            }
            this.hitEntities.add(entity.getId());
            if (entity == this.target) {
                this.target = null;
                this.spentTicks = 1;
            }
            this.burst(serverLevel);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        Entity owner = this.getOwner();
        return super.canHitEntity(entity) && entity != owner && !this.hitEntities.contains(entity.getId()) && (owner == null || !entity.isAlliedTo(owner) && !owner.isAlliedTo(entity));
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.level() instanceof ServerLevel serverLevel && !this.isRemoved()) {
            this.burst(serverLevel);
            this.discard();
        }
    }

    private void burst(ServerLevel level) {
        Vector3f color = this.isElite() ? new Vector3f(0.35F, 0.5F, 0.77F) : new Vector3f(0.7F, 0.42F, 0.48F);
        level.sendParticles(new DustParticleOptions(color, 1.0F), this.getX(), this.getY(0.5D), this.getZ(), 6, 0.15D, 0.15D, 0.15D, 0.0D);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 0.5F, 1.4F + this.random.nextFloat() * 0.4F);
    }
}
