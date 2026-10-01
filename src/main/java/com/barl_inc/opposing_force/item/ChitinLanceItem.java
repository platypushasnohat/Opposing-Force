package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFToolDefinitions;
import com.platypushasnohat.sinew.item.SinewSwordItem;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;

public class ChitinLanceItem extends SinewSwordItem {

    private int damageTime = 0;

    public ChitinLanceItem(Properties properties) {
        super(OFToolDefinitions.CHITIN, 3.0F, -2.4F, properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1 || player.isFallFlying()) {
            player.getCooldowns().addCooldown(stack.getItem(), 10);
        } else {
            player.startUsingItem(usedHand);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int useTime) {
        int timeUsed = Mth.clamp(this.getUseDuration(stack, livingEntity) - useTime, 0, 60);
        if (timeUsed >= 10) {
            float boostFactor = 0.08F * timeUsed;
            Vec3 boost = livingEntity.getDeltaMovement().add(livingEntity.getViewVector(1.0F).normalize().multiply(boostFactor, 0.0F, boostFactor));
            if (!level.isClientSide) {
                if (livingEntity instanceof Player player) {
                    player.getCooldowns().addCooldown(stack.getItem(), 40);
                }
                this.hurtNearbyEntities(level, livingEntity);
                stack.hurtAndBreak(1, livingEntity, EquipmentSlot.MAINHAND);
            }
            this.damageTime = 12;
            livingEntity.setDeltaMovement(boost.add(0, (livingEntity.onGround() ? 0.25F : 0), 0));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (entity instanceof LivingEntity living && (living.getItemBySlot(EquipmentSlot.MAINHAND).is(OFItems.CHITIN_LANCE.get()) || living.getItemBySlot(EquipmentSlot.OFFHAND).is(OFItems.CHITIN_LANCE.get()))) {
            if (this.damageTime > 0) {
                this.damageTime--;
                if (!level.isClientSide) {
                    if (level instanceof ServerLevel serverLevel) {
                        double xSpeed = living.getRandom().nextGaussian() * 0.02D;
                        double ySpeed = living.getRandom().nextGaussian() * 0.02D;
                        double zSpeed = living.getRandom().nextGaussian() * 0.02D;
                        serverLevel.sendParticles(ParticleTypes.CLOUD, living.getRandomX(1.0D), living.getRandomY(), living.getRandomZ(1.0D), 1, xSpeed, ySpeed, zSpeed, 0.1D);
                    }
                    this.hurtNearbyEntities(level, living);
                }
            }
        } else {
            this.damageTime = 0;
        }
    }

    private void hurtNearbyEntities(Level level, LivingEntity living) {
        AABB aabb = living.getBoundingBox().move(living.getLookAngle().normalize().scale(1.25D)).inflate(0.5D, 0.0D, 0.5D);
        DamageSource damageSource = living.damageSources().mobAttack(living);
        float yawRad = living.getYRot() * Mth.DEG_TO_RAD;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
            if (!living.isAlliedTo(target) && !living.equals(target) && living.hasLineOfSight(target)) {
                if (target.hurt(damageSource, 6.0F)) {
                    target.stopRiding();
                    target.knockback(3.0F, Mth.sin(yawRad), -Mth.cos(yawRad));
                    living.level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, living.getSoundSource(), 1.0F, 0.9F * SinewSoundUtils.randomizePitch(level));
                    if (target instanceof Player player && target.getUseItem().canPerformAction(ItemAbilities.SHIELD_BLOCK)) {
                        player.disableShield();
                    }
                }
            }
        }
    }
}
