package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.barl_inc.opposing_force.registry.OFToolDefinitions;
import com.platypushasnohat.sinew.item.SinewAxeItem;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BuzzsawItem extends SinewAxeItem {

    public BuzzsawItem(Properties properties) {
        super(OFToolDefinitions.BUZZSAW, 2.5F, -3.0F, properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.is(OFItems.BUZZSAW.get()) || !newStack.is(OFItems.BUZZSAW.get());
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 200;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BUZZSAW_START, SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(player));
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof Player player && player.tickCount % 5 == 0) {
            this.hurtNearbyEntities(stack, player, level);
        }
        if (level.isClientSide) {
            OpposingForce.PROXY.playSound(entity, (byte) 4);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        boolean using = entity instanceof LivingEntity living && living.getUseItem().equals(stack);
        int useTime = getUseTime(stack);
        if (level.isClientSide) {
            CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            if (compoundTag.getInt("PrevUseTime") != compoundTag.getInt("UseTime")) {
                compoundTag.putInt("PrevUseTime", getUseTime(stack));
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
            }
            if (using && useTime < 5.0F) {
                setUseTime(stack, useTime + 1);
            }
            if (!using && useTime > 0.0F) {
                setUseTime(stack, useTime - 1);
            }
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player) {
            OpposingForce.PROXY.clearSoundCacheFor(player);
            player.getCooldowns().addCooldown(this, 50);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BUZZSAW_END, SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(player));
        }
    }

    private void hurtNearbyEntities(ItemStack stack, Player player, Level level) {
        AABB attackBox = player.getBoundingBox().move(player.getLookAngle().normalize().scale(2.2D)).inflate(0.9D, 0.0D, 0.9D);
        List<LivingEntity> nearbyEntities = player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), player, attackBox);
        for (LivingEntity target : nearbyEntities) {
            if (player.hasLineOfSight(target)) {
                float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F;
                DamageSource damageSource = OFDamageTypes.causeSawDamage(level.registryAccess(), player);
                if (level instanceof ServerLevel serverlevel) {
                    damage = EnchantmentHelper.modifyDamage(serverlevel, stack, target, damageSource, damage) * 0.5F;
                }
                if (level.isClientSide) {
                    this.spawnParticles(level, target);
                } else {
                    if (target.hurt(damageSource, damage)) {
                        float knockback = this.getKnockback(stack, level, player, target, damageSource);
                        if (knockback > 0.0F) {
                            target.knockback(knockback * 0.2F, Mth.sin(player.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(player.getYRot() * Mth.DEG_TO_RAD));
                            target.setDeltaMovement(target.getDeltaMovement().multiply(0.6F, 1.0F, 0.6F));
                        }
                        if (level instanceof ServerLevel serverLevel) {
                            EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource);
                        }
                    }
                }
            }
            break;
        }
    }

    private void spawnParticles(Level level, LivingEntity target) {
        double x = target.getX() + level.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
        double y = target.getY() + level.getRandom().nextFloat() * target.getBbHeight();
        double z = target.getZ() + level.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
        level.addParticle(ParticleTypes.CRIT, x, y, z, 0.025D, 0.025D, 0.025D);
    }

    private float getKnockback(ItemStack stack, Level level, Player player, LivingEntity target, DamageSource damageSource) {
        float playerKnockback = (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK) * 0.2F;
        float knockback;
        if (level instanceof ServerLevel serverlevel) {
            knockback = EnchantmentHelper.modifyKnockback(serverlevel, stack, target, damageSource, playerKnockback);
        } else {
            knockback = playerKnockback;
        }
        return knockback;
    }

    public static int getUseTime(ItemStack stack) {
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return compoundTag.getInt("UseTime");
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        compoundTag.putInt("PrevUseTime", getUseTime(stack));
        compoundTag.putInt("UseTime", useTime);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }

    public static float getLerpedUseTime(ItemStack stack, float partialTicks) {
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        float prev = (float) compoundTag.getInt("PrevUseTime");
        float current = (float) compoundTag.getInt("UseTime");
        return prev + partialTicks * (current - prev);
    }
}
