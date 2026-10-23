package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.PhasingWanderGoal;
import com.barl_inc.opposing_force.entity.base.PhasingMonster;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.ai.goal.SpectralStareGoal;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public class Ghost extends PhasingMonster {

    public static final int ATTACK_ANIMATION = 1;
    public static final int ATTACK_2_ANIMATION = 2;

    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState attack2AnimationState = new SmoothAnimationState();

    private int carryCooldown;

    public Ghost(EntityType<? extends Ghost> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GhostAttackGoal(this));
        this.goalSelector.addGoal(2, new GhostCarryGoal(this));
        this.goalSelector.addGoal(3, new PhasingWanderGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new SpectralStareGoal(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (this.random.nextBoolean()) {
            int weapon = this.random.nextInt(3);
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(weapon == 0 ? Items.IRON_SWORD : weapon == 1 ? Items.IRON_AXE : OFItems.SPECTRAL_CLEAVER.get()));
            if (weapon < 2) {
                this.enchantSpawnedWeapon(level, this.random, difficulty);
            }
            this.setDropChance(EquipmentSlot.MAINHAND, 0.5F);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public static boolean checkGhostSpawnRules(EntityType<Ghost> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return (pos.getY() < 40 || level.getBiome(pos).is(Biomes.DARK_FOREST) && random.nextBoolean()) && checkMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (slot.getType() == EquipmentSlot.Type.HAND && !this.level().isClientSide) {
            SpectralSight.refreshTracking(this);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getTarget() != null) {
            this.dropCarried();
        }
        if (this.carryCooldown > 0) {
            this.carryCooldown--;
        }
    }

    public static boolean isTool(ItemStack stack) {
        return stack.getItem() instanceof TieredItem;
    }

    private void dropCarried() {
        ItemStack carried = this.getOffhandItem();
        if (!carried.isEmpty()) {
            this.spawnAtLocation(carried);
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
    }

    private void carry(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem().copy();
        ItemStack taken = stack.split(1);
        if (stack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(stack);
        }
        this.dropCarried();
        this.setItemSlot(EquipmentSlot.OFFHAND, taken);
        this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        this.setPersistenceRequired();
        this.carryCooldown = 100 + this.random.nextInt(100);
    }

    @Override
    public boolean isVisibleWithoutSight(Player viewer) {
        return !this.getMainHandItem().isEmpty() || !this.getOffhandItem().isEmpty() || super.isVisibleWithoutSight(viewer);
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.attack2AnimationState.animateWhen(this.getAnimationState() == ATTACK_2_ANIMATION, this.tickCount);
    }

    private static class GhostAttackGoal extends AttackGoal {

        private final Ghost ghost;
        private int attackCooldown;

        public GhostAttackGoal(Ghost ghost) {
            super(ghost);
            this.ghost = ghost;
        }

        @Override
        public void start() {
            super.start();
            this.attackCooldown = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.ghost.getTarget();
            if (target != null) {
                this.ghost.getNavigation().stop();
                if (this.attackState == 1) {
                    this.tickAttack(target);
                } else {
                    this.lookAtTarget(target, 30.0F, 30.0F);
                    this.ghost.approach(target);
                    if (this.attackCooldown > 0) {
                        this.attackCooldown--;
                    }
                    if (this.ghost.getGap(target) <= 2.0D && this.attackCooldown <= 0) {
                        this.attackState = 1;
                    }
                }
            }
        }

        private void tickAttack(LivingEntity target) {
            this.timer++;
            if (this.timer < 18) {
                this.lookAtTarget(target, 30.0F, 30.0F);
            }
            if (this.timer == 1) {
                this.ghost.setAnimationState(!this.ghost.getMainHandItem().isEmpty() || this.ghost.getRandom().nextBoolean() ? ATTACK_ANIMATION : ATTACK_2_ANIMATION);
            }
            if (this.timer == 18) {
                this.ghost.setDeltaMovement(Vec3.directionFromRotation(0.0F, this.ghost.getYRot()).scale(Math.min(0.4D, this.ghost.getGap(target) * 0.4D)));
            } else if (this.timer > 18) {
                this.ghost.setDeltaMovement(this.ghost.getDeltaMovement().scale(0.6D));
            }
            if (this.timer == 19) {
                this.ghost.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.7F, 1.3F);
            }
            if (this.timer == 21 && this.ghost.getGap(target) < 2.0D && this.ghost.hasLineOfSight(target)) {
                this.ghost.doHurtTarget(target);
            }
            if (this.timer > 40) {
                this.ghost.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown = 10;
                this.attackState = 0;
            }
        }
    }

    private static class GhostCarryGoal extends Goal {

        private final Ghost ghost;
        @Nullable
        private ItemEntity item;
        private int timer;

        public GhostCarryGoal(Ghost ghost) {
            this.ghost = ghost;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.ghost.getTarget() != null || this.ghost.carryCooldown > 0 || this.ghost.getRandom().nextInt(20) != 0) {
                return false;
            }
            this.item = this.findItem();
            return this.item != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.ghost.getTarget() == null && this.item != null && this.item.isAlive() && this.timer < 200;
        }

        @Override
        public void start() {
            this.timer = 0;
        }

        @Override
        public void stop() {
            this.item = null;
            this.ghost.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.item != null) {
                this.timer++;
                this.ghost.getNavigation().moveTo(this.item.getX(), this.item.getY() + 0.5D, this.item.getZ(), 1.0D);
                if (this.ghost.distanceToSqr(this.item) < 2.25D) {
                    this.ghost.carry(this.item);
                    this.item = null;
                }
            }
        }

        @Nullable
        private ItemEntity findItem() {
            boolean carryingTool = isTool(this.ghost.getOffhandItem());
            return this.ghost.level().getEntitiesOfClass(ItemEntity.class, this.ghost.getBoundingBox().inflate(12.0D, 0.0D, 12.0D).expandTowards(0.0D, -16.0D, 0.0D).expandTowards(0.0D, 4.0D, 0.0D), item -> !item.hasPickUpDelay() && (!carryingTool || isTool(item.getItem()))).stream()
                    .min(Comparator.<ItemEntity>comparingInt(item -> isTool(item.getItem()) ? 0 : 1).thenComparingDouble(this.ghost::distanceToSqr))
                    .orElse(null);
        }
    }
}