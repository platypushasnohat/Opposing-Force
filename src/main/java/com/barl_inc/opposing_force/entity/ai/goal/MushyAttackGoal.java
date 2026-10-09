package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Mushy;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import com.barl_inc.opposing_force.entity.Mushy;

import java.util.List;

public class MushyAttackGoal extends AttackGoal {
    private final Mushy mushy;
    private int attackCooldown;
    private boolean swinging;
    //attack state value:
    //0 deciding
    //1 melee
    //2 launch windup
    //3 ascending
    //4 launch hang
    //5 descending
    //6 recovery

    public MushyAttackGoal(Mushy mushy) {
        super(mushy);
        this.mushy = mushy;
    }

    private boolean isLaunching() {
        return this.attackState >= 2;
    }

    @Override
    public void start() {
        super.start();
        this.attackCooldown = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.mushy.setLaunchTilt(0.0F);
    }

    @Override
    public boolean isInterruptable() {
        return !this.isLaunching();
    }

    @Override
    public boolean canContinueToUse() {
        return this.isLaunching() || super.canContinueToUse();
    }

    @Override
    public void tick() {
        LivingEntity target = this.mushy.getTarget();
        switch (this.attackState) {
            case 1 -> this.tickMelee(target);
            case 2 -> this.tickWindup(target);
            case 3 -> this.tickAscend(target);
            case 4 -> this.tickHang(target);
            case 5 -> this.tickDive();
            case 6 -> this.tickRecover();
            default -> this.tickDecide(target);
        }
    }

    private void tickDecide(LivingEntity target) {
        if (target != null) {
            this.lookAtTarget(target, 30.0F, 20.0F);
            if (this.attackCooldown > 0) {
                this.attackCooldown--;
                return;
            }
            if (this.mushy.getRandom().nextFloat() < 0.33F && this.canLaunchAt(target)) {
                this.setState(2);
            }
            else {
                this.swinging = false;
                this.setState(1);
            }
        }
    }

    private boolean canLaunchAt(LivingEntity target) {
        return this.mushy.onGround()
                && !this.mushy.isInWaterOrBubble()
                && this.mushy.distanceToSqr(target) <= 11.0D * 11.0D
                && this.isWithinYRange(target, 4)
                && this.mushy.hasLineOfSight(target)
                && this.mushy.level().noCollision(this.mushy, this.mushy.getBoundingBox().expandTowards(0.0D, 5.0D, 0.0D));
    }

    private void tickMelee(LivingEntity target) {
        if (target != null) {
            this.lookAtTarget(target, 30.0F, 20.0F);
            this.mushy.getNavigation().moveTo(target, 1.1D);
            if (!this.swinging) {
                if (this.mushy.distanceToSqr(target) <= this.getAttackReachSqr(target, 1.55D)) {
                    this.swinging = true;
                    this.timer = 0;
                }
                return;
            }
            this.timer++;
            if (this.timer == 1) {
                this.mushy.setAnimationState(Mushy.ATTACK_ANIMATION);
                this.mushy.playSound(OFSoundEvents.FURBALL_ATTACK.get(), 1.0F, SinewSoundUtils.randomizePitch(this.mushy));
            }
            if (this.timer == 11 && this.isInAttackRange(target, 0.65D)) {
                this.mushy.doHurtTarget(target);
            }
            if (this.timer > 15) {
                this.mushy.setAnimationState(0);
                this.swinging = false;
                this.attackCooldown = 5 + this.mushy.getRandom().nextInt(3);
                this.setState(0);
            }
        }
    }

    private void tickWindup(LivingEntity target) {
        this.timer++;
        this.mushy.getNavigation().stop();
        if (target != null) {
            this.lookAtTarget(target, 30.0F, 20.0F);
        }
        if (this.timer == 1) {
            this.mushy.setAnimationState(Mushy.JUMP_ANIMATION);
        }
        if (this.timer >= 15) {
            Vec3 motion = this.mushy.getDeltaMovement();
            this.mushy.setDeltaMovement(motion.x, 1.3D, motion.z);
            this.mushy.hasImpulse = true;
            this.mushy.setAnimationState(Mushy.SPIN_ANIMATION);
            this.setState(3);
        }
    }

    private void tickAscend(LivingEntity target) {
        this.timer++;
        this.mushy.getNavigation().stop();
        this.steerOver(target);
        if (this.timer > 3 && this.mushy.getDeltaMovement().y < 0.35D) {
            if (target != null) {
                this.mushy.setLaunchTiltYaw(yawToward(this.mushy.position(), target.position()));
            }
            else {
                this.mushy.setLaunchTiltYaw(this.mushy.getYRot());
            }
            this.setState(4);
        }
        this.checkAirTime();
    }

    private void tickHang(LivingEntity target) {
        this.timer++;
        this.mushy.getNavigation().stop();
        float progress = Mth.clamp((float) this.timer / 16, 0.0F, 1.0F);
        float tilt = 180.0F * (progress * progress * (3.0F - (progress * 2.0F)));
        this.mushy.setLaunchTilt(tilt);
        if (tilt < 120.0F) {
            this.steerOver(target);
        }
        Vec3 motion = this.mushy.getDeltaMovement();
        this.mushy.setDeltaMovement(motion.x, motion.y + 0.05D, motion.z);
        if (this.timer >= 16) {
            this.setState(5);
        }
    }

    //aim over player for fall
    private void steerOver(LivingEntity target) {
        if (target != null) {
            this.lookAtTarget(target, 30.0F, 30.0F);
            Vec3 motion = this.mushy.getDeltaMovement();
            Vec3 offset = new Vec3(target.getX() - this.mushy.getX(), 0.0D, target.getZ() - this.mushy.getZ());
            double distance = offset.length();
            Vec3 wanted = distance > 1.0E-4D ? offset.scale(Math.min(distance * 0.25D, 0.45D) / distance) : Vec3.ZERO; //skip tiny angle to avoid snapping
            this.mushy.setDeltaMovement(Mth.lerp(0.3D, motion.x, wanted.x), motion.y, Mth.lerp(0.3D, motion.z, wanted.z));
        }
    }

    private void tickDive() {
        this.timer++;
        this.mushy.getNavigation().stop();
        Vec3 motion = this.mushy.getDeltaMovement();
        double y = Math.max(motion.y - 0.15D, -2.2D);
        this.mushy.setDeltaMovement(motion.x * 0.5D, y, motion.z * 0.5D);
        if (this.mushy.onGround() || this.isTouchingEntity()) {
            this.impact(this.getEntitiesInImpactRange());
        }
        else if (this.mushy.isInWaterOrBubble()) {
            this.endLaunch();
        }
        this.checkAirTime();
    }

    private boolean isTouchingEntity() {
        return !this.mushy.level().getEntitiesOfClass(LivingEntity.class,
                this.mushy.getBoundingBox().inflate(0.1D),
                this::canImpactHit).isEmpty();
    }

    private List<LivingEntity> getEntitiesInImpactRange() {
        return this.mushy.level().getEntitiesOfClass(LivingEntity.class,
                this.mushy.getBoundingBox().inflate(1.5D, 0.5D, 1.5D),
                this::canImpactHit);
    }

    private boolean canImpactHit(LivingEntity entity) {
        return entity != this.mushy && entity.isAlive() && this.mushy.canAttack(entity);
    }

    private void impact(List<LivingEntity> hit) {
        float damage = (float) this.mushy.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0F;
        for (LivingEntity entity : hit) {
            if (entity.hurt(this.mushy.damageSources().mobAttack(this.mushy), damage)) {
                entity.knockback(2.0D, this.mushy.getX() - entity.getX(), this.mushy.getZ() - entity.getZ());
            }
        }
        this.mushy.setDeltaMovement(0.0D, 0.0D, 0.0D);
        this.mushy.fallDistance = 0.0F;
        this.mushy.setAnimationState(0);
        this.setState(6);
    }

    private void spawnSporeBurst() {
        if ((this.mushy.level() instanceof ServerLevel level)) {
            RandomSource random = this.mushy.getRandom();
            int clumps = 8;
            for (int i = 0; i < clumps; i++) {
                double angle = (Math.PI * 2.0D) * (i + random.nextDouble() * 0.5D) / clumps;
                double dirX = Math.cos(angle);
                double dirZ = Math.sin(angle);
                double clumpX = this.mushy.getX() + dirX * 0.6D;
                double clumpY = this.mushy.getY() + 0.1D + random.nextDouble();
                double clumpZ = this.mushy.getZ() + dirZ * 0.6D;
                for (int j = 0; j < 8; j++) {
                    double x = clumpX + (random.nextDouble() - 0.5D) * 0.5D;
                    double y = clumpY + (random.nextDouble() - 0.5D) * 0.6D;
                    double z = clumpZ + (random.nextDouble() - 0.5D) * 0.5D;
                    double speed = 0.06D + random.nextDouble() * 0.06D;
                    level.sendParticles(OFParticleTypes.SPORE_CLOUD.get(), x, y, z, 0, dirX * speed, 0.02D, dirZ * speed, 1.0D);
                }
            }
        }
    }

    private void tickRecover() {
        this.timer++;
        this.mushy.getNavigation().stop();
        int bounceDelay = 3;
        if (this.timer == bounceDelay) { //small bounce upwards after delay so it lands on ground visually
            this.spawnSporeBurst();
            this.mushy.setDeltaMovement(0.0D, 0.65D, 0.0D);
            this.mushy.hasImpulse = true;
        }
        if (this.timer > bounceDelay + 1 && this.mushy.onGround() && this.mushy.getAnimationState() == Mushy.SPIN_ANIMATION) {
            this.mushy.setAnimationState(0);
        }
        //flip upright
        float revert = Mth.clamp(1.0F - (float) (this.timer - bounceDelay) / 8.0F, 0.0F, 1.0F);
        this.mushy.setLaunchTilt(180.0F * revert);
        if (this.timer >= 25 + bounceDelay) {
            this.endLaunch();
        }
    }

    private void endLaunch() {
        this.mushy.setLaunchTilt(0.0F);
        if (this.mushy.getAnimationState() == Mushy.SPIN_ANIMATION || this.mushy.getAnimationState() == Mushy.JUMP_ANIMATION) {
            this.mushy.setAnimationState(0);
        }
        this.attackCooldown = 10;
        this.setState(0);
    }

    //launch timeout
    private void checkAirTime() {
        if (this.timer > 300) {
            this.endLaunch();
        }
    }

    private void setState(int state) {
        this.attackState = state;
        this.timer = 0;
    }

    private static float yawToward(Vec3 from, Vec3 to) {
        return (float) (Mth.atan2(to.z - from.z, to.x - from.x) * Mth.RAD_TO_DEG) - 90.0F;
    }
}