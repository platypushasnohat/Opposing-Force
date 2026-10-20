package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Octovine;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.neoforge.common.Tags;

import java.util.Comparator;
import java.util.EnumSet;

public class OctovineEatGoal extends Goal {

    private final Octovine octovine;
    private ItemEntity meat;
    private int eatTimer;
    private int searchCooldown;
    private int stuckTicks;
    private ItemEntity ignoredMeat;

    public OctovineEatGoal(Octovine octovine) {
        this.octovine = octovine;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.octovine.angryTicks > 0 || this.octovine.getAnimationState() != 0) {
            return false;
        }
        if (this.searchCooldown > 0) {
            this.searchCooldown--;
            return false;
        }
        this.searchCooldown = 10;
        this.meat = this.findMeat();
        return this.meat != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.octovine.angryTicks <= 0 && this.meat != null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.octovine.distractedByMeat = true;
        this.octovine.setTarget(null);
        this.octovine.setAggressive(false);
        this.eatTimer = 0;
    }

    @Override
    public void stop() {
        this.octovine.distractedByMeat = false;
        this.meat = null;
        this.eatTimer = 0;
        this.octovine.getNavigation().stop();
        if (this.octovine.getAnimationState() == Octovine.EAT_ANIMATION) {
            this.octovine.setAnimationState(0);
        }
    }

    @Override
    public void tick() {
        if (this.meat == null || !this.meat.isAlive() || this.meat.getItem().isEmpty()) {
            this.meat = this.findMeat();
            this.eatTimer = 0;
            this.stuckTicks = 0;
            this.octovine.setAnimationState(0);
            return;
        }
        this.octovine.getLookControl().setLookAt(this.meat, 30.0F, 30.0F);
        boolean close = this.isClose();
        if (this.eatTimer > 0) {
            this.octovine.getNavigation().stop();
            this.eatTimer--;
            if (this.eatTimer == 0) {
                this.eatOne();
                if (!this.meat.getItem().isEmpty() && close) {
                    this.eatTimer = this.rollEatTime();
                } else {
                    this.octovine.setAnimationState(0);
                }
            }
        } else if (close) {
            this.stuckTicks = 0;
            this.octovine.getNavigation().stop();
            this.octovine.setAnimationState(Octovine.EAT_ANIMATION);
            this.eatTimer = this.rollEatTime();
        } else {
            if (this.octovine.getNavigation().isDone()) {
                if (this.octovine.distanceToSqr(this.meat) < 16.0D) {
                    this.octovine.getMoveControl().setWantedPosition(this.meat.getX(), this.meat.getY(), this.meat.getZ(), this.octovine.isInWater() ? 1.0D : 1.3D);
                } else {
                    this.octovine.getNavigation().moveTo(this.meat, this.octovine.isInWater() ? 1.0D : 1.3D);
                }
            }
            if (++this.stuckTicks > 80) {
                this.ignoredMeat = this.meat;
                this.meat = this.findMeat();
                this.stuckTicks = 0;
            }
        }
    }

    private boolean isClose() {
        double dx = this.meat.getX() - this.octovine.getX();
        double dz = this.meat.getZ() - this.octovine.getZ();
        double reach = this.octovine.getBbWidth() / 2.0D + 1.5D;
        return dx * dx + dz * dz < reach * reach && Math.abs(this.meat.getY() - this.octovine.getY()) < 2.0D;
    }

    private int rollEatTime() {
        return 40 + this.octovine.getRandom().nextInt(41);
    }

    private void eatOne() {
        this.meat.getItem().shrink(1);
        if (this.meat.getItem().isEmpty()) {
            this.meat.discard();
        }
        this.octovine.playSound(SoundEvents.GENERIC_EAT, 1.0F, 0.8F + this.octovine.getRandom().nextFloat() * 0.4F);
    }

    private ItemEntity findMeat() {
        return this.octovine.level().getEntitiesOfClass(ItemEntity.class, this.octovine.getBoundingBox().inflate(16.0D, 8.0D, 16.0D),
                        item -> item.isAlive() && item.getItem().is(Tags.Items.FOODS_RAW_MEAT) && this.octovine.hasLineOfSight(item))
                .stream()
                .min(Comparator.comparingDouble(this.octovine::distanceToSqr))
                .orElse(null);
    }
}
