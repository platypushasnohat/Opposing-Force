package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.LightDependentTargetGoal;
import com.barl_inc.opposing_force.entity.ai.goal.TarantulaAttackGoal;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.TamedSitGoal;
import com.platypushasnohat.sinew.entity.base.TamableMonster;
import com.platypushasnohat.sinew.entity.utils.KeybindUsingMount;
import com.platypushasnohat.sinew.network.MountedEntityKeyPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class Tarantula extends TamableMonster implements KeybindUsingMount, PlayerRideableJumping {

    public static final int ATTACK_ANIMATION = 1;
    public static final int SLAM_ANIMATION = 2;
    public static final int JUMP_ANIMATION = 3;

    public final SmoothAnimationState sitAnimationState = new SmoothAnimationState(0.25F);
    public final SmoothAnimationState jumpAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attack1AnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attack2AnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slamAnimationState = new SmoothAnimationState();

    private boolean attackAlt = false;

    private boolean hasJumped;
    private int riddenJumpCooldown = 0;
    private float playerJumpPendingScale;

    private int riddenAttackCooldown = 0;
    private int riddenAttackTimer = 0;

    public Tarantula(EntityType<? extends Tarantula> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 25;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 140.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 8.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TamedSitGoal(this));
        this.goalSelector.addGoal(3, new TarantulaAttackGoal(this));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Armadillo.class, 6.0F, 1.0D, 1.2D, (entity) -> !((Armadillo) entity).isScared()));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.2D, 8.0F, 3.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && Tarantula.this.getCommand() == COMMAND_FOLLOW;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && Tarantula.this.getCommand() == COMMAND_FOLLOW;
            }
        });
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.2D, (stack) -> stack.is(Items.SPIDER_EYE), false) {
            @Override
            public boolean canUse() {
                return super.canUse() && Tarantula.this.hasNoTargets();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && Tarantula.this.hasNoTargets();
            }
        });
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new LightDependentTargetGoal<>(this, Player.class, true, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Tarantula.this.isTame();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !Tarantula.this.isTame();
            }
        });
        this.targetSelector.addGoal(5, new LightDependentTargetGoal<>(this, IronGolem.class, true, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Tarantula.this.isTame();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !Tarantula.this.isTame();
            }
        });
    }

    public AABB getSwipeAttackBox() {
        return this.getBoundingBox().move(this.getLookAngle().normalize().multiply(2.3D, 0.0D, 2.3D)).inflate(0.1D, -0.25D, 0.1D);
    }

    public AABB getSlamAttackBox() {
        return this.getBoundingBox().move(this.getLookAngle().normalize().multiply(2.2D, 0.0D, 2.2D)).inflate(1.3D, -0.25D, 1.3D);
    }

    public void hurtNearbyEntities(AABB aabb, float knockbackMultiplier, float damageMultiplier, long targetLimit, boolean disableShield) {
        List<LivingEntity> nearbyEntities = this.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this, aabb);
        if (!nearbyEntities.isEmpty()) {
            nearbyEntities.stream().filter(entity -> entity != this && !entity.isAlliedTo(this) && this.getControllingPassenger() != entity).limit(targetLimit).forEach(entity -> {
                DamageSource damageSource = this.damageSources().mobAttack(this);
                entity.hurt(damageSource, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMultiplier);
                float yawRad = this.getYRot() * Mth.DEG_TO_RAD;
                entity.knockback(this.getKnockback(this, damageSource) * knockbackMultiplier, Mth.sin(yawRad), -Mth.cos(yawRad));
                if (disableShield && entity.isDamageSourceBlocked(damageSource) && entity instanceof Player player) {
                    player.disableShield();
                    player.knockback((this.getKnockback(this, damageSource) * knockbackMultiplier) * 0.75F, Mth.sin(yawRad), (-Mth.cos(yawRad)));
                    player.hurtMarked = true;
                }
            });
        }
    }

    private void tickPlayerAttack() {
        if (!this.level().isClientSide) {
            if (this.hasControllingPassenger()) {
                if (this.getAnimationState() == ATTACK_ANIMATION && this.riddenAttackTimer == 7) {
                    this.hurtNearbyEntities(this.getSwipeAttackBox(), 1.0F, 1.0F, 3, false);
                }
                else if (this.getAnimationState() == SLAM_ANIMATION && this.riddenAttackTimer == 17) {
                    this.hurtNearbyEntities(this.getSlamAttackBox(), 1.5F, 1.5F, 8, true);
                }
            }
        }
        else {
            Player player = Sinew.PROXY.getClientSidePlayer();
            if (player != null && player.isPassengerOfSameVehicle(this) && this.riddenAttackCooldown <= 0) {
                if (Sinew.PROXY.isKeyDown(2) && this.getAnimationState() == 0) {
                    PacketDistributor.sendToServer(new MountedEntityKeyPacket(this.getId(), player.getId(), 2));
                }
            }
        }
    }

    private boolean isInAttackPose() {
        return this.getAnimationState() == ATTACK_ANIMATION || this.getAnimationState() == SLAM_ANIMATION;
    }

    private boolean hasNoTargets() {
        return this.getTarget() == null && this.getLastHurtByMob() == null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.isTame()) {
            if (player.isShiftKeyDown() && this.getOwner() == player) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 4) {
                    this.setCommand(COMMAND_SIT);
                }
                player.displayClientMessage(Component.translatable("entity.sinew.all.command_" + this.getCommand(), this.getName()), true);
            }
            else {
                player.startRiding(this);
            }
            return InteractionResult.SUCCESS;
        }
        else {
            if (!this.level().isClientSide && itemStack.is(Items.SPIDER_EYE) && this.hasNoTargets()) {
                this.tryToTame(player, itemStack, 20, 1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (keyPresser.isPassengerOfSameVehicle(this)) {
            if (type == 2) {
                if (this.getAnimationState() == 0 && this.riddenAttackTimer <= 0 && this.riddenAttackCooldown <= 0) {
                    if (this.getRandom().nextFloat() <= 0.75F) {
                        this.setAnimationState(ATTACK_ANIMATION);
                    } else {
                        this.setAnimationState(SLAM_ANIMATION);
                    }
                }
            }
        }
    }

    private void executeRidersJump(float playerJumpPendingScale) {
        double movementFactor = this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double) this.getBlockSpeedFactor();
        double scale = (14.5F * playerJumpPendingScale) * movementFactor;
        double jumpHeight = (double) (2.0F * playerJumpPendingScale) * this.getJumpPower();
        this.addDeltaMovement(this.getLookAngle().multiply(1.0D, 0.0D, 1.0D).normalize().scale(scale).add(0.0D, jumpHeight, 0.0D));
        this.riddenJumpCooldown = 60;
        this.setAnimationState(JUMP_ANIMATION);
        this.hasImpulse = true;
        this.hasJumped = true;
        CommonHooks.onLivingJump(this);
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        Vec2 rotation = this.getRiddenRotation(player);
        this.setRot(rotation.y, rotation.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.onGround() && this.playerJumpPendingScale > 0.0F && !this.isInAttackPose() && !this.jumping) {
            this.executeRidersJump(this.playerJumpPendingScale);
        }
        this.playerJumpPendingScale = 0.0F;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        if (this.onGround() && this.isInAttackPose()) {
            return Vec3.ZERO;
        } else {
            float xMov = player.xxa * 0.75F;
            float zMov = player.zza;
            if (zMov <= 0.0F) {
                zMov *= 0.75F;
            }
            return new Vec3(xMov, 0.0F, zMov);
        }
    }

    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.75F;
    }

    @Override
    public int getJumpCooldown() {
        return this.riddenJumpCooldown;
    }

    @Override
    public void onPlayerJump(int jumpAmount) {
        if (this.riddenJumpCooldown <= 0 && this.onGround()) {
            this.playerJumpPendingScale = this.getPlayerJumpPendingScale(jumpAmount);
        }
    }

    private float getPlayerJumpPendingScale(int jumpAmount) {
        return jumpAmount >= 90 ? 1.0F : 0.4F + 0.4F * (float) jumpAmount / 90.0F;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int jumpPower) {
    }

    @Override
    public void handleStopJump() {
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof Player player) {
            return player;
        }
        return super.getControllingPassenger();
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        if (passenger instanceof LivingEntity living) {
            living.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return super.hurt(source, amount * 2.0F);
        } else {
            return super.hurt(source, amount);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 6;
    }

    // Override these so the rider doesn't take fall damage
    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        float[] onLivingFall = CommonHooks.onLivingFall(this, fallDistance, damageMultiplier);
        fallDistance = onLivingFall[0];
        damageMultiplier = onLivingFall[1];
        boolean flag = causeInternalFallDamage(fallDistance, damageMultiplier, damageSource);
        int i = this.calculateFallDamage(fallDistance, damageMultiplier);
        if (i > 0) {
            this.playSound(i > 4 ? this.getFallSounds().big() : this.getFallSounds().small(), 1.0F, 1.0F);
            this.playBlockFallSound();
            this.hurt(damageSource, (float)i);
            return true;
        } else {
            return flag;
        }
    }

    private boolean causeInternalFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        float[] onLivingFall = CommonHooks.onLivingFall(this, fallDistance, damageMultiplier);
        fallDistance = onLivingFall[0];
        damageMultiplier = onLivingFall[1];
        int i = this.calculateFallDamage(fallDistance, damageMultiplier);
        if (i > 0) {
            this.playBlockFallSound();
            this.hurt(damageSource, (float)i);
            return true;
        } else {
            return this.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if ((this.onGround() || this.isInLiquid() || this.isPassenger()) && this.getAnimationState() == JUMP_ANIMATION && !this.hasJumped) {
            this.setAnimationState(0);
        }
        if (this.hasJumped) {
            this.hasJumped = false;
        }
        if (this.riddenJumpCooldown > 0) {
            this.riddenJumpCooldown--;
        }

        if (this.riddenAttackTimer > 0) {
            this.riddenAttackTimer--;
        }
        if (this.riddenAttackTimer <= 0 && this.isInAttackPose()) {
            this.riddenAttackCooldown = this.getAnimationState() == SLAM_ANIMATION ? 25 : 15;
            this.setAnimationState(0);
        }
        if (this.riddenAttackCooldown > 0) {
            this.riddenAttackCooldown--;
        }
        this.tickPlayerAttack();

        if (!this.isTame() && !this.hasNoTargets() && this.getTameAttempts() > 0) {
            this.setTameAttempts(0);
        }

        if (!this.level().isClientSide && this.getHealth() < this.getMaxHealth() && this.isTame() && this.tickCount % 100 == 0) {
            this.heal(2.0F);
        }
    }

    @Override
    public void setupAnimationStates() {
        boolean noAnimation = this.getAnimationState() == 0;
        boolean sitting = !this.hasControllingPassenger() && this.getCommand() == COMMAND_SIT;
        this.idleAnimationState.animateWhen(noAnimation && !sitting, this.tickCount);
        this.walkAnimationState.animateWhen(noAnimation && !sitting, this.tickCount);
        this.jumpAnimationState.animateWhen(!this.onGround() && this.getAnimationState() == JUMP_ANIMATION, this.tickCount);
        this.attack1AnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION && !this.attackAlt, this.tickCount);
        this.attack2AnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION && this.attackAlt, this.tickCount);
        this.slamAnimationState.animateWhen(this.getAnimationState() == SLAM_ANIMATION, this.tickCount);
        this.sitAnimationState.animateWhen(sitting && noAnimation, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 6.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (ANIMATION_STATE.equals(key)) {
            if (this.getAnimationState() == ATTACK_ANIMATION) {
                this.attackAlt = this.getRandom().nextBoolean();
                this.riddenAttackTimer = 20;
            }
            else if (this.getAnimationState() == SLAM_ANIMATION) {
                this.riddenAttackTimer = 40;
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 0.7F);
    }
}
