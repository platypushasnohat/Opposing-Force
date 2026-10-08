package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.projectile.AcidCharge;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.SwimmingMoveControl;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.ai.goal.SwimWanderGoal;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothAmphibiousNavigation;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public class Octovine extends AnimatedMonster {

    private static final int BITE_ANIMATION = 1;
    public static final int SWING_ANIMATION = 2;
    public static final int SPIT_ANIMATION = 3;
    public static final int EAT_ANIMATION = 4;
    private static final float SWIM_SPEED_MODIFIER = 1.5F;

    private boolean isLandNavigator;
    private int angryTicks;
    private boolean distractedByMeat;

    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sprintAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState biteAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState swingAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState spitAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState eatAnimationState = new SmoothAnimationState();

    public Octovine(EntityType<? extends Octovine> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
        this.switchNavigator(true);
        this.xpReward = 15;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.OXYGEN_BONUS, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.ARMOR, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new OctovineEatGoal(this));
        this.goalSelector.addGoal(1, new OctovineAttackGoal(this));
        this.goalSelector.addGoal(2, new SwimWanderGoal(this, 1.0D, 30) {
            @Override
            public boolean canUse() {
                return super.canUse() && Octovine.this.isInWater();
            }
            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && Octovine.this.isInWater();
            }
        });
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Octovine.this.isInWater();
            }
            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !Octovine.this.isInWater();
            }
        });
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 50, true, false, this::isPrey));
    }

    public static boolean checkOctovineSpawnRules(EntityType<Octovine> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean peaceful = level.getDifficulty() == Difficulty.PEACEFUL;
        boolean dark = isDarkEnoughToSpawnRequireSkylight(level, pos, random);
        boolean inWater = level.getFluidState(pos).is(FluidTags.WATER);
        boolean ground = checkMobSpawnRules(type, level, reason, pos, random);
        //System.out.println("[Octovine spawn] " + pos + " below=" + level.getBlockState(pos.below()).getBlock() + " peaceful=" + peaceful + " dark=" + dark + " water=" + inWater + " ground=" + ground);
        return !peaceful && dark && (inWater || ground);
    }

    private boolean isPrey(LivingEntity entity) {
        if (this.distractedByMeat) return false;
        if (entity instanceof Enemy || entity instanceof Cow || entity instanceof Squid || entity instanceof Octovine) {
            return false;
        }
        float preySize = entity.getBbWidth() * entity.getBbHeight();
        float ownSize = this.getBbWidth() * this.getBbHeight();
        return preySize < ownSize && this.canAttack(entity);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 6.0F, 1.0F);
        this.walkAnimation.update(speed, 0.5F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.angryTicks > 0) {
            this.angryTicks--;
        }
        this.setSprinting(!this.isInWater() && this.isAggressive() && this.getTarget() != null && !this.getNavigation().isDone());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && this.distractedByMeat && source.getEntity() instanceof LivingEntity) {
            this.angryTicks = 100 + this.random.nextInt(21);
        }
        return hurt;
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(!this.isInWaterOrBubble(), this.tickCount);
        this.walkAnimationState.animateWhen(!this.isInWaterOrBubble() && !this.isSprinting(), this.tickCount);
        this.sprintAnimationState.animateWhen(!this.isInWaterOrBubble() && this.isSprinting(), this.tickCount);
        this.swimAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
        this.biteAnimationState.animateWhen(this.getAnimationState() == BITE_ANIMATION, this.tickCount);
        this.swingAnimationState.animateWhen(this.getAnimationState() == SWING_ANIMATION, this.tickCount);
        this.spitAnimationState.animateWhen(this.getAnimationState() == SPIT_ANIMATION, this.tickCount);
        this.eatAnimationState.animateWhen(this.getAnimationState() == EAT_ANIMATION, this.tickCount);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.BUCKET) && !this.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack filled = ItemUtils.createFilledResult(stack, player, Items.MILK_BUCKET.getDefaultInstance());
            player.setItemInHand(hand, filled);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.lookControl = new LookControl(this);
            this.navigation = this.createNavigation(this.level());
            this.isLandNavigator = true;
        } else {
            this.moveControl = new SwimmingMoveControl(this, 85, 15, SWIM_SPEED_MODIFIER, false);
            this.lookControl = new SmoothSwimmingLookControl(this, 15);
            this.navigation = new SmoothAmphibiousNavigation(this, this.level());
            this.isLandNavigator = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater() && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!this.isInWater() && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            double startY = this.getY();
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.horizontalCollision) {
                Vec3 motion = this.getDeltaMovement();
                if (this.isFree(motion.x, motion.y + 0.6D - this.getY() + startY, motion.z)) {
                    this.setDeltaMovement(motion.x, 0.3D, motion.z);
                }
                else if (this.isEyeInFluid(FluidTags.WATER) && this.isPathFinding()) {
                    this.setDeltaMovement(motion.add(0.0D, 0.05D, 0.0D));
                }
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getFluidState(pos).is(FluidTags.WATER) ? 10.0F : -level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return OFSoundEvents.FURBALL_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return OFSoundEvents.FURBALL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return OFSoundEvents.FURBALL_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.7F;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(OFDamageTypes.ACID) || super.isInvulnerableTo(source);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.1F, 1.2F);
    }

    private static class OctovineAttackGoal extends AttackGoal {
        private final Octovine octovine;
        private int attackCooldown;
        private boolean hasHit;
        private int spitCheckCooldown;

        public OctovineAttackGoal(Octovine octovine) {
            super(octovine);
            this.octovine = octovine;
        }

        @Override
        public boolean canContinueToUse() {
            return this.attackState != 0 || super.canContinueToUse();
        }

        @Override
        public void start() {
            super.start();
            this.attackCooldown = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.octovine.setSprinting(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.octovine.getTarget();
            boolean hasTarget = target != null && target.isAlive();
            if (this.attackState != 0) {
                this.octovine.getNavigation().stop();
                if (hasTarget) {
                    this.lookAtTarget(target, 30.0F, 30.0F);
                }
                if (this.attackState == 1) {
                    this.tickAttack(target, BITE_ANIMATION, 8, 12);
                } else if (this.attackState == 2) {
                    this.tickAttack(target, SWING_ANIMATION, 9, 12);
                } else {
                    this.tickSpit(target, 15, 30);
                }
                return;
            }
            if (hasTarget) {
                double distance = this.octovine.distanceToSqr(target);
                this.lookAtTarget(target, 30.0F, 30.0F);
                this.octovine.getNavigation().moveTo(target, this.octovine.isInWater() ? 1.0D : 1.2D);
                if (this.attackCooldown > 0) {
                    this.attackCooldown--;
                }
                if (this.spitCheckCooldown > 0) {
                    this.spitCheckCooldown--;
                }
                if (this.attackCooldown <= 0 && distance <= this.getAttackReachSqr(target, 1.55D)) {
                    this.attackState = this.octovine.getRandom().nextBoolean() ? 1 : 2;
                } else if (this.attackCooldown <= 0 && this.spitCheckCooldown <= 0 && distance > 5.25D && distance < 256.0D && !this.octovine.isUnderWater() && this.octovine.hasLineOfSight(target)) {
                    this.spitCheckCooldown = 20;
                    if (this.octovine.getRandom().nextInt(3) == 0) {
                        this.attackState = 3;
                    }
                }
            }
        }

        private void tickAttack(LivingEntity target, int animation, int hitStart, int hitEnd) {
            this.timer++;
            if (this.timer == 1) {
                this.octovine.setAnimationState(animation);
                this.hasHit = false;
            }
            if (!this.hasHit && target != null && target.isAlive() && this.timer >= hitStart && this.timer <= hitEnd && this.isInAttackRange(target, 0.65D)) {
                float damage = (float) this.octovine.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (this.octovine.angryTicks > 0) {
                    damage *= 2.0F;
                }
                target.hurt(this.octovine.damageSources().mobAttack(this.octovine), damage);
                this.hasHit = true;
            }
            if (this.timer > 15) {
                this.octovine.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown = 10 + this.octovine.getRandom().nextInt(10);
                this.attackState = 0;
            }
        }

        private void tickSpit(LivingEntity target, int fireTick, int endTick) {
            this.timer++;
            if (this.timer == 1) {
                this.octovine.setAnimationState(SPIT_ANIMATION);
                this.hasHit = false;
            }
            if (!this.hasHit && this.timer >= fireTick && target != null && target.isAlive()) {
                this.spitBile(target);
                this.hasHit = true;
            }
            if (this.timer > endTick) {
                this.octovine.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown = 20 + this.octovine.getRandom().nextInt(20);
                this.attackState = 0;
            }
        }

        private void spitBile(LivingEntity target) {
            Level level = this.octovine.level();
            AcidCharge charge = new AcidCharge(level, this.octovine);
            Vec3 mouth = this.octovine.position().add(new Vec3(0.0D, this.octovine.getEyeHeight() - 0.2D, 1.0D).yRot(-this.octovine.getYHeadRot() * Mth.DEG_TO_RAD));
            charge.setPos(mouth.x, mouth.y, mouth.z);
            double dx = target.getX() - charge.getX();
            double dy = target.getY(0.3333D) - charge.getY();
            double dz = target.getZ() - charge.getZ();
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            float inaccuracy = 1.0F + (float) horizontal * 0.6F;
            float speed = 1.0F;
            double lift = 0.0D;
            for (int i = 0; i < 3; i++) {
                double aimY = dy + lift;
                double flightTicks = Math.sqrt(horizontal * horizontal + aimY * aimY) / speed;
                lift = 0.55D * 0.03D * flightTicks * flightTicks;
            }
            charge.shoot(dx, dy + lift, dz, speed, inaccuracy);
            level.playSound(null, this.octovine.getX(), this.octovine.getY(), this.octovine.getZ(), SoundEvents.LLAMA_SPIT, this.octovine.getSoundSource(), 1.0F, 0.8F + this.octovine.getRandom().nextFloat() * 0.2F);
            level.addFreshEntity(charge);
        }
    }

    private static class OctovineEatGoal extends Goal {

        private final Octovine octovine;
        private ItemEntity meat;
        private int eatTimer;
        private int searchCooldown;
        private int stuckTicks;
        private ItemEntity ignoredMeat;

        public OctovineEatGoal(Octovine octovine) {
            this.octovine = octovine;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.octovine.angryTicks > 0 || this.octovine.getAnimationState() != 0) {
                return false;
            }
            if (this.searchCooldown > 0) {
                this.searchCooldown--;
                return false;
            }
            this.searchCooldown = 10;
            this.meat = this.findMeat();
            return this.meat != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.octovine.angryTicks <= 0 && this.meat != null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.octovine.distractedByMeat = true;
            this.octovine.setTarget(null);
            this.octovine.setAggressive(false);
            this.eatTimer = 0;
        }

        @Override
        public void stop() {
            this.octovine.distractedByMeat = false;
            this.meat = null;
            this.eatTimer = 0;
            this.octovine.getNavigation().stop();
            if (this.octovine.getAnimationState() == EAT_ANIMATION) {
                this.octovine.setAnimationState(0);
            }
        }

        @Override
        public void tick() {
            if (this.meat == null || !this.meat.isAlive() || this.meat.getItem().isEmpty()) {
                this.meat = this.findMeat();
                this.eatTimer = 0;
                this.stuckTicks = 0;
                this.octovine.setAnimationState(0);
                return;
            }
            this.octovine.getLookControl().setLookAt(this.meat, 30.0F, 30.0F);
            boolean close = this.isClose();
            if (this.eatTimer > 0) {
                this.octovine.getNavigation().stop();
                this.eatTimer--;
                if (this.eatTimer == 0) {
                    this.eatOne();
                    if (!this.meat.getItem().isEmpty() && close) {
                        this.eatTimer = this.rollEatTime();
                    } else {
                        this.octovine.setAnimationState(0);
                    }
                }
            } else if (close) {
                this.stuckTicks = 0;
                this.octovine.getNavigation().stop();
                this.octovine.setAnimationState(EAT_ANIMATION);
                this.eatTimer = this.rollEatTime();
            } else {
                if (this.octovine.getNavigation().isDone()) {
                    if (this.octovine.distanceToSqr(this.meat) < 16.0D) {
                        this.octovine.getMoveControl().setWantedPosition(this.meat.getX(), this.meat.getY(), this.meat.getZ(), this.octovine.isInWater() ? 1.0D : 1.3D);
                    } else {
                        this.octovine.getNavigation().moveTo(this.meat, this.octovine.isInWater() ? 1.0D : 1.3D);
                    }
                }
                if (++this.stuckTicks > 80) {
                    this.ignoredMeat = this.meat;
                    this.meat = this.findMeat();
                    this.stuckTicks = 0;
                }
            }
        }

        private boolean isClose() {
            double dx = this.meat.getX() - this.octovine.getX();
            double dz = this.meat.getZ() - this.octovine.getZ();
            double reach = this.octovine.getBbWidth() / 2.0D + 1.5D;
            return dx * dx + dz * dz < reach * reach && Math.abs(this.meat.getY() - this.octovine.getY()) < 2.0D;
        }

        private int rollEatTime() {
            return 40 + this.octovine.getRandom().nextInt(41);
        }

        private void eatOne() {
            this.meat.getItem().shrink(1);
            if (this.meat.getItem().isEmpty()) {
                this.meat.discard();
            }
            this.octovine.playSound(SoundEvents.GENERIC_EAT, 1.0F, 0.8F + this.octovine.getRandom().nextFloat() * 0.4F);
        }

        private ItemEntity findMeat() {
            return this.octovine.level().getEntitiesOfClass(ItemEntity.class, this.octovine.getBoundingBox().inflate(16.0D, 8.0D, 16.0D),
                            item -> item.isAlive() && item.getItem().is(Tags.Items.FOODS_RAW_MEAT) && this.octovine.hasLineOfSight(item))
                    .stream()
                    .min(Comparator.comparingDouble(this.octovine::distanceToSqr))
                    .orElse(null);
        }
    }
}
