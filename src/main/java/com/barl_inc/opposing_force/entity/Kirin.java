package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.base.SummonableMount;
import com.platypushasnohat.sinew.entity.utils.BodyChain;
import com.platypushasnohat.sinew.entity.utils.BodyChainMob;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class Kirin extends SummonableMount implements BodyChainMob {

    public static final int IDLE_TWITCH_ONE_ANIMATION = 1;
    public static final int IDLE_TWITCH_TWO_ANIMATION = 2;
    public static final int IDLE_TWITCH_THREE_ANIMATION = 3;
    public static final int IDLE_SHAKE_ONE_ANIMATION = 4;
    public static final int IDLE_SHAKE_TWO_ANIMATION = 5;
    public static final int DEATH_ANIMATION = 6;

    public static final float DEATH_GLOW_START = 1.3F;
    public static final float DEATH_LENGTH = 2.0F;

    private static final int[] IDLE_BLEND_DURATIONS = {20, 20, 25, 20, 20};
    private static final int IDLE_BLEND_COOLDOWN = 200;

    private static final float[] STEP_TIMES = {0.0F, 0.5F, 1.0F, 1.5F};
    private static final float[][] FOOT_POSITIONS = {{2.0F, -19.5F}, {2.0F, -0.5F}, {-2.0F, -19.5F}, {-2.0F, -0.5F}};
    private static final float[][] IDLE_FOOT_POSITIONS = {{2.5F, -4.4F}, {2.5F, 2.7F}, {-2.5F, -2.4F}, {-2.5F, 6.6F}};

    public final SmoothAnimationState idleTwitchOneAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleTwitchTwoAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleTwitchThreeAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleShakeOneAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleShakeTwoAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState deathAnimationState = new SmoothAnimationState(1.0F);

    private final BodyChain bodyChain = new BodyChain(0.5F, 0.0F, 0.0F, 0.0F, new float[]{0.5F, 0.25F, 0.3F, 0.35F}, new float[]{0.0F, 0.0F, 0.0F, 0.0F});

    private int idleBlendCooldown;
    private int deathTicks;
    @Nullable
    private DamageSource deathSource;
    private float prevWalkTime;

    public Kirin(EntityType<? extends Kirin> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 30;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 275.0D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(SinewAttributes.PROJECTILE_RESISTANCE, 0.2D)
                .add(SinewAttributes.MAGIC_RESISTANCE, 0.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new KirinIdleBlendGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("IdleBlendCooldown", this.idleBlendCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.idleBlendCooldown = compound.getInt("IdleBlendCooldown");
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.isDeadOrDying()) {
            this.bodyChain.tick(this.yBodyRot, 0.0F, 0.0F);
            this.tickFootsteps();
            if (this.walkAnimation.speed() < 0.15F) {
                this.spawnHoverSmoke();
            }
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        this.setAnimationState(DEATH_ANIMATION);
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel level, DamageSource damageSource) {
        this.deathSource = damageSource;
    }

    @Override
    protected void tickDeath() {
        this.deathTicks++;
        this.hurtTime = 0;
        if (this.level().isClientSide && this.deathTicks == Mth.ceil(DEATH_GLOW_START * 20.0F)) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(OFParticleTypes.KIRIN_RING.get(), this.getX(), this.getY(), this.getZ(), this.yBodyRot, 1.4D, i);
            }
        }
        if (!this.level().isClientSide && this.deathTicks >= DEATH_LENGTH * 20.0F && !this.isRemoved()) {
            if (this.deathSource != null) {
                super.dropAllDeathLoot((ServerLevel) this.level(), this.deathSource);
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    public float getDeathSeconds(float partialTicks) {
        return (this.deathTicks + partialTicks) / 20.0F;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.idleBlendCooldown > 0) {
            this.idleBlendCooldown--;
        }
    }

    private void tickFootsteps() {
        float walkTime = this.walkAnimation.position() * 0.05F * 5.0F;
        if (this.onGround() && this.walkAnimation.speed() > 0.15F) {
            for (int i = 0; i < STEP_TIMES.length; i++) {
                if (Mth.floor((walkTime - STEP_TIMES[i]) / 2.0F) > Mth.floor((this.prevWalkTime - STEP_TIMES[i]) / 2.0F)) {
                    this.footstep(FOOT_POSITIONS[i]);
                }
            }
        }
        this.prevWalkTime = walkTime;
    }

    private void spawnHoverSmoke() {
        float[] foot = IDLE_FOOT_POSITIONS[this.random.nextInt(IDLE_FOOT_POSITIONS.length)];
        double x = this.footX(foot) + (this.random.nextDouble() - 0.5D) * 0.4D;
        double z = this.footZ(foot) + (this.random.nextDouble() - 0.5D) * 0.4D;
        this.level().addParticle(OFParticleTypes.KIRIN_SMOKE.get(), x, this.getY() + 0.05D, z, (this.random.nextDouble() - 0.5D) * 0.02D, 0.01D, (this.random.nextDouble() - 0.5D) * 0.02D);
    }

    private double footX(float[] foot) {
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        return this.getX() + foot[0] / 16.0D * Mth.cos(yaw) + foot[1] / 16.0D * Mth.sin(yaw);
    }

    private double footZ(float[] foot) {
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        return this.getZ() + foot[0] / 16.0D * Mth.sin(yaw) - foot[1] / 16.0D * Mth.cos(yaw);
    }

    private void footstep(float[] foot) {
        double x = this.footX(foot);
        double z = this.footZ(foot);
        this.level().addParticle(OFParticleTypes.KIRIN_STEP.get(), x, this.getY() + 0.02D, z, this.yBodyRot, 0.0D, 0.0D);
        this.level().playLocalSound(x, this.getY(), z, SoundEvents.AMETHYST_BLOCK_STEP, this.getSoundSource(), 0.5F, 0.85F + this.random.nextFloat() * 0.3F, false);
    }

    @Override
    public BodyChain getBodyChain() {
        return this.bodyChain;
    }

    @Override
    public float getRenderYaw(float partialTicks) {
        return this.bodyChain.getRenderYaw(partialTicks);
    }

    @Override
    public float getSegmentYawOffset(int index, float partialTicks) {
        return this.bodyChain.getSegmentYawOffset(index, partialTicks);
    }

    @Override
    public float getSegmentPitchOffset(int index, float partialTicks) {
        return this.bodyChain.getSegmentPitchOffset(index, partialTicks, 0.0F);
    }

    public float getDarkness() {
        float daylight = Mth.clamp(0.5F + 2.0F * Mth.cos(this.level().getTimeOfDay(1.0F) * Mth.TWO_PI), 0.0F, 1.0F);
        float weather = (1.0F - this.level().getRainLevel(1.0F) * 5.0F / 16.0F) * (1.0F - this.level().getThunderLevel(1.0F) * 5.0F / 16.0F);
        return 1.0F - daylight * weather;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() != DEATH_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() != DEATH_ANIMATION, this.tickCount);
        this.idleTwitchOneAnimationState.animateWhen(this.getAnimationState() == IDLE_TWITCH_ONE_ANIMATION, this.tickCount);
        this.idleTwitchTwoAnimationState.animateWhen(this.getAnimationState() == IDLE_TWITCH_TWO_ANIMATION, this.tickCount);
        this.idleTwitchThreeAnimationState.animateWhen(this.getAnimationState() == IDLE_TWITCH_THREE_ANIMATION, this.tickCount);
        this.idleShakeOneAnimationState.animateWhen(this.getAnimationState() == IDLE_SHAKE_ONE_ANIMATION, this.tickCount);
        this.idleShakeTwoAnimationState.animateWhen(this.getAnimationState() == IDLE_SHAKE_TWO_ANIMATION, this.tickCount);
        this.deathAnimationState.animateWhen(this.getAnimationState() == DEATH_ANIMATION, this.tickCount);
    }

    private static class KirinIdleBlendGoal extends Goal {

        private final Kirin kirin;
        private int duration;
        private int timer;

        public KirinIdleBlendGoal(Kirin kirin) {
            this.kirin = kirin;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.kirin.getAnimationState() == 0 && this.kirin.idleBlendCooldown <= 0 && this.kirin.getTarget() == null && this.kirin.getNavigation().isDone() && this.kirin.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4D && this.kirin.getRandom().nextInt(reducedTickDelay(100)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < this.duration && this.kirin.getTarget() == null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            int idlePick = this.kirin.getRandom().nextInt(IDLE_BLEND_DURATIONS.length);
            this.timer = 0;
            this.duration = IDLE_BLEND_DURATIONS[idlePick];
            this.kirin.getNavigation().stop();
            this.kirin.setAnimationState(IDLE_TWITCH_ONE_ANIMATION + idlePick);
        }

        @Override
        public void stop() {
            this.kirin.setAnimationState(0);
            this.kirin.idleBlendCooldown = IDLE_BLEND_COOLDOWN;
        }

        @Override
        public void tick() {
            this.timer++;
        }
    }
}