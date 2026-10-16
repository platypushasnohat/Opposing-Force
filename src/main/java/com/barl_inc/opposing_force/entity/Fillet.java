package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.FilletJoinShoalGoal;
import com.barl_inc.opposing_force.entity.ai.goal.FilletSwimGoal;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.SwimmingMoveControl;
import com.platypushasnohat.sinew.entity.ai.goal.AquaticPanicGoal;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothWaterNavigation;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.utils.SwimPitch;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class Fillet extends AnimatedMonster implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Fillet.class, EntityDataSerializers.BOOLEAN);

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState flopAnimationState = new SmoothAnimationState();

    public SwimPitch swimPitch = new SwimPitch(this, 85.0F, 15.0F);

    public float circleSpeed = 1.0F;
    public int baitballCooldown = 200 + this.getRandom().nextInt(200);
    public int circleTime = 0;
    public int maxCircleTime = 300;
    public Vec3 circlePos;
    public Fillet groupLeader;
    public int groupSize = 1;

    public Fillet(EntityType<? extends Fillet> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SwimmingMoveControl(this, 85, 10, 0.02F);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.xpReward = 2;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AquaticPanicGoal(this, 1.7D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Terror.class, 8.0F, 1.7D, 1.7D));
        this.goalSelector.addGoal(3, new FilletSwimGoal(this));
        this.goalSelector.addGoal(4, new FilletJoinShoalGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void doPush(Entity entity) {
        if (entity instanceof LivingEntity livingEntity && this.canAttack(livingEntity) && !(livingEntity instanceof Fillet) && !this.isAlliedTo(livingEntity)) {
            livingEntity.hurt(this.damageSources().thorns(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        } else {
            super.doPush(entity);
        }
    }

    public void leaveGroup() {
        if (this.groupLeader != null) {
            this.groupLeader.decreaseGroupSize();
        }
        this.groupLeader = null;
    }

    public boolean hasNoLeader() {
        return !this.hasGroupLeader();
    }

    public boolean hasGroupLeader() {
        return this.groupLeader != null && this.groupLeader.isAlive();
    }

    private void increaseGroupSize() {
        this.groupSize++;
    }

    private void decreaseGroupSize() {
        this.groupSize--;
    }

    public boolean canGroupGrow() {
        return this.isGroupLeader() && this.groupSize < this.getMaxShoalSize() && this.getLastHurtByMob() == null;
    }

    public int getMaxShoalSize() {
        return 10;
    }

    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    public boolean isGroupLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.distanceToSqr(this.groupLeader) <= 121.0D;
    }

    public void moveToGroupLeader() {
        if (this.hasGroupLeader()) {
            this.getNavigation().moveTo(this.groupLeader.getX(), this.groupLeader.getY(), this.groupLeader.getZ(), 1.0D);
        }
    }

    public boolean isCircling() {
        return this.circlePos != null && this.circleTime < this.maxCircleTime;
    }

    public void createAndSetLeader(Fillet leader) {
        this.groupLeader = leader;
        leader.increaseGroupSize();
    }

    public void addFollowers(Stream<? extends Fillet> stream) {
        stream.limit(this.getMaxShoalSize() - this.groupSize)
                .filter(fish -> fish != this)
                .forEach(fish -> fish.createAndSetLeader(this));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(OFItems.FILLET_BUCKET.get());
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return this.getMaxShoalSize();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothWaterNavigation(this, level, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWaterOrBubble()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.horizontalCollision && this.isEyeInFluid(FluidTags.WATER) && this.isPathFinding()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.swimPitch.tick();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isInWater() && this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add((this.getRandom().nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5D, (this.getRandom().nextFloat() * 2.0F - 1.0F) * 0.2F));
            if (this.getRandom().nextFloat() < 0.25F) {
                this.setYRot(this.getRandom().nextFloat() * 360.0F);
            }
            this.playSound(SoundEvents.COD_FLOP, this.getSoundVolume(), this.getVoicePitch());
        }
        if (!this.level().isClientSide && this.baitballCooldown > 0) {
            this.baitballCooldown--;
        }
    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(i);
    }

    protected void handleAirSupply(int airSupply) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.isInWater(), this.tickCount);
        this.swimAnimationState.animateWhen(this.isInWater(), this.tickCount);
        this.flopAnimationState.animateWhen(!this.isInWater(), this.tickCount);
    }

    @Override
    public boolean canDrownInFluidType(FluidType fluidType) {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        float speed = Math.min(length * 10.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
    }

    @SuppressWarnings("unused")
    public static boolean checkFilletSpawnRules(EntityType<Fillet> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() <= level.getSeaLevel() - 33 && level.getRawBrightness(pos, 0) == 0 && level.getBlockState(pos).is(Blocks.WATER);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (spawnGroupData == null) {
            spawnGroupData = new ShoalSpawnGroupData(this);
        } else {
            ShoalSpawnGroupData data = (ShoalSpawnGroupData) spawnGroupData;
            this.createAndSetLeader(data.leader());
        }
        return spawnGroupData;
    }

    public record ShoalSpawnGroupData(Fillet leader) implements SpawnGroupData {
    }
}
