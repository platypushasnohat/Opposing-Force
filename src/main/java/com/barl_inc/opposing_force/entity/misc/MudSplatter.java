package com.barl_inc.opposing_force.entity.misc;

import com.barl_inc.opposing_force.entity.Motif;
import com.barl_inc.opposing_force.registry.OFEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class MudSplatter extends Entity {

    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(MudSplatter.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> LAYER = SynchedEntityData.defineId(MudSplatter.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(MudSplatter.class, EntityDataSerializers.OPTIONAL_UUID);

    public MudSplatter(EntityType<? extends MudSplatter> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public MudSplatter(Level level, double x, double y, double z) {
        this(OFEntities.MUD_SPLATTER.get(), level);
        this.setPos(this.spreadPosition(new Vec3(x, y, z)));
        this.setYRot(this.random.nextFloat() * 360.0F);
        this.setSize(1.0F + this.random.nextFloat() * 0.5F);
        int layer = 0;
        for (MudSplatter splatter : level.getEntitiesOfClass(MudSplatter.class, this.getBoundingBox())) {
            layer = Math.max(layer, splatter.getLayer() + 1);
        }
        this.setLayer(layer);
    }

    private Vec3 spreadPosition(Vec3 pos) {
        Vec3 raised = pos.add(0.0D, 0.1D, 0.0D);
        Vec3 spread = raised.add((this.random.nextDouble() - 0.5D) * 0.8D, 0.0D, (this.random.nextDouble() - 0.5D) * 0.8D);
        if (this.level().clip(new ClipContext(raised, spread, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS) {
            BlockHitResult ground = this.level().clip(new ClipContext(spread, spread.subtract(0.0D, 0.6D, 0.0D), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (ground.getType() != HitResult.Type.MISS) {
                return ground.getLocation();
            }
        }
        return pos;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIZE, 1.0F);
        builder.define(LAYER, 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (SIZE.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.tickCount = compoundTag.getInt("Age");
        this.setSize(compoundTag.getFloat("Size"));
        this.setLayer(compoundTag.getInt("Layer"));
        if (compoundTag.hasUUID("Owner")) {
            this.setOwnerId(compoundTag.getUUID("Owner"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("Age", this.tickCount);
        compoundTag.putFloat("Size", this.getSize());
        compoundTag.putInt("Layer", this.getLayer());
        this.entityData.get(OWNER).ifPresent(uuid -> compoundTag.putUUID("Owner", uuid));
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(float size) {
        this.entityData.set(SIZE, Mth.clamp(size, 1.0F, 1.5F));
    }

    public void setOwnerId(UUID uuid) {
        this.entityData.set(OWNER, Optional.of(uuid));
    }

    public boolean isOwner(Entity entity) {
        return this.entityData.get(OWNER).map(uuid -> uuid.equals(entity.getUUID())).orElse(false);
    }

    public int getLayer() {
        return this.entityData.get(LAYER);
    }

    public void setLayer(int layer) {
        this.entityData.set(LAYER, Mth.clamp(layer, 0, 12));
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.getSize(), 1.0F);
    }

    @Override
    public void refreshDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPos(x, y, z);
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.tickCount >= 100) {
            this.discard();
        }
        else {
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), living -> !(living instanceof Motif) && !this.isOwner(living))) {
                entity.makeStuckInBlock(Blocks.MUD.defaultBlockState(), new Vec3(0.36D, 0.10F, 0.36D));
            }
        }
    }

    public float getFade(float partialTicks) {
        return Mth.clamp((100 - (this.tickCount + partialTicks)) / 20, 0.0F, 1.0F);
    }
}
