package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.StalkGoal;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.UnrestrictedBodyRotationControl;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public class Roamer extends AnimatedMonster implements SpectralEntity {

    public static final int LOOK_AROUND_ANIMATION = 1;
    public static final int TWEAKING_ANIMATION = 2;

    public final SmoothAnimationState lookAroundAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState tweakingAnimationState = new SmoothAnimationState();

    private int stareTicks;
    private int lookAwayTicks;
    @Nullable
    private Player starer;

    public Roamer(EntityType<? extends Roamer> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RoamerDisappearGoal(this));
        this.goalSelector.addGoal(2, new RoamerIdleGoal(this, LOOK_AROUND_ANIMATION, 120));
        this.goalSelector.addGoal(2, new RoamerIdleGoal(this, TWEAKING_ANIMATION, 80));
        this.goalSelector.addGoal(3, new StalkGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new UnrestrictedBodyRotationControl(this);
    }

    public static boolean checkRoamerSpawnRules(EntityType<Roamer> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() >= level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) && checkSurfaceMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        Player player = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 32.0D, target -> !target.isSpectator() && !SpectralSight.canPerceive(target, this) && SpectralSight.isLookingAt((Player) target, this));
        if (player != null) {
            this.starer = player;
            this.stareTicks++;
            this.lookAwayTicks = 0;
        } else if (++this.lookAwayTicks > 10) {
            this.stareTicks = 0;
        }
        if (this.level().isDay() && !this.hasCustomName() && !this.isPersistenceRequired()) {
            this.discard();
        }
    }

    public boolean isFresno() {
        return this.getCustomName() != null && this.getCustomName().getString().equalsIgnoreCase("fresno");
    }

    @Override
    public boolean isVisibleWithoutSight(Player viewer) {
        return true;
    }

    @Override
    public int getFadeInTicks() {
        return 0;
    }

    @Override
    public boolean canBeLeashed() {
        return this.hasCustomName();
    }

    @Override
    public boolean canBeHeardBy(Player listener) {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.getAnimationState() == 0 && this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4D ? SoundEvents.ENDERMAN_AMBIENT : null;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.6F;
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.lookAroundAnimationState.animateWhen(this.getAnimationState() == LOOK_AROUND_ANIMATION, this.tickCount);
        this.tweakingAnimationState.animateWhen(this.getAnimationState() == TWEAKING_ANIMATION, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, this.getZ() - this.zo);
        this.walkAnimation.update(Math.min(length * 10.0F, 1.0F), 0.2F);
    }

    private static class RoamerDisappearGoal extends Goal {

        private final Roamer roamer;
        @Nullable
        private LivingEntity swapTarget;
        @Nullable
        private Vec3 hidingSpot;
        private int timer;

        public RoamerDisappearGoal(Roamer roamer) {
            this.roamer = roamer;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return !this.roamer.hasCustomName() && this.roamer.stareTicks >= 6 && this.roamer.starer != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 60 && !this.roamer.getNavigation().isDone();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            Player starer = this.roamer.starer;
            this.timer = 0;
            if (this.roamer.getRandom().nextInt(10) == 0 && !starer.isPassenger() && !starer.isVehicle()) {
                this.swapTarget = starer;
            } else {
                this.swapTarget = this.roamer.level().getEntitiesOfClass(LivingEntity.class, this.roamer.getBoundingBox().inflate(24.0D), this::canSwapWith).stream()
                        .max(Comparator.comparingDouble(this.roamer::distanceToSqr))
                        .orElse(null);
            }
            this.hidingSpot = this.swapTarget == null ? this.findHidingSpot(starer) : null;
            if (this.swapTarget != null || this.hidingSpot != null) {
                this.roamer.getNavigation().stop();
                this.vanish();
            } else {
                Vec3 away = DefaultRandomPos.getPosAway(this.roamer, 16, 7, starer.position());
                if (away != null) {
                    this.roamer.getNavigation().moveTo(away.x, away.y, away.z, 1.4D);
                }
            }
        }

        @Override
        public void stop() {
            this.roamer.stareTicks = 0;
            this.roamer.starer = null;
            this.swapTarget = null;
            this.hidingSpot = null;
        }

        @Override
        public void tick() {
            this.timer++;
        }

        private boolean canSwapWith(LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof Player) && !(entity instanceof AbstractVillager) && !(entity instanceof SpectralEntity) && !entity.isInvisible() && !entity.isInWater() && !entity.isSleeping() && !entity.isPassenger() && !entity.isVehicle() && !entity.getType().is(Tags.EntityTypes.BOSSES);
        }

        private void vanish() {
            if (this.swapTarget != null && this.swapTarget.isAlive()) {
                Vec3 position = this.roamer.position();
                this.roamer.teleportTo(this.swapTarget.getX(), this.swapTarget.getY(), this.swapTarget.getZ());
                this.swapTarget.teleportTo(position.x, position.y, position.z);
                if (this.swapTarget instanceof Mob mob) {
                    mob.getNavigation().stop();
                }
            } else if (this.hidingSpot != null) {
                this.roamer.teleportTo(this.hidingSpot.x, this.hidingSpot.y, this.hidingSpot.z);
            }
        }

        @Nullable
        private Vec3 findHidingSpot(Player starer) {
            Level level = this.roamer.level();
            for (int i = 0; i < 16; i++) {
                int x = this.roamer.getBlockX() + this.roamer.getRandom().nextIntBetweenInclusive(-16, 16);
                int z = this.roamer.getBlockZ() + this.roamer.getRandom().nextIntBetweenInclusive(-16, 16);
                BlockPos pos = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
                Vec3 spot = Vec3.atBottomCenterOf(pos);
                Vec3 eye = spot.add(0.0D, this.roamer.getEyeHeight(), 0.0D);
                boolean hidden = level.clip(new ClipContext(starer.getEyePosition(), eye, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, starer)).getType() != HitResult.Type.MISS;
                if (hidden && spot.distanceToSqr(starer.position()) > 64.0D && level.getFluidState(pos).isEmpty() && level.noCollision(this.roamer, this.roamer.getDimensions(this.roamer.getPose()).makeBoundingBox(spot))) {
                    return spot;
                }
            }
            return null;
        }
    }

    private static class RoamerIdleGoal extends Goal {

        private final Roamer roamer;
        private final int animation;
        private final int duration;
        private int timer;

        public RoamerIdleGoal(Roamer roamer, int animation, int duration) {
            this.roamer = roamer;
            this.animation = animation;
            this.duration = duration;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.roamer.getAnimationState() == 0 && this.roamer.getNavigation().isDone() && this.roamer.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4D && this.roamer.getRandom().nextInt(200) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < this.duration;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.roamer.getNavigation().stop();
            this.roamer.setAnimationState(this.animation);
        }

        @Override
        public void stop() {
            if (this.roamer.getAnimationState() == this.animation) {
                this.roamer.setAnimationState(0);
            }
        }

        @Override
        public void tick() {
            this.timer++;
            int loopTick = this.timer % 40;
            if (this.animation == TWEAKING_ANIMATION && (loopTick == 3 || loopTick == 25)) {
                this.roamer.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 0.8F, 1.4F + this.roamer.getRandom().nextFloat() * 0.3F);
            }
        }
    }
}