package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.LightDependentTargetGoal;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.ai.goal.TamedSitGoal;
import com.platypushasnohat.sinew.entity.base.TamableMonster;
import com.platypushasnohat.sinew.entity.utils.KeybindUsingMount;
import com.platypushasnohat.sinew.network.MountedEntityKeyPacket;
import com.platypushasnohat.sinew.utils.SinewMiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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

    private static final int ATTACK_ANIMATION = 1;
    private static final int SLAM_ANIMATION = 2;
    private static final int JUMP_ANIMATION = 3;

    public final SmoothAnimationState sprintAnimationState = new SmoothAnimationState();
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
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 8.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TamedSitGoal(this));
        this.goalSelector.addGoal(2, new TarantulaAttackGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Armadillo.class, 6.0F, 1.0D, 1.2D, (entity) -> !((Armadillo) entity).isScared()));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new LightDependentTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(2, new LightDependentTargetGoal<>(this, IronGolem.class, true, true));
    }

    private AABB getSwipeAttackBox() {
        return this.getBoundingBox().move(this.getLookAngle().normalize().multiply(2.3D, 0.0D, 2.3D)).inflate(0.1D, -0.25D, 0.1D);
    }

    private AABB getSlamAttackBox() {
        return this.getBoundingBox().move(this.getLookAngle().normalize().multiply(2.2D, 0.0D, 2.2D)).inflate(1.3D, -0.25D, 1.3D);
    }

    private void hurtNearbyEntities(AABB aabb, float knockbackMultiplier, float damageMultiplier, long targetLimit, boolean disableShield) {
        List<LivingEntity> nearbyEntities = this.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this, aabb);
        SinewMiscUtils.outlineBounds(aabb, this.level(), ParticleTypes.END_ROD);
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

    @Override
    public Component getCustomName() {
        return Component.literal("Animation State: " + this.getAnimationState() + ", riddenAttackTimer: " + this.riddenAttackTimer);
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.isTame()) {
            if (player.isShiftKeyDown() && this.getOwner() == player) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
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
            if (!this.level().isClientSide && itemStack.is(Items.SPIDER_EYE)) {
                this.tryToTame(player, itemStack, 64, itemStack.getCount());
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 6;
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
            this.riddenAttackCooldown = this.getAnimationState() == SLAM_ANIMATION ? 30 : 15;
            this.setAnimationState(0);
        }
        if (this.riddenAttackCooldown > 0) {
            this.riddenAttackCooldown--;
        }
        this.tickPlayerAttack();
    }

    @Override
    public void setupAnimationStates() {
        boolean noAnimation = this.getAnimationState() == 0;
        boolean sitting = !this.hasControllingPassenger() && this.getCommand() == COMMAND_SIT;
        this.idleAnimationState.animateWhen(noAnimation && !sitting, this.tickCount);
        this.walkAnimationState.animateWhen(!this.isSprinting() && noAnimation && !sitting, this.tickCount);
        this.sprintAnimationState.animateWhen(this.isSprinting() && noAnimation && !sitting, this.tickCount);
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

    private static class TarantulaAttackGoal extends AttackGoal {

        private final Tarantula tarantula;
        private int attackCooldown;
        private int slamCooldown;
        private int jumpCooldown;

        public TarantulaAttackGoal(Tarantula tarantula) {
            super(tarantula);
            this.tarantula = tarantula;
        }

        @Override
        public void start() {
            super.start();
            this.attackCooldown = 0;
            this.slamCooldown = 50;
            this.jumpCooldown = 20;
        }

        @Override
        public void tick() {
            LivingEntity target = this.tarantula.getTarget();
            if (target != null) {
                double distance = this.tarantula.distanceToSqr(target);
                if (this.attackState != 3) {
                    this.lookAtTarget(target, 10.0F, 10.0F);
                }
                if (this.attackState > 0) {
                    this.tarantula.getNavigation().stop();
                }
                if (this.attackState == 1) {
                    this.tickSwipeAttack();
                }
                else if (this.attackState == 2) {
                    this.tickSlam();
                }
                else if (this.attackState == 3) {
                    this.tickJump(target);
                }
                else {
                    this.lookAtTarget(target, 10.0F, 10.0F);
                    this.tarantula.getNavigation().moveTo(target, 1.3D);
                    if (this.attackCooldown > 0) {
                        this.attackCooldown--;
                    }
                    if (this.slamCooldown > 0) {
                        this.slamCooldown--;
                    }
                    if (this.jumpCooldown > 0) {
                        this.jumpCooldown--;
                    }
                    this.chooseAttackState(target, distance);
                }
            }
        }

        private void chooseAttackState(LivingEntity target, double distance) {
            if (this.attackCooldown <= 0 && this.slamCooldown > 0 && distance <= this.getAttackReachSqr(target, 1.1D)) {
                this.attackState = 1;
            }
            else if (this.slamCooldown <= 0 && distance <= this.getAttackReachSqr(target, 1.2D)) {
                this.attackState = 2;
            }
            else if (this.jumpCooldown <= 0 && distance >= 42 && distance < 100 && this.isWithinYRange(target, 1) && this.isPathClear(target)) {
                this.attackState = 3;
            }
        }

        private void tickSwipeAttack() {
            this.timer++;
            if (this.timer == 1) {
                this.tarantula.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 13) {
                this.tarantula.hurtNearbyEntities(this.tarantula.getSwipeAttackBox(), 1.0F, 1.0F, 3, false);
            }
            if (this.timer > 20) {
                this.tarantula.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown();
                this.attackState = 0;
            }
        }

        private void tickSlam() {
            this.timer++;
            if (this.timer == 1) {
                this.tarantula.setAnimationState(SLAM_ANIMATION);
            }
            if (this.timer == 23) {
                this.tarantula.hurtNearbyEntities(this.tarantula.getSlamAttackBox(), 1.5F, 1.5F, 8, true);
            }
            if (this.timer > 40) {
                this.tarantula.setAnimationState(0);
                this.timer = 0;
                this.slamCooldown = 70 + this.tarantula.getRandom().nextInt(50);
                this.attackState = 0;
            }
        }

        private void tickJump(LivingEntity target) {
            this.timer++;
            if (this.timer <= 14) {
                this.lookAtTarget(target, 25.0F, 25.0F);
            }
            Vec3 deltaMovement = this.tarantula.getDeltaMovement();
            Vec3 jumpVec = new Vec3(target.getX() - this.tarantula.getX(), 0.0F, target.getZ() - this.tarantula.getZ());
            if (jumpVec.lengthSqr() > 1.0E-7D) {
                jumpVec = jumpVec.normalize().scale(1.6D).add(deltaMovement);
            }
            if (this.timer == 15 && this.tarantula.onGround()) {
                this.tarantula.setAnimationState(JUMP_ANIMATION);
                this.tarantula.setDeltaMovement(jumpVec.x, 0.5F, jumpVec.z);
            }
            boolean stopJump = this.tarantula.onGround() || this.tarantula.onClimbable() || this.tarantula.isInWaterOrBubble();
            if (this.timer > 60 || (this.timer > 15 && stopJump)) {
                this.tarantula.setAnimationState(0);
                this.timer = 0;
                this.jumpCooldown = 100 + this.tarantula.getRandom().nextInt(100);
                this.attackCooldown();
                this.slamCooldown = 0;
                this.attackState = 0;
            }
        }

        private void attackCooldown() {
            this.attackCooldown = 10 + this.tarantula.getRandom().nextInt(5);
        }

        private boolean isPathClear(LivingEntity target) {
            double dx = target.getZ() - this.tarantula.getZ();
            double dz = target.getX() - this.tarantula.getX();
            double d2 = dx / dz;
            for (int i = 0; i < 6; i++) {
                double d3 = d2 == 0.0D ? 0.0D : dx * (i / 6.0D);
                double d4 = d2 == 0.0D ? dz * (i / 6.0D) : d3 / d2;
                for (int j = 1; j < 4; j++) {
                    if (!this.tarantula.level().getBlockState(BlockPos.containing(this.tarantula.getX() + d4, this.tarantula.getY() + (double) j, this.tarantula.getZ() + d3)).canBeReplaced()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
