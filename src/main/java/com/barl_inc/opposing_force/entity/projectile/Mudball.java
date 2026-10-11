package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.entity.Motif;
import com.barl_inc.opposing_force.entity.misc.MudSplatter;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class Mudball extends Projectile {

    private static final byte HIT_EFFECTS = 3;

    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(Mudball.class, EntityDataSerializers.FLOAT);

    public AnimationState projectileAnimationState = new AnimationState();

    private float damage = 2.0F;

    public Mudball(EntityType<? extends Mudball> entityType, Level level) {
        super(entityType, level);
    }

    public Mudball(Level level, LivingEntity owner, double x, double y, double z) {
        this(OFEntities.MUDBALL.get(), level);
        this.setPos(x, y, z);
        this.setOwner(owner);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIZE, 1.0F);
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(float size) {
        this.entityData.set(SIZE, size);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (SIZE.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.getSize());
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }
        this.checkInsideBlocks();
        Vec3 deltaMovement = this.getDeltaMovement();
        double x = this.getX() + deltaMovement.x;
        double y = this.getY() + deltaMovement.y;
        double z = this.getZ() + deltaMovement.z;
        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
        this.setPos(x, y, z);
        this.setDeltaMovement(deltaMovement.add(0.0D, -0.03D, 0.0D));
        if (this.level().isClientSide) {
            this.projectileAnimationState.animateWhen(this.isAlive(), this.tickCount);
            if (this.tickCount % 2 == 0) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MUD.defaultBlockState()), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.tickCount > 160 || this.getBlockY() < this.level().getMinBuildHeight() - 30) {
            if (!this.level().isClientSide) {
                this.discard();
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !(entity instanceof Motif);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            Entity owner = this.getOwner();
            float damage = owner instanceof Motif motif ? (float) motif.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F : this.damage;
            DamageSource damageSource = this.damageSources().mobProjectile(this, owner instanceof LivingEntity shooter ? shooter : null);
            boolean blocked = entity instanceof LivingEntity hitEntity && hitEntity.isDamageSourceBlocked(damageSource);
            entity.hurt(damageSource, damage);
            if (!blocked) {
                this.placeSplatter(entity.position());
            }
            this.impact();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.placeSplatter(result.getLocation().add(Vec3.atLowerCornerOf(result.getDirection().getNormal()).scale(0.05D)));
            this.impact();
        }
    }

    private void impact() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.MUD_BREAK, SoundSource.HOSTILE, 1.0F, SinewSoundUtils.randomizePitch(this.level()));
        this.level().broadcastEntityEvent(this, HIT_EFFECTS);
        this.discard();
    }

    private void placeSplatter(Vec3 pos) {
        Vec3 start = pos.add(0.0D, 0.1D, 0.0D);
        Vec3 end = pos.subtract(0.0D, 1.25D, 0.0D);
        BlockHitResult ground = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (ground.getType() != HitResult.Type.MISS) {
            Vec3 location = ground.getLocation();
            MudSplatter splatter = new MudSplatter(this.level(), location.x, location.y, location.z);
            if (this.getOwner() instanceof Player player) {
                splatter.setOwnerId(player.getUUID());
            }
            this.level().addFreshEntity(splatter);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == HIT_EFFECTS) {
            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MUD.defaultBlockState());
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }
}