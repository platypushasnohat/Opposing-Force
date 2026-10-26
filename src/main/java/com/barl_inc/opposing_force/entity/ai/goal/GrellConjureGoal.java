package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Grell;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GrellConjureGoal extends Goal {

    private final Grell grell;
    private int timer;

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
        return this.timer < Grell.CONJURE_TICKS && this.isValidTarget(this.grell.getTarget());
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
        this.grell.getNavigation().stop();
        this.grell.setAnimationState(Grell.CONJURE_ANIMATION);
    }

    @Override
    public void stop() {
        if (this.timer >= Grell.CONJURE_TICKS) {
            this.grell.setConjureCooldown(400);
        } else {
            this.grell.setConjureCooldown(0);
        }
        this.grell.setAnimationState(0);
    }

    @Override
    public void tick() {
        this.timer++;
        LivingEntity target = this.grell.getTarget();
        if (target != null) {
            this.grell.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }
        this.grell.getMoveControl().setWantedPosition(this.grell.getX(), this.grell.getY(), this.grell.getZ(), 0.0D);
        this.grell.setDeltaMovement(this.grell.getDeltaMovement().scale(0.5D));
    }
}
