package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.platypushasnohat.sinew.entity.utils.SplitMagicBonus;
import net.minecraft.core.particles.DustParticleOptions;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class ManiaBrain extends Projectile implements SplitMagicBonus {

    public static final float DAMAGE = 2.5F;
    public static final int LIFETIME = 6;

    private static final EntityDataAccessor<Integer> SHADE = SynchedEntityData.defineId(ManiaBrain.class, EntityDataSerializers.INT);

    private ItemStack weapon = ItemStack.EMPTY;
    private Vec3 forward = Vec3.ZERO;
    private int count = 4;
    private final Set<Integer> hitEntities = new HashSet<>();

    public ManiaBrain(EntityType<? extends ManiaBrain> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public ManiaBrain(Level level, Player owner, ItemStack weapon, Shade shade, int count) {
        this(OFEntities.MANIA_BRAIN.get(), level);
        this.setOwner(owner);
        this.weapon = weapon.copy();
        this.count = count;
        this.entityData.set(SHADE, shade.ordinal());
        this.forward = owner.getLookAngle();
        this.setPos(owner.getEyePosition().add(this.forward.scale(0.6D)).subtract(0.0D, 0.15D, 0.0D));
        this.setDeltaMovement(this.getVelocity());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SHADE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Shade", this.entityData.get(SHADE));
        compoundTag.putInt("Count", this.count);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(SHADE, Mth.clamp(compoundTag.getInt("Shade"), 0, Shade.values().length - 1));
        this.count = Math.max(1, compoundTag.getInt("Count"));
    }

    public Shade getShade() {
        return Shade.values()[Mth.clamp(this.entityData.get(SHADE), 0, Shade.values().length - 1)];
    }

    @Override
    public int getMagicCount() {
        return this.count;
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

    private Vec3 getVelocity() {
        return this.forward.scale(1.2D);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.forward.lengthSqr() > 0.0D) {
                this.setDeltaMovement(this.getVelocity());
            }
            Vec3 start = this.position();
            Vec3 end = start.add(this.getDeltaMovement());
            BlockHitResult blockHit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (blockHit.getType() != HitResult.Type.MISS) {
                end = blockHit.getLocation();
            }
            for (Entity target : this.level().getEntities(this, this.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0D), this::canHitEntity)) {
                AABB box = target.getBoundingBox().inflate(this.getBbWidth() * 0.5F);
                if (box.contains(start) || box.clip(start, end).isPresent()) {
                    this.onHit(new EntityHitResult(target));
                }
            }
            if (blockHit.getType() != HitResult.Type.MISS) {
                this.onHit(blockHit);
            }
            if (this.tickCount >= LIFETIME || this.forward.lengthSqr() == 0.0D) {
                this.discard();
            }
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level() instanceof ServerLevel serverLevel && !this.isRemoved()) {
            DamageSource source = OFDamageTypes.causeManiaDamage(serverLevel.registryAccess(), this, this.getOwner());
            float damage = DAMAGE;
            if (!this.weapon.isEmpty()) {
                damage = EnchantmentHelper.modifyDamage(serverLevel, this.weapon, result.getEntity(), source, DAMAGE);
            }
            result.getEntity().hurt(source, damage);
            if (result.getEntity() instanceof LivingEntity target && target.isAlive()) {
                this.applyShade(target);
            }
            this.hitEntities.add(result.getEntity().getId());
            this.burst(serverLevel);
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && !this.hitEntities.contains(target.getId());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.level() instanceof ServerLevel serverLevel && !this.isRemoved()) {
            this.burst(serverLevel);
            this.discard();
        }
    }

    private void applyShade(LivingEntity target) {
        Shade shade = this.getShade();
        if (shade == Shade.BLACK) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1), this.getOwner());
        } else if (shade == Shade.CYAN) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1), this.getOwner());
        } else if (shade == Shade.YELLOW) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0), this.getOwner());
        } else if (shade == Shade.MAGENTA) {
            target.addEffect(new MobEffectInstance(OFMobEffects.MANIA, 160, 0), this.getOwner());
        } else if (shade == Shade.PURPLE) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1), this.getOwner());
        } else if (shade == Shade.GREEN) {
            Vec3 push = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize();
            target.knockback(1.2D, -push.x, -push.z);
        } else if (shade == Shade.BLUE) {
            target.addEffect(new MobEffectInstance(OFMobEffects.MANIA, 160, 1), this.getOwner());
        }
    }

    private void burst(ServerLevel level) {
        int color = this.getShade().color;
        DustParticleOptions dust = new DustParticleOptions(new Vector3f((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F), 1.0F);
        level.sendParticles(dust, this.getX(), this.getY(), this.getZ(), 6, 0.15D, 0.15D, 0.15D, 0.0D);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 0.5F, 1.4F + this.random.nextFloat() * 0.4F);
    }

    public enum Shade {
        BLACK(0x1A1A1A),
        CYAN(0x00FFFF),
        MAGENTA(0xFF00FF),
        YELLOW(0xFFFF00),
        PURPLE(0x8A2BE2),
        GREEN(0x2BFF4A),
        BLUE(0x2B5BFF);

        public final int color;

        Shade(int color) {
            this.color = color;
        }
    }
}
