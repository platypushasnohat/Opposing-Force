package com.barl_inc.opposing_force.entity.misc;

import com.barl_inc.opposing_force.entity.projectile.Soundwave;
import com.barl_inc.opposing_force.registry.OFEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TachiSlash extends Entity {

    public static final int LIFETIME = 12;
    public static final double SPEED = 0.5D;

    private final Set<UUID> hitEntities = new HashSet<>();
    private float damage;
    @Nullable
    private LivingEntity owner;

    public TachiSlash(EntityType<? extends TachiSlash> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public TachiSlash(Level level, LivingEntity owner, Vec3 pos, float yaw, float damage, Collection<? extends Entity> alreadyHit) {
        this(OFEntities.TACHI_SLASH.get(), level);
        this.owner = owner;
        this.damage = damage;
        this.setPos(pos);
        this.setYRot(yaw);
        this.yRotO = yaw;
        this.setDeltaMovement(Vec3.directionFromRotation(0.0F, yaw).scale(SPEED));
        for (Entity entity : alreadyHit) {
            this.hitEntities.add(entity.getUUID());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());
        boolean blocked = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
        if (!blocked) {
            this.setPos(end);
        }
        if (!this.level().isClientSide) {
            if (this.owner != null && this.owner.isAlive()) {
                this.hitTargets(this.owner);
            }
            if (this.owner == null || blocked || this.tickCount >= LIFETIME) {
                this.discard();
            }
        }
    }

    private void hitTargets(LivingEntity owner) {
        DamageSource source = owner instanceof Player player ? this.damageSources().playerAttack(player) : this.damageSources().mobAttack(owner);
        float yaw = this.getYRot() * Mth.DEG_TO_RAD;
        for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.25D), target -> Soundwave.isEnemy(target, owner) && !this.hitEntities.contains(target.getUUID()))) {
            this.hitEntities.add(target.getUUID());
            if (target.hurt(source, this.damage)) {
                target.knockback(0.3F, Mth.sin(yaw), -Mth.cos(yaw));
            }
        }
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
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
