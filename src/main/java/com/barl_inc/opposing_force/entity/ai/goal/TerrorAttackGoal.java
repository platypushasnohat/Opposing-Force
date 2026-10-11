package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Terror;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TerrorAttackGoal extends AttackGoal {

    private final Terror terror;
    private int attackCooldown;
    private int chargeCount;
    private int maxChargeCount;

    public TerrorAttackGoal(Terror terror) {
        super(terror);
        this.terror = terror;
    }

    @Override
    public void start() {
        super.start();
        this.terror.setSprinting(false);
        this.attackCooldown();
        this.chargeCount = 0;
        this.maxChargeCount = 1 + this.terror.getRandom().nextInt(2);
    }

    @Override
    public void stop() {
        super.stop();
        this.terror.setSprinting(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.terror.getTarget();
        if (target != null) {
            double distance = this.terror.distanceToSqr(target);
            if (this.attackState == 1) {
                this.terror.getNavigation().stop();
                this.tickCharge(target);
            }
            else if (this.attackState == 2) {
                this.terror.getNavigation().stop();
                this.tickStopCharge();
            }
            else {
                if (this.terror.getAnimationState() != Terror.GROW_LEGS_ANIMATION) {
                    this.lookAtTarget(target, 25.0F, 25.0F);
                    this.terror.getNavigation().moveTo(target, 1.3D);
                    if (this.attackCooldown > 0) {
                        this.attackCooldown--;
                    }
                    if (this.attackCooldown <= 0 && distance <= 100) {
                        this.attackState = 1;
                    }
                } else {
                    this.terror.getNavigation().stop();
                }
            }
        }
    }

    private void tickCharge(LivingEntity target) {
        this.timer++;
        double distance = this.terror.distanceTo(target);
        if (this.timer == 1) {
            this.terror.playSound(OFSoundEvents.TERROR_SAW_START.get(), 1.0F, SinewSoundUtils.randomizePitch(this.terror));
            this.terror.setAnimationState(Terror.ATTACK_ANIMATION);
        }
        if (this.timer == 15) {
            this.terror.setSprinting(true);
        }
        if (this.timer <= 15) {
            this.lookAtTarget(target, 45.0F, 45.0F);
        }
        if (this.timer > 15) {
            Vec3 chargeDirection = new Vec3(target.getX() - this.terror.getX(), target.getY() - this.terror.getY(), target.getZ() - this.terror.getZ()).normalize();
            float desiredYaw = (float) (Mth.atan2(chargeDirection.z, chargeDirection.x) * Mth.RAD_TO_DEG) - 90.0F;
            this.terror.setYRot(Mth.approachDegrees(this.terror.getYRot(), desiredYaw, 0.5F));
            this.terror.yBodyRot = this.terror.getYRot();
            this.terror.yHeadRot = this.terror.getYRot();
            float yawRad = this.terror.getYRot() * Mth.DEG_TO_RAD;
            float speed = 0.4F;
            Vec3 forward = new Vec3(-Mth.sin(yawRad), this.terror.isInWater() ? chargeDirection.y : 0.0F, Mth.cos(yawRad));
            this.terror.setDeltaMovement(forward.multiply(speed, 0.04F, speed).add(0.0F, this.terror.getDeltaMovement().y, 0.0F));
            if (this.terror.tickCount % 5 == 0) {
                this.hurtNearbyEntities();
            }
        }
        BlockHitResult hitResult = this.terror.level().clip(new ClipContext(this.terror.position().add(0.0F, this.terror.getBbHeight() - 0.2F, 0.0F), this.terror.position().add(0.0F, this.terror.getBbHeight() - 0.2F, 0.0F).add(this.terror.getLookAngle().scale(1.1D)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.terror));
        BlockPos hitPos = hitResult.getBlockPos();
        BlockState state = this.terror.level().getBlockState(hitPos);
        SoundType soundType = state.getSoundType(this.terror.level(), hitPos, this.terror);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.terror.level().playSound(null, hitPos.getX(), hitPos.getY(), hitPos.getZ(), soundType.getBreakSound(), this.terror.getSoundSource(), 1.0F, 0.9F);
            this.timer = 0;
            this.terror.setSprinting(false);
            this.attackState = 2;
        }
        if (this.timer >= 5 && this.terror.getAnimationState() == Terror.GROW_LEGS_ANIMATION) {
            this.timer = 0;
            this.terror.setSprinting(false);
            this.attackCooldown();
            this.attackState = 0;
        }
        if (this.timer > 50 || (this.timer > 22 && distance > 10)) {
            this.timer = 0;
            this.terror.setSprinting(false);
            this.terror.setAnimationState(0);
            this.attackState = this.chargeCount >= this.maxChargeCount ? 2 : 1;
            this.chargeCount++;
        }
    }

    private void tickStopCharge() {
        this.timer++;
        if (this.timer == 1) {
            this.terror.playSound(OFSoundEvents.TERROR_SAW_END.get(), 1.0F, SinewSoundUtils.randomizePitch(this.terror));
            this.terror.setAnimationState(Terror.COOLDOWN_ANIMATION);
        }
        if (this.timer > 50) {
            this.terror.setAnimationState(0);
            this.timer = 0;
            this.attackCooldown();
            this.attackState = 0;
            this.chargeCount = 0;
            this.maxChargeCount = 1 + this.terror.getRandom().nextInt(2);
        }
    }

    private void attackCooldown() {
        this.attackCooldown = 10 + this.terror.getRandom().nextInt(10);
    }

    private void hurtNearbyEntities() {
        AABB attackBox = this.terror.getBoundingBox().move(this.terror.getLookAngle().normalize()).inflate(0.2D, 0.0D, 0.2D);
        List<LivingEntity> nearbyEntities = this.terror.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.terror, attackBox);
        if (!nearbyEntities.isEmpty()) {
            nearbyEntities.stream().filter(entity -> entity != this.terror).limit(4).forEach(entity -> {
                float damage = (float) this.terror.getAttributeValue(Attributes.ATTACK_DAMAGE);
                entity.hurt(OFDamageTypes.causeSawDamage(this.terror.level().registryAccess(), this.terror), damage);
                float yawRad = this.terror.getYRot() * Mth.DEG_TO_RAD;
                entity.knockback(0.5F, Mth.sin(yawRad), -Mth.cos(yawRad));
                if (entity.isDamageSourceBlocked(this.terror.damageSources().mobAttack(this.terror)) && entity instanceof Player player) {
                    player.disableShield();
                    player.knockback(0.25F, Mth.sin(yawRad), (-Mth.cos(yawRad)));
                    player.hurtMarked = true;
                    this.terror.addDeltaMovement(new Vec3(0, 0.25D, 0));
                    this.terror.addDeltaMovement(this.terror.getLookAngle().scale(1.0D).multiply(-0.5D, 0, -0.5D));
                    this.timer = 0;
                    this.terror.setSprinting(false);
                    this.attackState = 2;
                }
            });
        }
    }
}