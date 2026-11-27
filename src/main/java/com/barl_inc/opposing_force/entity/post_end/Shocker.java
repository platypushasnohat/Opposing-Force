package com.barl_inc.opposing_force.entity.post_end;

import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class Shocker extends AnimatedMonster {

    private static final EntityDataAccessor<Float> HEAD_SPIN = SynchedEntityData.defineId(Shocker.class, EntityDataSerializers.FLOAT);

    public static final byte TWEAK_EVENT = 70;

    public final SmoothAnimationState tweakAnimationState = new SmoothAnimationState();

    private int tweakCooldown;
    private int tweakTicks;

    public float prevHeadSpin;
    public float headSpin;

    public float prevGlow;
    public float glow;

    public Shocker(EntityType<? extends Shocker> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 15;
        this.tweakCooldown = 100 + this.getRandom().nextInt(101);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new ShockerHeadSpinGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HEAD_SPIN, 0.0F);
    }

    public float getHeadSpin() {
        return this.getEntityData().get(HEAD_SPIN);
    }

    public void setHeadSpin(float degrees) {
        this.getEntityData().set(HEAD_SPIN, Mth.wrapDegrees(degrees));
    }

    public float getHeadSpin(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.prevHeadSpin, this.headSpin);
    }

    public float getGlow(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevGlow, this.glow);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.prevHeadSpin = this.headSpin;
            this.headSpin = this.getHeadSpin();
            this.prevGlow = this.glow;
            this.glow += (this.getDarkness() - this.glow) * 0.1F;
            if (this.tweakTicks > 0) {
                this.tweakTicks--;
            }
        }
    }

    public float getDarkness() {
        BlockPos pos = BlockPos.containing(this.getEyePosition());
        float daylight = Mth.clamp(0.5F + 2.0F * Mth.cos(this.level().getTimeOfDay(1.0F) * Mth.TWO_PI), 0.0F, 1.0F);
        float weather = (1.0F - this.level().getRainLevel(1.0F) * 5.0F / 16.0F) * (1.0F - this.level().getThunderLevel(1.0F) * 5.0F / 16.0F);
        float sky = this.level().getBrightness(LightLayer.SKY, pos) / 15.0F * daylight * weather;
        float block = this.level().getBrightness(LightLayer.BLOCK, pos) / 15.0F;
        return 1.0F - Math.max(sky, block);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.tweakCooldown--;
        if (this.tweakCooldown <= 0) {
            this.level().broadcastEntityEvent(this, TWEAK_EVENT);
            this.tweakCooldown = 100 + this.getRandom().nextInt(101);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == TWEAK_EVENT) {
            this.tweakTicks = 40;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(true, this.tickCount);
        this.walkAnimationState.animateWhen(true, this.tickCount);
        this.tweakAnimationState.animateWhen(this.tweakTicks > 0, this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float length = (float) Mth.length(this.getX() - this.xo, 0.0F, this.getZ() - this.zo);
        float speed = Math.min(length * 8.0F, 1.0F);
        this.walkAnimation.update(speed, 0.4F);
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
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    private static class ShockerHeadSpinGoal extends Goal {

        private final Shocker shocker;
        private float direction;
        private float spun;

        public ShockerHeadSpinGoal(Shocker shocker) {
            this.shocker = shocker;
        }

        @Override
        public boolean canUse() {
            return this.shocker.getRandom().nextInt(reducedTickDelay(200)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.spun < 360.0F;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.direction = this.shocker.getRandom().nextBoolean() ? 1.0F : -1.0F;
            this.spun = 0.0F;
        }

        @Override
        public void tick() {
            float step = Math.min(3.0F, 360.0F - this.spun);
            this.spun += step;
            this.shocker.setHeadSpin(this.shocker.getHeadSpin() + step * this.direction);
        }
    }
}