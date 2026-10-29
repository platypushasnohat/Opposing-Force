package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.*;
import com.platypushasnohat.sinew.entity.utils.SplitProjectileBonus;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;

public class Bullet extends Projectile implements ItemSupplier, SplitProjectileBonus {

    protected static final EntityDataAccessor<ItemStack> RENDER_STACK = SynchedEntityData.defineId(Bullet.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(Bullet.class, EntityDataSerializers.FLOAT);

    public static final int LIFESPAN = 100;
    public static final byte HIT_EFFECTS = 3;

    @Nullable
    protected ItemStack firedFromWeapon = null;
    private int projectileCount = 1;

    public Bullet(EntityType<? extends Bullet> entityType, Level level) {
        super(entityType, level);
    }

    public Bullet(EntityType<? extends Bullet> entityType, double x, double y, double z, Level level) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    public Bullet(LivingEntity shooter, Level level, ItemStack weapon) {
        this(OFEntities.BULLET.get(), shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), level);
        this.setOwner(shooter);
        if (weapon != null && level instanceof ServerLevel) {
            this.firedFromWeapon = weapon.copy();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RENDER_STACK, new ItemStack(this.getDefaultItem()));
        builder.define(DAMAGE, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("Item", this.getItem().save(this.registryAccess()));
        if (this.firedFromWeapon != null) {
            compoundTag.put("Weapon", this.firedFromWeapon.save(this.registryAccess(), new CompoundTag()));
        }
        compoundTag.putFloat("Damage", this.getDamage());
        compoundTag.putInt("ProjectileCount", this.projectileCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("Item", 10)) {
            this.setItem(ItemStack.parse(this.registryAccess(), compoundTag.getCompound("Item")).orElseGet(() -> new ItemStack(this.getDefaultItem())));
        } else {
            this.setItem(new ItemStack(this.getDefaultItem()));
        }
        if (compoundTag.contains("Weapon", 10)) {
            this.firedFromWeapon = ItemStack.parse(this.registryAccess(), compoundTag.getCompound("Weapon")).orElse(null);
        } else {
            this.firedFromWeapon = null;
        }
        this.setDamage(compoundTag.getFloat("Damage"));
        if (compoundTag.contains("ProjectileCount")) {
            this.projectileCount = compoundTag.getInt("ProjectileCount");
        }
    }

    @Override
    public ItemStack getItem() {
        return this.getEntityData().get(RENDER_STACK);
    }
    public void setItem(ItemStack stack) {
        this.getEntityData().set(RENDER_STACK, stack.copyWithCount(1));
    }
    public Item getDefaultItem() {
        return OFItems.SHOT_IRON_BULLET.get();
    }
    @Override
    public ItemStack getWeaponItem() {
        return this.firedFromWeapon;
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }
    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    @Override
    public int getProjectileCount() {
        return this.projectileCount;
    }
    public void setProjectileCount(int projectileCount) {
        this.projectileCount = projectileCount;
    }

    public void tickRotation() {
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);
    }

    public void tickMovement() {
        Vec3 movement = this.getDeltaMovement();
        double nextX = this.getX() + movement.x;
        double nextY = this.getY() + movement.y;
        double nextZ = this.getZ() + movement.z;
        this.tickRotation();
        if (this.isInWater()) {
            for (int i = 0; i < 4; i++) {
                this.level().addParticle(ParticleTypes.BUBBLE, nextX - movement.x * 0.25D, nextY - movement.y * 0.25D, nextZ - movement.z * 0.25D, movement.x, movement.y, movement.z);
            }
            this.setDeltaMovement(movement.scale(0.8F));
        }
        if (this.level().isClientSide) {
            float height = this.getBbHeight() / 2;
            if (this.getTrailParticle() != null) {
                this.level().addParticle(this.getTrailParticle(), nextX, nextY + height, nextZ, 0.0D, 0.0D, 0.0D);
            }
            if (this.getRingParticle() != null && this.tickCount % 4 == 0) {
                this.level().addParticle(this.getRingParticle(), nextX, nextY + height, nextZ, 0.0D, 0.0D, 0.0D);
            }
        }
        this.applyGravity();
        this.setPos(nextX, nextY, nextZ);
    }

    public void processCollision() {
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitResult)) {
            this.onHit(hitResult);
        }
    }

    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
        Vec3 clamped = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        EnchantmentHelper.onHitBlock(level, stack, this.getOwner() instanceof LivingEntity living ? living : null, this, null, clamped, level.getBlockState(hitResult.getBlockPos()), item -> this.discard());
    }

    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        double knockbackModifier = this.firedFromWeapon != null && this.level() instanceof ServerLevel serverlevel ? EnchantmentHelper.modifyKnockback(serverlevel, this.firedFromWeapon, entity, damageSource, 0.0F) : 0.0F;
        if (knockbackModifier > 0.0D) {
            double resistance = Math.max(0.0D, 1.0D - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 scaled = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(knockbackModifier * 0.2D * resistance);
            if (scaled.lengthSqr() > 0.0D) {
                entity.push(scaled.x, 0.1D, scaled.z);
            }
        }
    }

    @Nullable
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SMOKE;
    }

    @Nullable
    protected ParticleOptions getRingParticle() {
        return OFParticleTypes.BULLET_TRAIL.get();
    }

    @Nullable
    protected ParticleOptions getImpactParticle() {
        return OFParticleTypes.BULLET_IMPACT.get();
    }

    protected DamageSource getDamageSource() {
        return OFDamageTypes.causeBulletDamage(this.level().registryAccess(), this, this.getOwner());
    }

    protected SoundEvent getImpactSound() {
        return OFSoundEvents.BULLET_IMPACT.get();
    }

    protected void playImpactSound(double x, double y, double z) {
        this.level().playSound(null, x, y, z, this.getImpactSound(), this.getSoundSource(), 0.8F, SinewSoundUtils.randomizePitch(this.level()));
    }

    protected void spawnImpactParticles() {
        if (this.getImpactParticle() != null) {
            this.level().addParticle(this.getImpactParticle(), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
        }
    }

    public void doImpactEffects(double x, double y, double z) {
        this.playImpactSound(x, y, z);
        this.level().broadcastEntityEvent(this, HIT_EFFECTS);
    }

    @Override
    public void tick() {
        super.tick();
        this.processCollision();
        this.checkInsideBlocks();
        this.tickMovement();

        if (!this.level().isClientSide) {
            if (this.tickCount > LIFESPAN || this.getBlockY() > this.level().getMaxBuildHeight() + 30) {
                this.doImpactEffects(this.getX(), this.getY(), this.getZ());
                this.discard();
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.01D;
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
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
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = this.getBoundingBox().getSize() * 10.0;
        if (Double.isNaN(size)) {
            size = 1.0;
        }
        size *= 64.0 * getViewScale();
        return distance < size * size;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        float damage = this.getDamage();
        DamageSource damageSource = this.getDamageSource();
        ItemStack weapon = this.getWeaponItem();
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            if (weapon != null) {
                damage = EnchantmentHelper.modifyDamage(serverLevel, weapon, entity, damageSource, damage);
            }
            if (entity.hurt(damageSource, damage) && entity instanceof LivingEntity living) {
                this.doKnockback(living, damageSource);
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, living, damageSource, weapon);
            }
            this.doImpactEffects(entity.getX(), entity.getY(), entity.getZ());
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        if (!this.level().isClientSide) {
            ItemStack weapon = this.getWeaponItem();
            if (this.level() instanceof ServerLevel serverLevel && weapon != null) {
                this.hitBlockEnchantmentEffects(serverLevel, result, weapon);
            }
            this.doImpactEffects(pos.getX(), pos.getY(), pos.getZ());
            this.discard();
        }
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float xPos = -Mth.sin(y * Mth.DEG_TO_RAD) * Mth.cos(x * Mth.DEG_TO_RAD);
        float yPos = -Mth.sin((x + z) * Mth.DEG_TO_RAD);
        float zPos = Mth.cos(y * Mth.DEG_TO_RAD) * Mth.cos(x * Mth.DEG_TO_RAD);
        this.shoot(xPos, yPos, zPos, velocity, inaccuracy);
        Vec3 knownMovement = shooter.getKnownMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(knownMovement.x, 0.0F, knownMovement.z));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == HIT_EFFECTS) {
            this.spawnImpactParticles();
        }
    }
}
