package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.registry.OFEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HeroSwordBeam extends Projectile {

    public static final float SPEED = 1.5F;

    private float damage = 2.0F;

    public HeroSwordBeam(EntityType<? extends HeroSwordBeam> entityType, Level level) {
        super(entityType, level);
    }

    public HeroSwordBeam(Level level, LivingEntity owner, float damage) {
        this(OFEntities.HERO_SWORD_BEAM.get(), level);
        this.setOwner(owner);
        this.damage = damage;
        float yaw = owner.getYRot() * Mth.DEG_TO_RAD;
        double side = owner.getMainArm() == HumanoidArm.RIGHT ? 1.0D : -1.0D;
        double rightX = -Mth.cos(yaw) * side;
        double rightZ = -Mth.sin(yaw) * side;
        double forwardX = -Mth.sin(yaw);
        double forwardZ = Mth.cos(yaw);
        this.setPos(owner.getX() + rightX * 0.35D + forwardX * 0.3D, owner.getEyeY() - 0.6D, owner.getZ() + rightZ * 0.35D + forwardZ * 0.3D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS) {
            this.onHit(hit);
        }
        Vec3 velocity = this.getDeltaMovement();
        this.setPos(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);
        if (!this.level().isClientSide && this.tickCount >= 20) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity owner = this.getOwner();
            DamageSource source = this.damageSources().mobProjectile(this, owner instanceof LivingEntity living ? living : null);
            result.getEntity().hurt(source, this.damage);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage", this.damage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.damage = tag.getFloat("Damage");
    }
}
