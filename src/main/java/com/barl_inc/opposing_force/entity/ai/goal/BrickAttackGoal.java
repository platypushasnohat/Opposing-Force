package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Brick;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
    public boolean canUse() {
        return !this.brick.isTame() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.defendCooldown = 40 + this.brick.getRandom().nextInt(40);
        this.pathCooldown = 0;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.brick.getTarget();
        double followRange = this.brick.getAttributeValue(Attributes.FOLLOW_RANGE);
        return super.canContinueToUse() && target != null && this.brick.distanceToSqr(target) <= followRange * followRange;
    }

    @Override
    public void stop() {
        super.stop();
        if (this.brick.getAnimationState() == Brick.DEFEND_ANIMATION) {
            this.brick.setAnimationState(0);
        }
        this.attackState = 0;
        this.timer = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.brick.getTarget();
        if (target != null) {
            if (this.attackState == 1) {
                this.tickDefend();
            } else {
                this.tickGuard(target);
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
        this.lookAtTarget(target, 0.9F, 30.0F);
        if (this.defendCooldown <= 0 && distance <= 10.0D * 10.0D && this.brick.onGround() && this.getNearbyMonsters().size() > 1) {
            this.attackState = 1;
            this.timer = 0;
        }
        else {
            Mob ally = this.getProtectedAlly(target);
            if (ally != null) {
                if (this.pathCooldown <= 0 && this.brick.distanceToSqr(ally) > ally.getBbWidth() * 0.5D) {
                    this.brick.getNavigation().moveTo(ally, 1.1D);
                    this.pathCooldown = 5;
                }
            }
            else {
                if (this.pathCooldown <= 0 && distance > target.getBbWidth() * 0.5D) {
                    this.brick.getNavigation().moveTo(target, 1.1D);
                    this.pathCooldown = 5;
                }
            }
        }
    }

    // stand between the target and the monster
    private Vec3 getGuardPosition(LivingEntity target) {
        Mob ally = this.getProtectedAlly(target);
        Vec3 from = ally != null ? ally.position() : this.brick.position();
        Vec3 offset = new Vec3(from.x - target.getX(), 0.0D, from.z - target.getZ());
        double length = offset.length();
        Vec3 direction = length > 1.0E-4D ? offset.scale(1.0D / length) : Vec3.ZERO;
        double guardDistance = this.brick.getBbWidth() * 0.75D + target.getBbWidth() * 0.75D + 0.2D;
        return target.position().add(direction.scale(guardDistance));
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
        return this.brick.level().getEntitiesOfClass(LivingEntity.class, this.brick.getBoundingBox().inflate(8.0D), entity -> entity.isAlive() && entity instanceof Enemy && !(entity instanceof TamableAnimal tamable && tamable.isTame()));
    }

    private void tickDefend() {
        this.timer++;
        this.brick.getNavigation().stop();
        if (this.timer == 1) {
            this.brick.setAnimationState(Brick.DEFEND_ANIMATION);
        }
        if (this.timer % 20 == 1) {
            this.brick.level().playSound(null, this.brick.getX(), this.brick.getY(), this.brick.getZ(), SoundEvents.EVOKER_CAST_SPELL, this.brick.getSoundSource(), 0.5F, 1.25F * SinewSoundUtils.randomizePitch(this.brick));
            for (LivingEntity entity : this.getNearbyMonsters()) {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 2), this.brick);
            }
            if (this.brick.level() instanceof ServerLevel serverLevel) {
                this.brick.spawnSporeParticles(serverLevel, 8);
            }
        }
        if (this.timer > 200) {
            this.brick.setAnimationState(0);
            this.defendCooldown = 100 + this.brick.getRandom().nextInt(100);
            this.attackState = 0;
            this.timer = 0;
        }
    }
}
