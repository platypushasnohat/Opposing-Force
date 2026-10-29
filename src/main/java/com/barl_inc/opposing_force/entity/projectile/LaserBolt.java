package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Arrays;

public class LaserBolt extends Bullet {

    private int pierceCount = 0;

    private final Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;

    public LaserBolt(EntityType<? extends LaserBolt> entityType, Level level) {
        super(entityType, level);
    }

    public LaserBolt(EntityType<? extends LaserBolt> entityType, double x, double y, double z, Level level) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    public LaserBolt(LivingEntity shooter, Level level, ItemStack weapon) {
        this(OFEntities.LASER_BOLT.get(), shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), level);
        this.setOwner(shooter);
        if (weapon != null && level instanceof ServerLevel) {
            this.firedFromWeapon = weapon.copy();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Nullable
    @Override
    protected ParticleOptions getTrailParticle() {
        return OFParticleTypes.LASER_DUST.get();
    }

    @Nullable
    @Override
    protected ParticleOptions getRingParticle() {
        return null;
    }

    @Override
    protected ParticleOptions getImpactParticle() {
        return OFParticleTypes.LASER_IMPACT.get();
    }

    @Override
    protected void spawnImpactParticles() {
        super.spawnImpactParticles();
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(OFParticleTypes.LASER_DUST.get(), this.getX(), this.getY() + 0.2F, this.getZ(), 0, 0, 0);
        }
    }

    @Override
    public void tickRotation() {
        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickTrail();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
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
                this.pierceCount++;
                this.setDamage(this.getDamage() * 0.66F);
                this.doImpactEffects(entity.getX(), entity.getY(), entity.getZ());
            }
            if (this.pierceCount > 1) {
                this.discard();
            }
        }
    }

    @Override
    protected SoundEvent getImpactSound() {
        return OFSoundEvents.LASER_BOLT_IMPACT.get();
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
        return this.trailPointer != -1;
    }
}