package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.GrellConjureGoal;
import com.barl_inc.opposing_force.entity.ai.goal.PhasingWanderGoal;
import com.barl_inc.opposing_force.entity.base.BasicFlyingMonster;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import com.platypushasnohat.sinew.registry.SinewAttachments;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Grell extends BasicFlyingMonster implements SpectralEntity {

    public static final int BITE_ANIMATION = 1;
    public static final int CONJURE_ANIMATION = 2;
    public static final int CONJURE_TICKS = 60;

    private static final EntityDataAccessor<CompoundTag> REVEALED = SynchedEntityData.defineId(Grell.class, EntityDataSerializers.COMPOUND_TAG);
    private static final int MATERIALIZE_TICKS = 200;

    public final SmoothAnimationState chargeAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState biteAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState conjureAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState brainAnimationState = new SmoothAnimationState();

    private final Set<UUID> revealed = new HashSet<>();
    private final Object2IntMap<UUID> stares = new Object2IntOpenHashMap<>();
    private boolean staredAt;
    private int conjureCooldown;
    private int materialize;
    private int prevMaterialize;
    private int conjureFade;
    private int prevConjureFade;

    public Grell(EntityType<? extends Grell> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 20;
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.FLYING_SPEED, 0.35F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new GrellFreezeGoal(this));
        this.goalSelector.addGoal(1, new GrellConjureGoal(this));
        this.goalSelector.addGoal(2, new GrellAttackGoal(this));
        this.goalSelector.addGoal(3, new PhasingWanderGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> this.revealed.contains(player.getUUID())));
    }

    public static boolean checkGrellSpawnRules(EntityType<Grell> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() < 40 && checkMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(REVEALED, new CompoundTag());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (REVEALED.equals(accessor) && this.level().isClientSide) {
            this.revealed.clear();
            this.revealed.addAll(readUUIDs(this.entityData.get(REVEALED)));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Revealed", writeUUIDs(this.revealed));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CompoundTag tag = compound.getCompound("Revealed");
        this.revealed.clear();
        this.revealed.addAll(readUUIDs(tag));
        this.entityData.set(REVEALED, tag);
    }

    private static CompoundTag writeUUIDs(Set<UUID> uuids) {
        ListTag list = new ListTag();
        uuids.forEach(uuid -> list.add(NbtUtils.createUUID(uuid)));
        CompoundTag tag = new CompoundTag();
        tag.put("UUIDs", list);
        return tag;
    }

    private static Set<UUID> readUUIDs(CompoundTag tag) {
        Set<UUID> uuids = new HashSet<>();
        tag.getList("UUIDs", Tag.TAG_INT_ARRAY).forEach(uuid -> uuids.add(NbtUtils.loadUUID(uuid)));
        return uuids;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.tickClientMaterialize();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.tickStares();
        if (this.conjureCooldown > 0) {
            this.conjureCooldown--;
        }
    }

    private void tickStares() {
        Set<UUID> before = Set.copyOf(this.stares.keySet());
        List<Player> starers = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(32.0D), this::isStaring);
        Set<UUID> staring = new HashSet<>();
        this.staredAt = !starers.isEmpty();
        for (Player player : starers) {
            staring.add(player.getUUID());
            int stare = this.stares.getInt(player.getUUID()) + 1;
            if (stare >= this.getStareTicks(player)) {
                this.stares.removeInt(player.getUUID());
                this.reveal(player);
                if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && (this.getTarget() == null || !this.getTarget().isAlive())) {
                    this.setTarget(player);
                }
            } else {
                this.stares.put(player.getUUID(), stare);
            }
        }
        for (UUID uuid : Set.copyOf(this.stares.keySet())) {
            int stare = this.stares.getInt(uuid) - 1;
            if (!staring.contains(uuid) && stare <= 0) {
                this.stares.removeInt(uuid);
            } else if (!staring.contains(uuid)) {
                this.stares.put(uuid, stare);
            }
        }
        if (!before.equals(this.stares.keySet())) {
            SpectralSight.refreshTracking(this);
        }
    }

    private boolean isStaring(Player player) {
        return !player.isSpectator() && !this.revealed.contains(player.getUUID()) && this.isLookedAtBy(player);
    }

    private boolean isLookedAtBy(Player player) {
        return player.distanceToSqr(this) <= 1024.0D && SpectralSight.isLookingAt(player, this);
    }

    private int getStareTicks(Player player) {
        int ticks = MATERIALIZE_TICKS;
        if (this.hasSpectralSight(player)) {
            ticks = 60;
        }
        return ticks;
    }

    private boolean hasSpectralSight(Entity entity) {
        return entity.hasData(SinewAttachments.SPECTRAL_SIGHT.get()) && entity.getData(SinewAttachments.SPECTRAL_SIGHT.get()).canSee(this.getType());
    }

    private void reveal(Player player) {
        if (this.revealed.add(player.getUUID())) {
            this.entityData.set(REVEALED, writeUUIDs(this.revealed));
        }
    }

    private boolean isMaterializingFor(Entity viewer) {
        return this.stares.containsKey(viewer.getUUID()) && !this.hasSpectralSight(viewer);
    }

    private void tickClientMaterialize() {
        this.prevMaterialize = this.materialize;
        this.prevConjureFade = this.conjureFade;
        if (this.getAnimationState() == CONJURE_ANIMATION) {
            this.conjureFade = Math.min(this.conjureFade + 1, CONJURE_TICKS);
        } else {
            this.conjureFade = 0;
        }
        Player player = Sinew.PROXY.getClientSidePlayer();
        if (player != null && SpectralSight.canPerceive(player, this)) {
            this.materialize = MATERIALIZE_TICKS;
        } else if (player != null && this.isLookedAtBy(player)) {
            this.materialize = Math.min(this.materialize + 1, MATERIALIZE_TICKS);
        } else {
            this.materialize = Math.max(this.materialize - 1, 0);
        }
    }

    public float getMaterialize(float partialTicks) {
        float conjure = Mth.lerp(partialTicks, this.prevConjureFade, this.conjureFade) / CONJURE_TICKS;
        return Mth.lerp(partialTicks, this.prevMaterialize, this.materialize) / MATERIALIZE_TICKS * (1.0F - conjure);
    }

    public int getConjureCooldown() {
        return this.conjureCooldown;
    }

    public void setConjureCooldown(int conjureCooldown) {
        this.conjureCooldown = conjureCooldown;
    }

    public boolean isConjuring() {
        return this.getAnimationState() == CONJURE_ANIMATION;
    }

    @Override
    public boolean isPerceivedBy(Entity viewer) {
        return this.revealed.contains(viewer.getUUID()) || this.stares.containsKey(viewer.getUUID());
    }

    @Override
    public boolean canBeHurtBy(DamageSource source) {
        boolean materializing = source.getEntity() != null && this.isMaterializingFor(source.getEntity());
        return SpectralEntity.super.canBeHurtBy(source) && !materializing;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = !this.isConjuring() && super.hurt(source, amount);
        if (hurt && source.getEntity() instanceof Player player) {
            this.stares.removeInt(player.getUUID());
            this.reveal(player);
        }
        return hurt;
    }

    @Override
    public void onTargetLostSight(LivingEntity lostTarget) {
    }

    @Override
    public int getFadeInTicks() {
        return 0;
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(true, this.tickCount);
        this.walkAnimationState.animateWhen(!this.isAggressive() && !this.isConjuring(), this.tickCount);
        this.chargeAnimationState.animateWhen(this.isAggressive() && !this.isConjuring(), this.tickCount);
        this.biteAnimationState.animateWhen(this.getAnimationState() == BITE_ANIMATION, this.tickCount);
        this.brainAnimationState.animateWhen(this.isAlive() && this.getAnimationState() == 0, this.tickCount);
        this.conjureAnimationState.animateWhen(this.isConjuring(), this.tickCount);
    }

    private static class GrellFreezeGoal extends Goal {

        private final Grell grell;

        public GrellFreezeGoal(Grell grell) {
            this.grell = grell;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.grell.staredAt && this.grell.getTarget() == null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.grell.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.grell.getMoveControl().setWantedPosition(this.grell.getX(), this.grell.getY(), this.grell.getZ(), 0.0D);
            this.grell.setDeltaMovement(this.grell.getDeltaMovement().scale(0.5D));
        }
    }

    private static class GrellAttackGoal extends AttackGoal {

        private final Grell grell;
        private int biteCooldown;

        public GrellAttackGoal(Grell grell) {
            super(grell);
            this.grell = grell;
        }

        @Override
        public void stop() {
            LivingEntity target = this.grell.getTarget();
            super.stop();
            if (target != null && target.isAlive() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
                this.grell.setTarget(target);
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.grell.getTarget();
            if (target != null) {
                this.lookAtTarget(target, 30.0F, 30.0F);
                this.grell.getNavigation().moveTo(target.getX(), target.getEyeY() - this.grell.getBbHeight() * 0.5D, target.getZ(), 1.6D);
                if (this.attackState == 1) {
                    this.tickBite(target);
                } else if (this.biteCooldown > 0) {
                    this.biteCooldown--;
                } else if (this.grell.distanceToSqr(target) <= this.getAttackReachSqr(target)) {
                    this.attackState = 1;
                }
            }
        }

        private void tickBite(LivingEntity target) {
            this.timer++;
            if (this.timer == 1) {
                this.grell.setAnimationState(BITE_ANIMATION);
            }
            if (this.timer == 5 && this.isInAttackRange(target, 0.7D)) {
                this.grell.doHurtTarget(target);
            }
            if (this.timer >= 7) {
                this.grell.setAnimationState(0);
                this.timer = 0;
                this.attackState = 0;
                this.biteCooldown = 30;
            }
        }
    }
}
