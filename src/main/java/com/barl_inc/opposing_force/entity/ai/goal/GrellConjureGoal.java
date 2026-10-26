package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Grell;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GrellConjureGoal extends Goal {

    private final Grell grell;
    private int timer;
    private int punishTimer;

    public GrellConjureGoal(Grell grell) {
        this.grell = grell;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.grell.getTarget();
        return this.grell.getConjureCooldown() <= 0 && this.isValidTarget(target) && this.grell.hasLineOfSight(target);
    }

    @Override
    public boolean canContinueToUse() {
        boolean conjuring = this.timer < Grell.CONJURE_TICKS || this.grell.isHidden();
        boolean punishing = this.punishTimer >= 0 && this.punishTimer < 7;
        return this.isValidTarget(this.grell.getTarget()) && (conjuring || punishing);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private boolean isValidTarget(LivingEntity target) {
        return target != null && target.isAlive() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target);
    }

    @Override
    public void start() {
        this.timer = 0;
        this.punishTimer = -1;
        this.grell.getNavigation().stop();
        this.grell.setAnimationState(Grell.CONJURE_ANIMATION);
        this.grell.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.5F, 0.7F);
    }

    @Override
    public void stop() {
        if (!this.isValidTarget(this.grell.getTarget())) {
            this.grell.setConjureCooldown(0);
        } else if (this.punishTimer >= 0) {
            this.grell.setConjureCooldown(80);
        }
        this.grell.endIllusion();
        this.grell.setAnimationState(0);
    }

    @Override
    public void tick() {
        this.timer++;
        LivingEntity target = this.grell.getTarget();
        if (target != null) {
            this.grell.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.timer == Grell.CONJURE_TICKS) {
                this.grell.startIllusion(target, 4);
            }
            if (this.grell.isHidden() && this.grell.getIllusionTicks() <= 0) {
                this.grell.failIllusion();
                this.grell.setAnimationState(Grell.BITE_ANIMATION);
                this.punishTimer = 0;
            }
        }
        if (this.punishTimer >= 0 && target != null) {
            this.tickPunish(target);
        } else {
            this.grell.getMoveControl().setWantedPosition(this.grell.getX(), this.grell.getY(), this.grell.getZ(), 0.0D);
            this.grell.setDeltaMovement(this.grell.getDeltaMovement().scale(0.5D));
        }
    }

    private void tickPunish(LivingEntity target) {
        this.punishTimer++;
        this.grell.getMoveControl().setWantedPosition(target.getX(), target.getEyeY() - this.grell.getBbHeight() * 0.5D, target.getZ(), 2.0D);
        if (this.punishTimer == 5) {
            this.grell.punishBite(target);
        }
    }
}
