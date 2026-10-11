package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Brick;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class BrickAttackGoal extends AttackGoal {

    private final Brick brick;
    private int defendCooldown;
    private int pathCooldown;

    public BrickAttackGoal(Brick brick) {
        super(brick);
        this.brick = brick;
    }

    @Override
    public void start() {
        super.start();
        this.defendCooldown = 60 + this.brick.getRandom().nextInt(60);
        this.pathCooldown = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.brick.getTarget();
        if (target != null) {
            switch (this.attackState) {
                case 1 -> this.tickDefendStart(target);
                case 2 -> this.tickDefend(target);
                default -> this.tickGuard(target);
            }
        }
    }

    private void tickGuard(LivingEntity target) {
        double distance = this.brick.distanceToSqr(target);
        if (this.defendCooldown > 0) {
            this.defendCooldown--;
        }
        if (this.pathCooldown > 0) {
            this.pathCooldown--;
        }
        this.lookAtTarget(target, 25.0F, 25.0F);
        if (this.defendCooldown <= 0 && distance <= 10.0D * 10.0D && this.brick.onGround() && this.getNearbyMonsters().size() > 1) {
            this.setState(1);
        }
        else if (this.pathCooldown <= 0) {
            Vec3 guardPos = this.getGuardPos(target);
            if (this.brick.distanceToSqr(guardPos) > 1.0D) {
                this.brick.getNavigation().moveTo(guardPos.x, guardPos.y, guardPos.z, 1.4D);
            }
            else {
                this.brick.getNavigation().stop();
            }
            this.pathCooldown = 5;
        }
    }

    //moves between player and mob
    private Vec3 getGuardPos(LivingEntity target) {
        Mob ally = this.getProtectedAlly(target);
        Vec3 from = ally != null ? ally.position() : this.brick.position();
        Vec3 offset = new Vec3(from.x - target.getX(), 0.0D, from.z - target.getZ());
        double length = offset.length();
        Vec3 direction = length > 1.0E-4D ? offset.scale(1.0D / length) : Vec3.ZERO;
        return target.position().add(direction.scale(2.5D));
    }

    @Nullable
    private Mob getProtectedAlly(LivingEntity target) {
        Mob closest = null;
        double closestDistance = Double.MAX_VALUE;
        List<Mob> allies = this.brick.level().getEntitiesOfClass(Mob.class, target.getBoundingBox().inflate(12.0D), mob -> this.canProtect(mob, target));
        for (Mob ally : allies) {
            double distance = ally.distanceToSqr(target);
            if (distance < closestDistance) {
                closest = ally;
                closestDistance = distance;
            }
        }
        return closest;
    }

    private boolean canProtect(Mob mob, LivingEntity target) {
        return mob != this.brick && mob.isAlive() && mob instanceof Enemy && !(mob instanceof Brick) && mob.getTarget() == target;
    }

    private List<LivingEntity> getNearbyMonsters() {
        return this.brick.level().getEntitiesOfClass(LivingEntity.class, this.brick.getBoundingBox().inflate(8.0D), entity -> entity.isAlive() && entity instanceof Enemy);
    }

    private void tickDefendStart(LivingEntity target) {
        this.timer++;
        this.brick.getNavigation().stop();
        this.lookAtTarget(target, 30.0F, 30.0F);
        if (this.timer == 1) {
            this.brick.setAnimationState(Brick.DEFEND_START_ANIMATION);
        }
        if (this.timer > 15) {
            this.brick.setAnimationState(Brick.DEFEND_ANIMATION);
            this.setState(2);
        }
    }

    private void tickDefend(LivingEntity target) {
        this.timer++;
        this.brick.getNavigation().stop();
        this.lookAtTarget(target, 30.0F, 30.0F);
        if (this.timer % 10 == 1) {
            for (LivingEntity entity : this.getNearbyMonsters()) {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 0), this.brick);
            }
        }
        if (this.timer > 100) {
            this.brick.setAnimationState(0);
            this.defendCooldown = 200 + this.brick.getRandom().nextInt(100);
            this.setState(0);
        }
    }

    private void setState(int state) {
        this.attackState = state;
        this.timer = 0;
    }
}
