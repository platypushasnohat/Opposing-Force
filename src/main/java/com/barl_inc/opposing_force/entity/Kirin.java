package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.KirinAttackGoal;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.base.SummonableMount;
import com.platypushasnohat.sinew.entity.utils.BodyChain;
import com.platypushasnohat.sinew.entity.utils.BodyChainMob;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class Kirin extends SummonableMount implements BodyChainMob {

    public static final int IDLE_TWITCH_ONE_ANIMATION = 1;
    public static final int IDLE_TWITCH_TWO_ANIMATION = 2;
    public static final int IDLE_TWITCH_THREE_ANIMATION = 3;
    public static final int IDLE_SHAKE_ONE_ANIMATION = 4;
    public static final int IDLE_SHAKE_TWO_ANIMATION = 5;
    public static final int DEATH_ANIMATION = 6;
    public static final int TELEPORT_ANIMATION = 7;
    public static final int LIGHTNING_CALL_ANIMATION = 8;

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
    public final SmoothAnimationState teleportAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState lightningCallAnimationState = new SmoothAnimationState(1.0F);

    private final BodyChain bodyChain = new BodyChain(0.5F, 0.0F, 0.0F, 0.0F, new float[]{0.5F, 0.25F, 0.3F, 0.35F}, new float[]{0.0F, 0.0F, 0.0F, 0.0F});

    private int idleBlendCooldown;
    private int deathTicks;
    @Nullable
    private Player closePlayer;
    private int closeTicks;
    @Nullable
    private DamageSource deathSource;
    private float prevWalkTime;
    private int teleportTicks;

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
        this.goalSelector.addGoal(1, new KirinAttackGoal(this));
        this.goalSelector.addGoal(1, new KirinIdleBlendGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new KirinProximityTargetGoal(this));
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
        if (this.level().isClientSide) {
            if (this.getAnimationState() == TELEPORT_ANIMATION) {
                this.teleportTicks++;
            } else {
                this.teleportTicks = 0;
            }
        }
        if (this.level().isClientSide && !this.isDeadOrDying()) {
            this.bodyChain.tick(this.yBodyRot, 0.0F, 0.0F);
            this.tickFootsteps();
            if (this.walkAnimation.speed() < 0.15F && this.getAnimationState() != TELEPORT_ANIMATION) {
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
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, this.getSoundSource(), 190.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, this.getSoundSource(), 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
            this.remove(RemovalReason.KILLED);
        }
    }

    public boolean isTeleportHidden(float partialTicks) {
        float seconds = (this.teleportTicks + partialTicks) / 20.0F;
        return this.getAnimationState() == TELEPORT_ANIMATION && seconds >= 0.09F && seconds < 0.34F;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        if (this.getAnimationState() == TELEPORT_ANIMATION && this.distanceToSqr(x, y, z) > 4.0D) {
            this.lerpSteps = 0;
            this.setPos(x, y, z);
            this.setYRot(yRot);
            this.setXRot(xRot);
            this.yBodyRot = yRot;
            this.yBodyRotO = yRot;
            this.yHeadRot = yRot;
            this.yHeadRotO = yRot;
            this.setOldPosAndRot();
        } else {
            super.lerpTo(x, y, z, yRot, xRot, steps);
        }
    }

    public void teleportBurst() {
        ServerLevel level = (ServerLevel) this.level();
        int bolts = 8 + this.random.nextInt(4);
        for (int i = 0; i < bolts; i++) {
            float angle = (i + this.random.nextFloat() * 0.6F) / bolts * Mth.TWO_PI;
            double length = 1.5D + this.random.nextDouble() * 1.5D;
            double reachX = Mth.cos(angle) * length;
            double reachY = 0.1D + this.random.nextDouble() * 0.3D;
            double reachZ = Mth.sin(angle) * length;
            double startX = this.getX() + Mth.cos(angle) * 0.4D;
            double startY = this.getY() + 0.1D;
            double startZ = this.getZ() + Mth.sin(angle) * 0.4D;
            level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX, startY, startZ, 0, reachX, reachY, reachZ, 1.0D);
            double split = 0.3D + this.random.nextDouble() * 0.4D;
            float branchAngle = angle + (this.random.nextBoolean() ? 0.6F : -0.6F);
            double branchLength = length * (1.0D - split) * 0.7D;
            level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX + reachX * split, startY + reachY * split, startZ + reachZ * split, 0, Mth.cos(branchAngle) * branchLength, 0.05D, Mth.sin(branchAngle) * branchLength, 1.0D);
        }
        level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), this.getX(), this.getY() + 0.3D, this.getZ(), 12, 0.6D, 0.2D, 0.6D, 0.0D);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), OFSoundEvents.ELECTRIC_CHARGE_DISSIPATE.get(), SoundSource.HOSTILE, 3.0F, (1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F) * 0.7F);
    }

    public void electricBurst(double x, double y, double z, int minBolts, double minLength, int arcs, float volume) {
        ServerLevel level = (ServerLevel) this.level();
        int bolts = minBolts + this.random.nextInt(4);
        for (int i = 0; i < bolts; i++) {
            float angle = (i + this.random.nextFloat() * 0.6F) / bolts * Mth.TWO_PI;
            double length = minLength + this.random.nextDouble() * minLength;
            double reachX = Mth.cos(angle) * length;
            double reachY = 0.1D + this.random.nextDouble() * 0.3D;
            double reachZ = Mth.sin(angle) * length;
            double startX = x + Mth.cos(angle) * 0.4D;
            double startY = y + 0.1D;
            double startZ = z + Mth.sin(angle) * 0.4D;
            level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX, startY, startZ, 0, reachX, reachY, reachZ, 1.0D);
            double split = 0.3D + this.random.nextDouble() * 0.4D;
            float branchAngle = angle + (this.random.nextBoolean() ? 0.6F : -0.6F);
            double branchLength = length * (1.0D - split) * 0.7D;
            level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX + reachX * split, startY + reachY * split, startZ + reachZ * split, 0, Mth.cos(branchAngle) * branchLength, 0.05D, Mth.sin(branchAngle) * branchLength, 1.0D);
        }
        level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), x, y + 0.3D, z, arcs, minLength * 0.4D, 0.2D, minLength * 0.4D, 0.0D);
        this.level().playSound(null, x, y, z, OFSoundEvents.ELECTRIC_CHARGE_DISSIPATE.get(), SoundSource.HOSTILE, volume, (1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F) * 0.7F);
    }

    public void lightningCall(Vec3 strike) {
        ((ServerLevel) this.level()).sendParticles(OFParticleTypes.LIGHTNING_CALL.get(), strike.x, strike.y, strike.z, 0, 0.0D, 40.0D, 0.0D, 1.0D);
    }

    public void lightningCallImpact(Vec3 strike) {
        this.electricBurst(strike.x, strike.y, strike.z, 16, 3.0D, 30, 5.0F);
        this.level().playSound(null, strike.x, strike.y, strike.z, OFSoundEvents.ELECTRIC_CHARGE_ZAP.get(), SoundSource.HOSTILE, 3.0F, 0.6F + this.random.nextFloat() * 0.2F);
        AABB hitBox = new AABB(strike.x - 0.8D, strike.y - 0.5D, strike.z - 0.8D, strike.x + 0.8D, strike.y + 2.5D, strike.z + 0.8D);
        for (Player player : this.level().getEntitiesOfClass(Player.class, hitBox, player -> player.isAlive() && !player.isSpectator() && !player.isCreative())) {
            if (player.hurt(OFDamageTypes.causeElectricDamage(this.level().registryAccess(), this, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                player.addEffect(new MobEffectInstance(OFMobEffects.ELECTRIFIED, 900, 0), this);
            }
        }
    }

    public List<Player> getLightningCallTargets() {
        return this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0D), player -> player.isAlive() && !player.isSpectator() && !player.isCreative() && player.hasEffect(OFMobEffects.ELECTRIFIED));
    }

    public float getDeathSeconds(float partialTicks) {
        return (this.deathTicks + partialTicks) / 20.0F;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        Player nearest = this.level().getNearestPlayer(TargetingConditions.forCombat().range(6.0D).selector(target -> !this.isIgnoredPlayer((Player) target)), this);
        if (nearest != null && nearest == this.closePlayer) {
            this.closeTicks++;
        } else {
            this.closePlayer = nearest;
            this.closeTicks = 0;
        }
        if (this.idleBlendCooldown > 0) {
            this.idleBlendCooldown--;
        }
    }

    //todo later once kirin armor in
    private boolean isIgnoredPlayer(Player player) {
        return false;
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
        //empty because the step stuff is custom
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(OFDamageTypes.ELECTRIFIED) || super.isInvulnerableTo(source);
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
        this.teleportAnimationState.animateWhen(this.getAnimationState() == TELEPORT_ANIMATION, this.tickCount);
        this.lightningCallAnimationState.animateWhen(this.getAnimationState() == LIGHTNING_CALL_ANIMATION, this.tickCount);
    }

    private static class KirinProximityTargetGoal extends TargetGoal {

        private final Kirin kirin;

        public KirinProximityTargetGoal(Kirin kirin) {
            super(kirin, false);
            this.kirin = kirin;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            return this.kirin.getTarget() == null && this.kirin.closePlayer != null && this.kirin.closeTicks >= 300;
        }

        @Override
        public void start() {
            this.kirin.setTarget(this.kirin.closePlayer);
            this.kirin.closeTicks = 0;
            super.start();
        }
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