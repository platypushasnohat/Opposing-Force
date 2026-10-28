package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Pipsqueak;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class PipsqueakAttackGoal extends AttackGoal {

    private final Pipsqueak pipsqueak;
    private int attackCooldown;
    private int screamCooldown;
    private int aimedWaves;
    private int chaseTicks;
    private float orbitAngle;

    public PipsqueakAttackGoal(Pipsqueak pipsqueak) {
        super(pipsqueak);
        this.pipsqueak = pipsqueak;
    }

    @Override
    public void start() {
        super.start();
        this.attackCooldown = 10;
        this.chaseTicks = 0;
        this.orbitAngle = this.pipsqueak.getRandom().nextFloat() * Mth.TWO_PI;
    }

    @Override
    public void stop() {
        super.stop();
        this.pipsqueak.setMouthOpen(false);
        this.pipsqueak.setSwallowed(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.pipsqueak.getTarget();
        if (this.screamCooldown > 0) {
            this.screamCooldown--;
        }
        if (target != null) {
            switch (this.attackState) {
                case 1 -> this.tickBite(target);
                case 2 -> this.tickChoke();
                case 3 -> this.tickScream(target);
                default -> this.tickHover(target);
            }
        }
    }

    private void tickHover(LivingEntity target) {
        this.lookAtTarget(target, 30.0F, 30.0F);
        boolean cooldownEnded = this.attackCooldown == 1;
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        } else {
            this.chaseTicks++;
        }
        if (this.chaseTicks >= 100 || (cooldownEnded && this.screamCooldown <= 0 && this.pipsqueak.hasLineOfSight(target) && this.pipsqueak.getRandom().nextBoolean())) {
            this.setState(3);
        } else if (!this.pipsqueak.hasLineOfSight(target)) {
            if (this.pipsqueak.getNavigation().isDone()) {
                this.pipsqueak.getNavigation().moveTo(target, 1.2D);
            }
        } else if (this.attackCooldown > 0) {
            this.pipsqueak.getNavigation().stop();
            this.orbitAngle += 0.08F;
            double x = target.getX() + Mth.cos(this.orbitAngle) * 3.0D;
            double y = target.getEyeY() + 0.5D;
            double z = target.getZ() + Mth.sin(this.orbitAngle) * 3.0D;
            this.pipsqueak.getMoveControl().setWantedPosition(x, y, z, 1.0D);
        } else {
            this.pipsqueak.getNavigation().stop();
            this.moveToMouth(target, 1.4D);
            if (this.pipsqueak.distanceToSqr(target) <= this.getAttackReachSqr(target, 1.5D)) {
                this.setState(1);
            }
        }
    }

    private void tickBite(LivingEntity target) {
        this.timer++;
        if (this.timer == 1) {
            this.pipsqueak.setAnimationState(Pipsqueak.BITE_ANIMATION);
            this.pipsqueak.setMouthOpen(true);
            this.pipsqueak.setSwallowed(false);
        }
        if (this.timer < 7) {
            this.lookAtTarget(target, 30.0F, 30.0F);
            this.moveToMouth(target, 1.0D);
        }
        if (this.timer == 7) {
            this.pipsqueak.setMouthOpen(false);
            this.pipsqueak.playSound(SoundEvents.PHANTOM_BITE, 1.0F, 1.4F);
            this.pipsqueak.getMoveControl().setWantedPosition(this.pipsqueak.getX(), this.pipsqueak.getY(), this.pipsqueak.getZ(), 1.0D);
            if (this.isInAttackRange(target, 0.6D)) {
                this.pipsqueak.doHurtTarget(target);
            }
        }
        if (this.timer >= 15) {
            this.finishMouthAttack();
        }
    }

    private void tickScream(LivingEntity target) {
        this.timer++;
        this.lookAtTarget(target, 30.0F, 30.0F);
        if (this.timer == 1) {
            this.pipsqueak.setAnimationState(Pipsqueak.SCREAM_ANIMATION);
            this.aimedWaves = 2 + this.pipsqueak.getRandom().nextInt(2);
            this.pipsqueak.setSwallowed(false);
            this.pipsqueak.getNavigation().stop();
            this.pipsqueak.getMoveControl().setWantedPosition(this.pipsqueak.getX(), this.pipsqueak.getY(), this.pipsqueak.getZ(), 1.0D);
        }
        if (this.timer == 12) {
            this.pipsqueak.setMouthOpen(true);
            this.pipsqueak.playSound(SoundEvents.PHANTOM_AMBIENT, 1.5F, 1.2F);
        }
        if (this.timer >= 13 && this.timer <= 33 && (this.timer - 13) % 2 == 0) {
            boolean aimed = this.pipsqueak.getRandom().nextInt(11 - (this.timer - 13) / 2) < this.aimedWaves;
            if (aimed) {
                this.aimedWaves--;
            }
            this.pipsqueak.releaseSoundwave(target, aimed);
        }
        if (this.timer >= 40) {
            this.pipsqueak.setMouthOpen(false);
            this.screamCooldown = 60;
            this.finishMouthAttack();
        }
    }

    private void finishMouthAttack() {
        if (this.pipsqueak.hasSwallowed()) {
            this.pipsqueak.setSwallowed(false);
            this.pipsqueak.setAnimationState(Pipsqueak.CHOKE_ANIMATION);
            this.pipsqueak.playSound(SoundEvents.PLAYER_HURT_DROWN, 1.0F, 1.6F);
            this.setState(2);
        } else {
            this.pipsqueak.setAnimationState(0);
            this.attackCooldown = 10;
            this.setState(0);
        }
    }

    private void tickChoke() {
        this.timer++;
        this.pipsqueak.getNavigation().stop();
        if (this.timer == 11 || this.timer == 22 || this.timer == 30) {
            this.pipsqueak.spitSwallowed();
            this.pipsqueak.playSound(SoundEvents.LLAMA_SPIT, 1.0F, 1.4F);
        }
        if (this.timer >= 40) {
            this.pipsqueak.setAnimationState(0);
            this.attackCooldown = 40;
            this.setState(0);
        }
    }

    private void moveToMouth(LivingEntity target, double speed) {
        Vec3 toPipsqueak = this.pipsqueak.position().subtract(target.position()).multiply(1.0D, 0.0D, 1.0D).normalize().scale(target.getBbWidth() * 0.5D + 0.6D);
        double y = target.getEyeY() - this.pipsqueak.getBbHeight() * 0.5D;
        this.pipsqueak.getMoveControl().setWantedPosition(target.getX() + toPipsqueak.x, y, target.getZ() + toPipsqueak.z, speed);
    }

    private void setState(int state) {
        this.attackState = state;
        this.timer = 0;
        this.chaseTicks = 0;
    }
}
