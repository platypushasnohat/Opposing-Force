package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.item.magic.ManiaItem;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
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
    public static final int CHARGING = 0;
    public static final int WILD = 1;
    public static final int HOMING = 2;
    public static final int SPENT = 3;

    private static final EntityDataAccessor<Boolean> ELITE = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COUNT = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> SWAPS = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.INT);

    private ItemStack weapon = ItemStack.EMPTY;
    private int index;
    private int maniaAmplifier;
    private float speed = 1.0F;
    private int stateTicks;
    private int wildDuration = 15;
    private Vec3 forward = Vec3.ZERO;
    private Vec3 clusterOffset = Vec3.ZERO;
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
        this.maniaAmplifier = maniaAmplifier;
        this.speed = 0.8F + this.random.nextFloat() * 0.4F;
        this.entityData.set(ELITE, elite);
        this.entityData.set(COUNT, count);
        this.rollClusterOffset();
        this.setPos(this.getClusterPosition(owner));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ELITE, false);
        builder.define(STATE, CHARGING);
        builder.define(COUNT, 6);
        builder.define(SIZE, 0.4F);
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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(ELITE, compoundTag.getBoolean("Elite"));
        this.entityData.set(STATE, SPENT);
    }

    public boolean isElite() {
        return this.entityData.get(ELITE);
    }

    public int getState() {
        return this.entityData.get(STATE);
    }

    public boolean isCharging() {
        return this.getState() == CHARGING;
    }

    public int getCount() {
        return this.entityData.get(COUNT);
    }

    public float getBrainScale() {
        return 1.2F * this.entityData.get(SIZE);
    }

    @Override
    public int getMagicCount() {
        return this.getCount();
    }

    public void release(Player owner) {
        this.setState(WILD);
        this.wildDuration = 10 + this.random.nextInt(11);
        this.forward = owner.getLookAngle();
        this.setDeltaMovement(this.forward.scale(0.7D * this.speed).add(this.random.nextGaussian() * 0.4D, this.random.nextGaussian() * 0.3D, this.random.nextGaussian() * 0.4D));
    }

    private void setState(int state) {
        this.entityData.set(STATE, state);
        this.stateTicks = 0;
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
        this.stateTicks++;
        int state = this.getState();
        if (state == CHARGING) {
            this.tickCharging(owner);
        } else if (state == WILD) {
            this.tickWild();
        } else if (state == HOMING) {
            this.tickHoming(owner);
        } else {
            this.tickSpent();
        }
        if (!this.isRemoved() && this.random.nextFloat() < this.getSwapChance()) {
            this.swap(owner);
        }
    }

    private void tickCharging(LivingEntity owner) {
        if (owner.isUsingItem() && owner.getUseItem().getItem() instanceof ManiaItem) {
            if (this.random.nextFloat() < 0.1F) {
                this.rollClusterOffset();
            }
            this.setDeltaMovement(this.getClusterPosition(owner).subtract(this.position()).scale(0.6D));
        } else {
            this.discard();
        }
    }

    private void tickWild() {
        Vec3 wander = new Vec3(this.random.nextGaussian(), this.random.nextGaussian() * 0.6D, this.random.nextGaussian()).scale(0.3D);
        this.setDeltaMovement(this.getDeltaMovement().add(wander).add(this.forward.scale(0.12D)).normalize().scale(0.7D * this.speed));
        this.checkHits(false);
        this.entityData.set(SIZE, Mth.lerp(Math.min(this.stateTicks / (float) this.wildDuration, 1.0F), 0.4F, 1.0F));
        if (this.stateTicks >= this.wildDuration) {
            this.setState(HOMING);
        }
    }

    private void tickHoming(LivingEntity owner) {
        if (this.target == null || !this.isValidTarget(owner, this.target)) {
            this.target = this.findTarget(owner);
        }
        if (this.target != null) {
            Vec3 direction = this.target.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(1.8D * this.speed);
            this.setDeltaMovement(this.getDeltaMovement().lerp(direction, 0.4D));
            this.checkHits(true);
            if (this.stateTicks > 60) {
                this.discard();
            }
        } else {
            this.setState(SPENT);
        }
    }

    private void tickSpent() {
        this.checkHits(true);
        this.entityData.set(SIZE, Mth.clamp(1.0F - this.stateTicks / 10.0F, 0.0F, 1.0F));
        if (this.stateTicks > 10) {
            this.discard();
        }
    }

    private void tickClient() {
        if (this.snapTicks > 0) {
            this.snapTicks--;
        }
        if (this.glitchTicks > 0) {
            this.glitchTicks--;
        } else if (this.random.nextFloat() < this.getGlitchChance()) {
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
        ParticleOptions sparkle = this.isElite() ? OFParticleTypes.ELITE_MANIA_SPARKLE.get() : OFParticleTypes.MANIA_SPARKLE.get();
        if (this.isCharging() && this.random.nextFloat() < 0.3F) {
            Vec3 spark = this.position().add(this.random.nextGaussian() * 0.1D, this.getBbHeight() * 0.5D + this.random.nextGaussian() * 0.1D, this.random.nextGaussian() * 0.1D);
            this.level().addParticle(sparkle, spark.x, spark.y, spark.z, this.random.nextGaussian() * 0.03D, this.random.nextGaussian() * 0.03D + 0.01D, this.random.nextGaussian() * 0.03D);
        }
        if (!this.isCharging() && motion.lengthSqr() > 0.04D) {
            Vec3 drift = motion.scale(-0.1D);
            for (int i = 0; i < 2; i++) {
                Vec3 trail = this.position().subtract(motion.scale(this.random.nextDouble())).add(this.random.nextGaussian() * 0.08D, this.getBbHeight() * 0.5D + this.random.nextGaussian() * 0.08D, this.random.nextGaussian() * 0.08D);
                this.level().addParticle(sparkle, trail.x, trail.y, trail.z, drift.x + this.random.nextGaussian() * 0.02D, drift.y + this.random.nextGaussian() * 0.02D, drift.z + this.random.nextGaussian() * 0.02D);
            }
        }
    }

    private float getGlitchChance() {
        return new float[]{0.2F, 0.2F, 0.15F, 0.08F}[this.getState()];
    }

    private float getSwapChance() {
        return new float[]{0.06F, 0.04F, 0.03F, 0.0F}[this.getState()];
    }

    private void rollClusterOffset() {
        this.clusterOffset = new Vec3(this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian()).normalize().scale(this.random.nextDouble() * 0.08D);
    }

    private Vec3 getClusterPosition(LivingEntity owner) {
        Vec3 look = owner.getLookAngle();
        Vec3 right = look.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (right.lengthSqr() < 1.0E-4D) {
            right = new Vec3(1.0D, 0.0D, 0.0D);
        } else {
            right = right.normalize();
        }
        Vec3 up = right.cross(look);
        float angle = Mth.TWO_PI * this.index / this.getCount() + owner.tickCount * 0.05F;
        return owner.getEyePosition().add(look.scale(2.0D)).add(right.scale(Mth.cos(angle) * 0.55D)).add(up.scale(Mth.sin(angle) * 0.55D)).add(0.0D, -this.getBbHeight() * 0.5D, 0.0D).add(this.clusterOffset);
    }

    @Nullable
    private LivingEntity findTarget(LivingEntity owner) {
        LivingEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16.0D), entity -> this.isValidTarget(owner, entity))) {
            double distance = entity.distanceToSqr(this);
            if (distance < nearestDistance) {
                nearest = entity;
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    private boolean isValidTarget(LivingEntity owner, LivingEntity entity) {
        boolean hostile = entity instanceof Enemy || entity == owner.getLastHurtMob() || entity == owner.getLastHurtByMob() || entity instanceof Mob mob && mob.getTarget() == owner;
        return hostile && entity.isAlive() && !entity.isSpectator() && !this.isAlly(owner, entity) && !this.hitEntities.contains(entity.getId()) && entity.distanceToSqr(this) < 24.0D * 24.0D;
    }

    private boolean isAlly(Entity owner, Entity entity) {
        return entity == owner || entity.isAlliedTo(owner) || owner.isAlliedTo(entity);
    }

    private void swap(LivingEntity owner) {
        int state = this.getState();
        List<ManiaBrain> brains = this.level().getEntitiesOfClass(ManiaBrain.class, this.getBoundingBox().inflate(8.0D), brain -> brain != this && brain.getOwner() == owner && brain.getState() == state);
        if (!brains.isEmpty()) {
            ManiaBrain other = brains.get(this.random.nextInt(brains.size()));
            Vec3 position = this.position();
            this.setPos(other.position());
            other.setPos(position);
            this.entityData.set(SWAPS, this.entityData.get(SWAPS) + 1);
            other.entityData.set(SWAPS, other.entityData.get(SWAPS) + 1);
        }
    }

    private void checkHits(boolean blocks) {
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());
        BlockHitResult blockHit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        boolean hitBlock = blocks && blockHit.getType() != HitResult.Type.MISS;
        if (hitBlock) {
            end = blockHit.getLocation();
        }
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0D), this::canHitEntity)) {
            AABB box = entity.getBoundingBox().inflate(this.getBbWidth() * 0.5F);
            if (box.contains(start) || box.clip(start, end).isPresent()) {
                this.onHit(new EntityHitResult(entity));
            }
        }
        if (hitBlock) {
            this.onHit(blockHit);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.isRemoved()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            Entity entity = result.getEntity();
            DamageSource source = OFDamageTypes.causeManiaDamage(serverLevel.registryAccess(), this, this.getOwner());
            entity.hurt(source, EnchantmentHelper.modifyDamage(serverLevel, this.weapon, entity, source, DAMAGE));
            if (entity instanceof LivingEntity living && living.isAlive()) {
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 140, 2), this.getOwner());
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 140, 2), this.getOwner());
                living.addEffect(new MobEffectInstance(OFMobEffects.MANIA, 220, this.maniaAmplifier), this.getOwner());
            }
            this.hitEntities.add(entity.getId());
            if (entity == this.target) {
                this.target = null;
                this.setState(SPENT);
            }
            this.burst(serverLevel);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        Entity owner = this.getOwner();
        return !this.isCharging() && super.canHitEntity(entity) && !this.hitEntities.contains(entity.getId()) && (owner == null || !this.isAlly(owner, entity));
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.isRemoved()) {
            this.burst((ServerLevel) this.level());
            this.discard();
        }
    }

    private void burst(ServerLevel level) {
        Vector3f color = this.isElite() ? new Vector3f(0.35F, 0.5F, 0.77F) : new Vector3f(0.7F, 0.42F, 0.48F);
        level.sendParticles(new DustParticleOptions(color, 1.0F), this.getX(), this.getY(0.5D), this.getZ(), 6, 0.15D, 0.15D, 0.15D, 0.0D);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 0.5F, 1.4F + this.random.nextFloat() * 0.4F);
    }
}
