package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.entity.utils.SplitMagicBonus;
import com.platypushasnohat.sinew.item.DisabledItem;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class RingProjectile extends Projectile implements SplitMagicBonus {

    public static final float DAMAGE = 2.5F;

    private static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.defineId(RingProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COUNT = SynchedEntityData.defineId(RingProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ORBIT_INDEX = SynchedEntityData.defineId(RingProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ORBIT_COUNT = SynchedEntityData.defineId(RingProjectile.class, EntityDataSerializers.INT);

    private ItemStack weapon = ItemStack.EMPTY;
    private int slot;
    private int launchDelay = -1;
    private float facing;
    private float prevFacing;

    public RingProjectile(EntityType<? extends RingProjectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public RingProjectile(Level level, Player owner, int index, int count, int slot, ItemStack weapon) {
        this(OFEntities.RING_PROJECTILE.get(), level);
        this.setOwner(owner);
        this.entityData.set(INDEX, index);
        this.entityData.set(COUNT, count);
        this.entityData.set(ORBIT_INDEX, index);
        this.entityData.set(ORBIT_COUNT, count);
        this.slot = slot;
        this.weapon = weapon.copy();
        this.setPos(owner.getX(), owner.getY(0.5D), owner.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(INDEX, 0);
        builder.define(COUNT, 5);
        builder.define(ORBIT_INDEX, 0);
        builder.define(ORBIT_COUNT, 5);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Index", this.getIndex());
        compoundTag.putInt("Count", this.getCount());
        compoundTag.putInt("Slot", this.slot);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(INDEX, compoundTag.getInt("Index"));
        this.entityData.set(COUNT, Math.max(1, compoundTag.getInt("Count")));
        this.slot = compoundTag.getInt("Slot");
    }

    public int getIndex() {
        return this.entityData.get(INDEX);
    }

    public int getCount() {
        return this.entityData.get(COUNT);
    }

    public int getOrbitIndex() {
        return this.entityData.get(ORBIT_INDEX);
    }

    public int getOrbitCount() {
        return this.entityData.get(ORBIT_COUNT);
    }

    public float getSwarmScale() {
        return 1.0F + Mth.square(this.getOrbitCount() / 40.0F);
    }

    public float getFacing(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.prevFacing, this.facing);
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
            float target = this.getOrbitIndex() * 360.0F / this.getOrbitCount();
            if (this.tickCount == 1) {
                this.prevFacing = target;
                this.facing = target;
            } else {
                this.prevFacing = this.facing;
                this.facing = Mth.rotLerp(0.3F, this.facing, target);
            }
        } else {
            if (this.getOwner() instanceof LivingEntity owner && owner.isAlive() && owner.level() == this.level() && this.tickCount < 190) {
                LivingEntity target = this.getTarget(owner);
                if (target == null) {
                    this.launchDelay = -1;
                    this.updateOrbit(owner);
                } else if (this.launchDelay < 0) {
                    this.launchDelay = (int) (this.getOrbitIndex() * 3.0F / this.getSwarmScale());
                } else if (this.launchDelay > 0) {
                    this.launchDelay--;
                }
                if (target != null && this.launchDelay == 0) {
                    Vec3 direction = target.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(0.9D);
                    this.setDeltaMovement(this.getDeltaMovement().lerp(direction, 0.3D));
                    if (this.getBoundingBox().inflate(0.2D).intersects(target.getBoundingBox())) {
                        DamageSource source = OFDamageTypes.causeCursedRingDamage(this.level().registryAccess(), this, owner);
                        target.hurt(source, EnchantmentHelper.modifyDamage((ServerLevel) this.level(), this.weapon, target, source, DAMAGE));
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 0.6F, SinewSoundUtils.randomizePitch(this.level()));
                        this.finish();
                    }
                } else {
                    float angle = owner.tickCount * 0.08F + this.getOrbitIndex() * Mth.TWO_PI / this.getOrbitCount();
                    float radius = 1.1F * Mth.sqrt(this.getSwarmScale());
                    Vec3 orbit = owner.position().add(Mth.cos(angle) * radius, owner.getBbHeight() * 0.6F + Mth.sin(this.tickCount * 0.15F + this.getIndex()) * 0.1F, Mth.sin(angle) * radius);
                    this.setDeltaMovement(orbit.subtract(this.position()).scale(0.35D));
                }
            } else {
                this.finish();
            }
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
    }

    @Nullable
    private LivingEntity getTarget(LivingEntity owner) {
        LivingEntity target = owner.getLastHurtMob();
        return target != null && target.isAlive() && target != owner && owner.tickCount - owner.getLastHurtMobTimestamp() < 100 ? target : null;
    }

    private void updateOrbit(LivingEntity owner) {
        List<RingProjectile> rings = this.level().getEntitiesOfClass(RingProjectile.class, this.getBoundingBox().inflate(64.0D), ring -> ring.getOwner() == owner);
        this.entityData.set(ORBIT_INDEX, (int) rings.stream().filter(ring -> ring.getIndex() < this.getIndex()).count());
        this.entityData.set(ORBIT_COUNT, rings.size());
    }

    private void finish() {
        if (this.getOwner() instanceof ServerPlayer player && this.level().getEntitiesOfClass(RingProjectile.class, this.getBoundingBox().inflate(64.0D), ring -> ring != this && ring.getOwner() == player).isEmpty()) {
            DisabledItem.enableItem(player, this.slot);
            player.getCooldowns().addCooldown(OFItems.CURSED_RINGLINE.get(), 60);
        }
        this.discard();
    }
}