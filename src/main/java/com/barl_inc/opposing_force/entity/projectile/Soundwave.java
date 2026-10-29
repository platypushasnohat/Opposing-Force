package com.barl_inc.opposing_force.entity.projectile;

import com.barl_inc.opposing_force.entity.Pipsqueak;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Soundwave extends Projectile {

    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(Soundwave.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FADING = SynchedEntityData.defineId(Soundwave.class, EntityDataSerializers.BOOLEAN);

    private final List<Vec3> trail = new ArrayList<>();
    private final Set<UUID> hitEntities = new HashSet<>();
    private Type type = Type.SCREAM;
    private int phase;
    private float targetSize = 1.6F;
    private float shrinkSpeed = 0.04F;
    private boolean shrinkPending;
    private double distanceSinceBounce;
    private int fadeTicks;
    private int wallBounces;
    private float damageBonus;
    @Nullable
    private UUID ignoredEntity;
    @Nullable
    private LivingEntity homingTarget;

    public Soundwave(EntityType<? extends Soundwave> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public Soundwave(Level level, LivingEntity owner, Vec3 pos, Vec3 direction) {
        this(level, owner, Type.SCREAM, 0, 0.0F, pos, direction);
    }

    public Soundwave(Level level, LivingEntity owner, Type type, int phase, float damageBonus, Vec3 pos, Vec3 direction) {
        this(OFEntities.SOUNDWAVE.get(), level);
        this.setOwner(owner);
        this.type = type;
        this.phase = phase;
        this.damageBonus = damageBonus;
        if (type != Type.SPLIT) {
            this.wallBounces = phase;
        }
        this.targetSize = this.getStageSize();
        this.entityData.set(SIZE, this.targetSize);
        this.setPos(pos);
        this.setDeltaMovement(direction.normalize().scale(this.getSpeed()));
    }

    public static void releaseSplit(Level level, LivingEntity owner, LivingEntity source, int phase, int count, float damageBonus) {
        Vec3 center = source.getBoundingBox().getCenter();
        Set<LivingEntity> taken = new HashSet<>();
        taken.add(source);
        Vec3 previousDirection = null;
        for (int i = 0; i < count; i++) {
            LivingEntity target = findTarget(level, owner, center, taken);
            Vec3 direction;
            if (target != null) {
                taken.add(target);
                direction = target.getBoundingBox().getCenter().subtract(center);
                previousDirection = null;
            } else if (previousDirection != null) {
                direction = previousDirection.reverse();
            } else {
                direction = randomDirection(level.getRandom());
                previousDirection = direction;
            }
            Soundwave soundwave = new Soundwave(level, owner, Type.SPLIT, phase, damageBonus, center, direction);
            soundwave.ignoredEntity = source.getUUID();
            soundwave.homingTarget = target;
            level.addFreshEntity(soundwave);
        }
    }

    @Nullable
    public static LivingEntity findTarget(Level level, Entity owner, Vec3 from, Set<LivingEntity> excluded) {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(from, from).inflate(16.0D), entity -> !excluded.contains(entity) && isEnemy(entity, owner) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && level.clip(new ClipContext(from, entity.getBoundingBox().getCenter(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS)
                .stream().min(Comparator.comparingDouble(entity -> entity.distanceToSqr(from))).orElse(null);
    }

    public static boolean isEnemy(LivingEntity entity, @Nullable Entity owner) {
        boolean enemy = entity.isAlive() && entity != owner && !(entity instanceof ArmorStand);
        if (owner instanceof Pipsqueak) {
            enemy &= !(entity instanceof Pipsqueak);
        } else if (owner != null) {
            enemy &= !entity.isAlliedTo(owner) && !(entity instanceof OwnableEntity ownable && ownable.getOwner() == owner);
        }
        return enemy;
    }

    private static Vec3 randomDirection(RandomSource random) {
        return new Vec3(random.nextGaussian(), random.nextGaussian() * 0.5D, random.nextGaussian()).normalize();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIZE, 1.6F);
        builder.define(FADING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Type", this.type.ordinal());
        compoundTag.putInt("Phase", this.phase);
        compoundTag.putFloat("Size", this.getSize());
        compoundTag.putInt("WallBounces", this.wallBounces);
        compoundTag.putFloat("DamageBonus", this.damageBonus);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.type = Type.values()[Mth.clamp(compoundTag.getInt("Type"), 0, Type.values().length - 1)];
        this.phase = compoundTag.getInt("Phase");
        this.entityData.set(SIZE, compoundTag.getFloat("Size"));
        this.targetSize = this.getSize();
        this.wallBounces = compoundTag.getInt("WallBounces");
        this.damageBonus = compoundTag.getFloat("DamageBonus");
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    public boolean isFading() {
        return this.entityData.get(FADING);
    }

    public float getAlpha(float partialTicks) {
        float alpha = 1.0F;
        if (this.isFading()) {
            alpha = Mth.clamp(1.0F - (this.fadeTicks + partialTicks) / 10.0F, 0.0F, 1.0F);
        }
        return alpha;
    }

    private float getSpeed() {
        return Type.stage(this.type.speeds, this.phase);
    }

    private float getStageSize() {
        return Type.stage(this.type.sizes, this.phase);
    }

    private float getDamage() {
        return Type.stage(this.type.damages, this.phase);
    }

    private double getSpacing() {
        return 0.225D * this.getSize();
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
        if (this.isFading()) {
            this.fadeTicks++;
            if (!this.level().isClientSide && this.fadeTicks >= 10) {
                this.discard();
            }
        } else {
            this.trail.addFirst(this.position());
            if (this.trail.size() > 30) {
                this.trail.removeLast();
            }
            if (!this.level().isClientSide) {
                this.steer();
            }
            this.move();
            if (!this.level().isClientSide) {
                this.hitEntities();
                this.updateSize();
                if (this.tickCount > this.type.lifetime) {
                    this.entityData.set(FADING, true);
                }
            }
        }
    }

    private void steer() {
        if (this.homingTarget != null && this.homingTarget.isAlive() && this.homingTarget.level() == this.level()) {
            Vec3 wanted = this.homingTarget.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(this.getSpeed());
            this.setDeltaMovement(this.getDeltaMovement().lerp(wanted, 0.15D).normalize().scale(this.getSpeed()));
        } else {
            this.homingTarget = null;
        }
    }

    private void move() {
        Vec3 start = this.position();
        Vec3 velocity = this.getDeltaMovement();
        BlockHitResult hitResult = this.level().clip(new ClipContext(start, start.add(velocity), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            Vec3 normal = Vec3.atLowerCornerOf(hitResult.getDirection().getNormal());
            this.setPos(hitResult.getLocation().add(normal.scale(0.05D)));
            if (!this.level().isClientSide) {
                this.bounce(velocity, normal);
            }
        } else {
            this.setPos(start.add(velocity));
        }
        this.distanceSinceBounce += velocity.length();
    }

    private void bounce(Vec3 velocity, Vec3 normal) {
        this.wallBounces++;
        if (this.type != Type.SPLIT) {
            if (this.shrinkPending || this.getSize() > this.getStageSize()) {
                this.targetSize = this.getStageSize();
                this.shrinkSpeed = 0.2F;
            }
            this.phase++;
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), this.type.echo, this.type.soundSource, 0.8F * (float) Math.pow(0.7D, this.wallBounces - 1), this.type.pitch);
        if (this.wallBounces >= this.type.maxBounces) {
            this.setDeltaMovement(Vec3.ZERO);
            this.entityData.set(FADING, true);
        } else {
            Vec3 reflected = velocity.subtract(normal.scale(2.0D * velocity.dot(normal)));
            Vec3 direction = this.twist(reflected, 15.0F, 40.0F);
            if (direction.dot(normal) <= 0.0D) {
                direction = reflected;
            }
            if (this.type == Type.SPLIT || (this.type == Type.FORK && this.wallBounces % 3 == 0)) {
                LivingEntity target = findTarget(this.level(), this.getOwner(), this.position(), this.getIgnoredSet());
                if (target != null && target.getBoundingBox().getCenter().subtract(this.position()).dot(normal) > 0.0D) {
                    direction = target.getBoundingBox().getCenter().subtract(this.position());
                    this.homingTarget = target;
                }
            }
            this.setDeltaMovement(direction.normalize().scale(this.getSpeed()));
            if (this.type != Type.SPLIT) {
                this.hitEntities.clear();
                this.shrinkPending = true;
                this.distanceSinceBounce = 0.0D;
            }
        }
    }

    private Set<LivingEntity> getIgnoredSet() {
        Set<LivingEntity> ignored = new HashSet<>();
        if (this.type == Type.SPLIT && this.ignoredEntity != null && this.level() instanceof ServerLevel serverLevel && serverLevel.getEntity(this.ignoredEntity) instanceof LivingEntity entity) {
            ignored.add(entity);
        }
        return ignored;
    }

    private Vec3 twist(Vec3 direction, float minAngle, float maxAngle) {
        Vec3 normal = direction.normalize();
        Vec3 side = normal.cross(new Vec3(this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian())).normalize();
        float angle = Mth.nextFloat(this.random, minAngle, maxAngle) * Mth.DEG_TO_RAD;
        return normal.scale(Mth.cos(angle)).add(side.scale(Mth.sin(angle))).scale(direction.length());
    }

    private void updateSize() {
        if (this.shrinkPending && this.distanceSinceBounce >= this.getSpacing() * 2.0D) {
            this.shrinkPending = false;
            this.targetSize = this.getStageSize();
        }
        float size = this.getSize();
        if (size > this.targetSize) {
            size = Math.max(this.targetSize, size - this.shrinkSpeed);
            this.entityData.set(SIZE, size);
        } else {
            this.shrinkSpeed = 0.04F;
        }
    }

    private void hitEntities() {
        Entity owner = this.getOwner();
        LivingEntity attacker = null;
        if (owner instanceof LivingEntity livingOwner) {
            attacker = livingOwner;
        }
        List<Vec3> points = this.getSegmentPoints(this.position());
        AABB box = new AABB(points.getFirst(), points.getLast()).minmax(new AABB(points.get(1), points.get(1))).inflate(0.4D * this.getSize());
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, box, entity -> isEnemy(entity, owner) && !entity.getUUID().equals(this.ignoredEntity) && !this.hitEntities.contains(entity.getUUID()))) {
            if (!this.isRemoved()) {
                this.hitEntities.add(entity.getUUID());
                entity.hurt(this.getMeleeSource(attacker), this.getDamage() + this.damageBonus);
                if (entity instanceof Creeper creeper) {
                    creeper.ignite();
                }
                if (this.type == Type.SPLIT) {
                    if (this.phase < 2 && attacker != null) {
                        releaseSplit(this.level(), attacker, entity, this.phase + 1, 2, this.damageBonus);
                    }
                    this.discard();
                }
            }
        }
    }

    private DamageSource getMeleeSource(@Nullable LivingEntity attacker) {
        DamageSource source = this.damageSources().generic();
        if (this.type != Type.SCREAM && attacker != null) {
            source = new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(OFDamageTypes.SOUNDWAVE), attacker, attacker, this.position());
        } else if (attacker instanceof Player player) {
            source = new DamageSource(this.damageSources().playerAttack(player).typeHolder(), player, player, this.position());
        } else if (attacker != null) {
            source = new DamageSource(this.damageSources().mobAttack(attacker).typeHolder(), attacker, attacker, this.position());
        }
        return source;
    }

    public List<Vec3> getSegmentPoints(Vec3 head) {
        List<Vec3> points = new ArrayList<>();
        points.add(head);
        double spacing = this.getSpacing();
        double needed = spacing;
        Vec3 previous = head;
        int index = 0;
        while (points.size() < 3 && index < this.trail.size()) {
            Vec3 next = this.trail.get(index);
            double length = previous.distanceTo(next);
            if (length >= needed) {
                previous = previous.add(next.subtract(previous).scale(needed / length));
                points.add(previous);
                needed = spacing;
            } else {
                needed -= length;
                previous = next;
                index++;
            }
        }
        Vec3 back = this.getDeltaMovement().normalize().scale(-needed);
        while (points.size() < 3) {
            previous = previous.add(back);
            points.add(previous);
            back = back.normalize().scale(spacing);
        }
        return points;
    }

    public enum Type {
        SCREAM(new float[]{0.45F, 0.6F, 0.75F, 0.9F, 1.05F, 1.2F}, new float[]{1.6F, 1.35F, 1.1F, 0.85F, 0.6F, 0.35F}, new float[]{6.0F, 4.0F, 4.0F, 2.0F, 2.0F, 1.0F}, 6, 300, SoundEvents.PHANTOM_AMBIENT, SoundSource.HOSTILE, 1.2F),
        FORK(new float[]{0.9F, 0.97F, 1.04F, 1.11F, 1.18F, 1.25F, 1.32F, 1.4F}, new float[]{1.6F, 1.42F, 1.24F, 1.06F, 0.88F, 0.7F, 0.52F, 0.35F}, new float[]{8.0F, 8.0F, 6.0F, 6.0F, 4.0F, 4.0F, 2.0F, 2.0F}, 8, 300, SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.6F),
        SPLIT(new float[]{0.6F}, new float[]{1.0F, 0.66F, 0.33F}, new float[]{8.0F, 6.0F, 4.0F}, 3, 100, SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.6F);

        private final float[] speeds;
        private final float[] sizes;
        private final float[] damages;
        private final int maxBounces;
        private final int lifetime;
        private final SoundEvent echo;
        private final SoundSource soundSource;
        private final float pitch;

        Type(float[] speeds, float[] sizes, float[] damages, int maxBounces, int lifetime, SoundEvent echo, SoundSource soundSource, float pitch) {
            this.speeds = speeds;
            this.sizes = sizes;
            this.damages = damages;
            this.maxBounces = maxBounces;
            this.lifetime = lifetime;
            this.echo = echo;
            this.soundSource = soundSource;
            this.pitch = pitch;
        }

        private static float stage(float[] values, int phase) {
            return values[Mth.clamp(phase, 0, values.length - 1)];
        }
    }
}
