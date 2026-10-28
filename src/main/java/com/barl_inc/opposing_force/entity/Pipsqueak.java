package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.FlyingWanderGoal;
import com.barl_inc.opposing_force.entity.base.BasicFlyingMonster;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class Pipsqueak extends BasicFlyingMonster {

    private static final EntityDataAccessor<Boolean> HANGING = SynchedEntityData.defineId(Pipsqueak.class, EntityDataSerializers.BOOLEAN);

    public final SmoothAnimationState hangingAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState closeMouthAnimationState = new SmoothAnimationState();

    private int hangTicks;
    private float tilt;
    private float prevTilt;
    private float bank;
    private float prevBank;

    public Pipsqueak(EntityType<? extends Pipsqueak> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 8;
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PipsqueakHangGoal(this));
        this.goalSelector.addGoal(2, new FlyingWanderGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    public static boolean checkPipsqueakSpawnRules(EntityType<Pipsqueak> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() < 40 && checkUndergroundMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HANGING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Hanging", this.isHanging());
        compound.putInt("HangTicks", this.hangTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(HANGING, compound.getBoolean("Hanging"));
        this.hangTicks = compound.getInt("HangTicks");
    }

    @Override
    public void tick() {
        if (this.isHanging()) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        super.tick();
        if (this.level().isClientSide) {
            this.tickTilt();
        }
    }

    private void tickTilt() {
        this.prevTilt = this.tilt;
        this.prevBank = this.bank;
        float targetTilt = 0.0F;
        float targetBank = 0.0F;
        if (!this.isHanging()) {
            float speed = (float) Math.sqrt(Mth.square(this.getX() - this.xo) + Mth.square(this.getZ() - this.zo));
            targetTilt = Math.min(speed * 150.0F, 20.0F);
            targetBank = Mth.clamp(Mth.wrapDegrees(this.yBodyRot - this.yBodyRotO) * -3.0F, -30.0F, 30.0F);
        }
        this.tilt = Mth.lerp(0.2F, this.tilt, targetTilt);
        this.bank = Mth.lerp(0.2F, this.bank, targetBank);
    }

    public float getTilt(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevTilt, this.tilt);
    }

    public float getBank(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevBank, this.bank);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isHanging()) {
            this.hangTicks--;
            if (this.hangTicks <= 0 || this.getTarget() != null || !this.canHangAt(this.position())) {
                this.stopHanging();
            }
        }
    }

    public boolean isHanging() {
        return this.entityData.get(HANGING);
    }

    private void startHanging(Vec3 spot) {
        this.setPos(spot);
        this.setDeltaMovement(Vec3.ZERO);
        this.setNoGravity(true);
        this.getNavigation().stop();
        this.hangTicks = 600 + this.random.nextInt(1200);
        this.entityData.set(HANGING, true);
    }

    private void stopHanging() {
        this.hangTicks = 0;
        this.entityData.set(HANGING, false);
    }

    @Nullable
    private Vec3 findHangSpot() {
        Vec3 spot = null;
        for (int attempt = 0; attempt < 10 && spot == null; attempt++) {
            BlockPos column = this.blockPosition().offset(this.random.nextInt(17) - 8, 0, this.random.nextInt(17) - 8);
            spot = this.findCeilingAbove(column);
        }
        return spot;
    }

    @Nullable
    private Vec3 findCeilingAbove(BlockPos column) {
        Vec3 spot = null;
        BlockPos.MutableBlockPos pos = column.mutable();
        boolean searching = this.level().getBlockState(pos).getCollisionShape(this.level(), pos).isEmpty();
        for (int up = 0; up < 10 && searching; up++) {
            pos.move(Direction.UP);
            if (!this.level().getBlockState(pos).getCollisionShape(this.level(), pos).isEmpty()) {
                Vec3 candidate = new Vec3(pos.getX() + 0.5D, pos.getY() - this.getBbHeight(), pos.getZ() + 0.5D);
                if (this.canHangAt(candidate)) {
                    spot = candidate;
                }
                searching = false;
            }
        }
        return spot;
    }

    private boolean canHangAt(Vec3 spot) {
        BlockPos ceiling = BlockPos.containing(spot.x, spot.y + this.getBbHeight() + 0.5D, spot.z);
        return this.level().getBlockState(ceiling).isFaceSturdy(this.level(), ceiling, Direction.DOWN) && this.level().noCollision(this, this.getDimensions(this.getPose()).makeBoundingBox(spot));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && this.isHanging()) {
            this.stopHanging();
        }
        return hurt;
    }

    @Override
    public boolean isFlying() {
        return !this.isHanging() && super.isFlying();
    }

    @Override
    public boolean isPushable() {
        return !this.isHanging() && super.isPushable();
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(!this.isHanging(), this.tickCount);
        this.flyAnimationState.animateWhen(!this.isHanging(), this.tickCount);
        this.hangingAnimationState.animateWhen(this.isHanging(), this.tickCount);
        this.closeMouthAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
    }

    private static class PipsqueakHangGoal extends Goal {

        private final Pipsqueak pipsqueak;
        @Nullable
        private Vec3 spot;
        private int timer;

        public PipsqueakHangGoal(Pipsqueak pipsqueak) {
            this.pipsqueak = pipsqueak;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            boolean canUse = this.pipsqueak.isHanging();
            if (!canUse && this.pipsqueak.getTarget() == null && this.pipsqueak.getRandom().nextInt(200) == 0) {
                this.spot = this.pipsqueak.findHangSpot();
                canUse = this.spot != null;
            }
            return canUse;
        }

        @Override
        public boolean canContinueToUse() {
            return this.pipsqueak.isHanging() || (this.spot != null && this.timer < 200 && this.pipsqueak.getTarget() == null);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.timer = 0;
            if (this.spot != null) {
                this.pipsqueak.getNavigation().moveTo(this.spot.x, this.spot.y, this.spot.z, 1.0D);
            }
        }

        @Override
        public void stop() {
            this.spot = null;
            this.pipsqueak.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (!this.pipsqueak.isHanging() && this.spot != null) {
                this.timer++;
                if (!this.pipsqueak.canHangAt(this.spot)) {
                    this.spot = null;
                } else if (this.pipsqueak.distanceToSqr(this.spot) < 0.36D) {
                    this.pipsqueak.startHanging(this.spot);
                    this.spot = null;
                } else if (this.pipsqueak.getNavigation().isDone()) {
                    this.pipsqueak.getMoveControl().setWantedPosition(this.spot.x, this.spot.y, this.spot.z, 1.0D);
                }
            }
        }
    }
}