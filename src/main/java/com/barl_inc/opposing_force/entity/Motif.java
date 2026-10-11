package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.projectile.Mudball;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.control.UnrestrictedBodyRotationControl;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Motif extends AnimatedMonster {

    public static final int SHOOT_ANIMATION = 1;

    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState();

    public Motif(EntityType<? extends Motif> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MotifAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false, target -> !isWearingPot(target)));
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new UnrestrictedBodyRotationControl(this);
    }

    public static boolean isWearingPot(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(OFItems.MYSTIC_POT.get());
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() != SHOOT_ANIMATION, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() != SHOOT_ANIMATION, this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == SHOOT_ANIMATION, this.tickCount);
    }

    @Override
    public int getMaxHeadYRot() {
        return 180;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = this.getTarget();
        if (target instanceof Player && isWearingPot(target) && target != this.getLastHurtByMob()) {
            this.setTarget(null);
        }
        Player player = this.level().getNearestPlayer(this, 16.0D);
        if (player != null) {
            this.getLookControl().setLookAt(player, 30.0F, 30.0F);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15F, SinewSoundUtils.randomizePitch(this));
    }

    private static class MotifAttackGoal extends AttackGoal {

        private static final int[] SHOT_TICKS = {14, 18, 23, 28, 33, 38};
        private final Motif motif;
        private int barrageCooldown;
        private static final float BARRAGE_TURN_SPEED = 8.0F;

        public MotifAttackGoal(Motif motif) {
            super(motif);
            this.motif = motif;
        }

        @Override
        public void start() {
            super.start();
            this.barrageCooldown = 20 + this.motif.getRandom().nextInt(20);
        }

        @Override
        public void stop() {
            super.stop();
            this.motif.setAnimationState(0);
        }

        @Override
        public void tick() {
            LivingEntity target = this.motif.getTarget();
            if (target != null) {
                double distance = this.motif.distanceToSqr(target);
                if (this.attackState == 1) {
                    this.tickBarrage(target);
                }
                else {
                    this.tickDecide(target, distance);
                }
            }
        }

        private void tickDecide(LivingEntity target, double distance) {
            if (this.barrageCooldown > 0) {
                this.barrageCooldown--;
            }
            boolean canSee = this.motif.hasLineOfSight(target);
            if (canSee && distance <= 16.0D * 16.0D && this.barrageCooldown <= 0) {
                this.timer = 0;
                this.attackState = 1;
            }
            else if (canSee && distance <= 8.0D * 8.0D) {
                this.lookAtTarget(target, 25.0F, 25.0F);
                this.motif.getNavigation().stop();
            }
            else {
                this.lookAtTarget(target, 25.0F, 25.0F);
                this.motif.getNavigation().moveTo(target, 1.0D);
            }
        }

        private void tickBarrage(LivingEntity target) {
            this.motif.getNavigation().stop();
            this.timer++;
            if (this.timer == 1) {
                this.motif.setAnimationState(SHOOT_ANIMATION);
            }
            this.turnToward(target);
            for (int i = 0; i < SHOT_TICKS.length; i++) {
                if (this.timer == SHOT_TICKS[i]) {
                    this.shootMudball(target, i % 2 == 0);
                }
            }
            if (this.timer > 50) {
                this.motif.setAnimationState(0);
                this.timer = 0;
                this.barrageCooldown = 25 + this.motif.getRandom().nextInt(25);
                this.attackState = 0;
            }
        }

        private void turnToward(LivingEntity target) {
            float wanted = (float) (Mth.atan2(target.getZ() - this.motif.getZ(), target.getX() - this.motif.getX()) * Mth.RAD_TO_DEG) - 90.0F;
            float yaw = Mth.approachDegrees(this.motif.getYRot(), wanted, BARRAGE_TURN_SPEED);
            this.motif.setYRot(yaw);
            this.motif.setYHeadRot(yaw);
            this.motif.setYBodyRot(yaw);
        }

        private void shootMudball(LivingEntity target, boolean left) {
            float yaw = -this.motif.getYRot() * Mth.DEG_TO_RAD;
            Vec3 muzzle = this.motif.position().add(new Vec3(left ? 0.72D : -0.72D, 2.45D, 1.05D).yRot(yaw));
            double range = this.motif.position().subtract(target.position()).horizontalDistance();
            Vec3 aim = this.motif.position().add(new Vec3(0.0D, 0.0D, range).yRot(yaw)).with(Direction.Axis.Y, target.getY(0.25D));
            Vec3 delta = aim.subtract(muzzle);
            double flightTime = delta.horizontalDistance() / 1.1F;
            Mudball mudball = new Mudball(this.motif.level(), this.motif, muzzle.x, muzzle.y, muzzle.z);
            mudball.shoot(delta.x, delta.y + 0.5D * 0.03D * flightTime * flightTime, delta.z, 1.1F, 1.5F);
            this.motif.level().addFreshEntity(mudball);
            this.motif.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.6F * SinewSoundUtils.randomizePitch(this.motif));
        }
    }
}
