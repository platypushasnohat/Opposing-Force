package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Pipsqueak;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class PipsqueakAttackGoal extends AttackGoal {

    private final Pipsqueak pipsqueak;
    private int attackCooldown;
    private float orbitAngle;

    public PipsqueakAttackGoal(Pipsqueak pipsqueak) {
        super(pipsqueak);
        this.pipsqueak = pipsqueak;
    }

    @Override
    public void start() {
        super.start();
        this.attackCooldown = 10;
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
        if (target != null) {
            switch (this.attackState) {
                case 1 -> this.tickBite(target);
                case 2 -> this.tickChoke();
                default -> this.tickHover(target);
            }
        }
    }

    private void tickHover(LivingEntity target) {
        this.lookAtTarget(target, 30.0F, 30.0F);
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }
        if (!this.pipsqueak.hasLineOfSight(target)) {
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
            this.pipsqueak.getMoveControl().setWantedPosition(this.pipsqueak.getX(), this.pipsqueak.getY(), this.pipsqueak.getZ(), 1.0D);
            if (this.isInAttackRange(target, 0.6D)) {
                this.pipsqueak.doHurtTarget(target);
            }
        }
        if (this.timer >= 15) {
            if (this.pipsqueak.hasSwallowed()) {
                this.pipsqueak.setSwallowed(false);
                this.pipsqueak.setAnimationState(Pipsqueak.CHOKE_ANIMATION);
                this.setState(2);
            } else {
                this.pipsqueak.setAnimationState(0);
                this.attackCooldown = 10;
                this.setState(0);
            }
        }
    }

    private void tickChoke() {
        this.timer++;
        this.pipsqueak.getNavigation().stop();
        if (this.timer == 11 || this.timer == 22 || this.timer == 30) {
            this.pipsqueak.spitSwallowed();
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
    }
}