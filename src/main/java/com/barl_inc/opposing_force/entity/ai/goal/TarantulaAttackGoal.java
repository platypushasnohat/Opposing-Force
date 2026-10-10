package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Tarantula;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

public class TarantulaAttackGoal extends AttackGoal {

    private final Tarantula tarantula;
    private int attackCooldown;
    private int slamCooldown;
    private int jumpCooldown;

    public TarantulaAttackGoal(Tarantula tarantula) {
        super(tarantula);
        this.tarantula = tarantula;
    }

    @Override
    public void start() {
        super.start();
        this.attackCooldown = 0;
        this.slamCooldown = 50;
        this.jumpCooldown = 20;
    }

    @Override
    public void tick() {
        LivingEntity target = this.tarantula.getTarget();
        if (target != null) {
            double distance = this.tarantula.distanceToSqr(target);
            if (this.attackState != 3) {
                this.lookAtTarget(target, 10.0F, 10.0F);
            }
            if (this.attackState > 0) {
                this.tarantula.getNavigation().stop();
            }
            if (this.attackState == 1) {
                this.tickSwipeAttack();
            }
            else if (this.attackState == 2) {
                this.tickSlam();
            }
            else if (this.attackState == 3) {
                this.tickJump(target);
            }
            else {
                this.lookAtTarget(target, 10.0F, 10.0F);
                this.tarantula.getNavigation().moveTo(target, 1.3D);
                if (this.attackCooldown > 0) {
                    this.attackCooldown--;
                }
                if (this.slamCooldown > 0) {
                    this.slamCooldown--;
                }
                if (this.jumpCooldown > 0) {
                    this.jumpCooldown--;
                }
                this.chooseAttackState(target, distance);
            }
        }
    }

    private void chooseAttackState(LivingEntity target, double distance) {
        if (this.attackCooldown <= 0 && this.slamCooldown > 0 && distance <= this.getAttackReachSqr(target, 1.1D)) {
            this.attackState = 1;
        }
        else if (this.slamCooldown <= 0 && distance <= this.getAttackReachSqr(target, 1.2D)) {
            this.attackState = 2;
        }
        else if (this.jumpCooldown <= 0 && distance >= 42 && distance < 100 && this.isWithinYRange(target, 1) && this.isPathClear(target)) {
            this.attackState = 3;
        }
    }

    private void tickSwipeAttack() {
        this.timer++;
        if (this.timer == 1) {
            this.tarantula.setAnimationState(Tarantula.ATTACK_ANIMATION);
        }
        if (this.timer == 13) {
            this.tarantula.hurtNearbyEntities(this.tarantula.getSwipeAttackBox(), 1.0F, 1.0F, 3, false);
        }
        if (this.timer > 20) {
            this.tarantula.setAnimationState(0);
            this.timer = 0;
            this.attackCooldown();
            this.attackState = 0;
        }
    }

    private void tickSlam() {
        this.timer++;
        if (this.timer == 1) {
            this.tarantula.setAnimationState(Tarantula.SLAM_ANIMATION);
        }
        if (this.timer == 23) {
            this.tarantula.hurtNearbyEntities(this.tarantula.getSlamAttackBox(), 1.5F, 1.5F, 8, true);
        }
        if (this.timer > 40) {
            this.tarantula.setAnimationState(0);
            this.timer = 0;
            this.slamCooldown = 70 + this.tarantula.getRandom().nextInt(50);
            this.attackState = 0;
        }
    }

    private void tickJump(LivingEntity target) {
        this.timer++;
        if (this.timer <= 14) {
            this.lookAtTarget(target, 25.0F, 25.0F);
        }
        Vec3 deltaMovement = this.tarantula.getDeltaMovement();
        Vec3 jumpVec = new Vec3(target.getX() - this.tarantula.getX(), 0.0F, target.getZ() - this.tarantula.getZ());
        if (jumpVec.lengthSqr() > 1.0E-7D) {
            jumpVec = jumpVec.normalize().scale(1.6D).add(deltaMovement);
        }
        if (this.timer == 15 && this.tarantula.onGround()) {
            this.tarantula.setAnimationState(Tarantula.JUMP_ANIMATION);
            this.tarantula.setDeltaMovement(jumpVec.x, 0.5F, jumpVec.z);
            CommonHooks.onLivingJump(this.tarantula);
        }
        boolean stopJump = this.tarantula.onGround() || this.tarantula.onClimbable() || this.tarantula.isInWaterOrBubble();
        if (this.timer > 60 || (this.timer > 15 && stopJump)) {
            this.tarantula.setAnimationState(0);
            this.timer = 0;
            this.jumpCooldown = 100 + this.tarantula.getRandom().nextInt(100);
            this.attackCooldown();
            this.slamCooldown = 0;
            this.attackState = 0;
        }
    }

    private void attackCooldown() {
        this.attackCooldown = 10 + this.tarantula.getRandom().nextInt(5);
    }

    private boolean isPathClear(LivingEntity target) {
        double dx = target.getZ() - this.tarantula.getZ();
        double dz = target.getX() - this.tarantula.getX();
        double d2 = dx / dz;
        for (int i = 0; i < 6; i++) {
            double d3 = d2 == 0.0D ? 0.0D : dx * (i / 6.0D);
            double d4 = d2 == 0.0D ? dz * (i / 6.0D) : d3 / d2;
            for (int j = 1; j < 4; j++) {
                if (!this.tarantula.level().getBlockState(BlockPos.containing(this.tarantula.getX() + d4, this.tarantula.getY() + (double) j, this.tarantula.getZ() + d3)).canBeReplaced()) {
                    return false;
                }
            }
        }
        return true;
    }
}
