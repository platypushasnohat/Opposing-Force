package com.barl_inc.opposing_force.item.melee;

import com.barl_inc.opposing_force.entity.misc.TachiSlash;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class TachiItem extends SwordItem {

    public static final int FULL_CHARGE_TICKS = 20;
    private static final Map<Player, Float> SINGLE_TARGET_SWINGS = new WeakHashMap<>();

    public TachiItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.pass(stack);
        if (hand == InteractionHand.MAIN_HAND && player.getOffhandItem().isEmpty()) {
            player.startUsingItem(hand);
            result = InteractionResultHolder.consume(stack);
        }
        return result;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int held = this.getUseDuration(stack, entity) - timeLeft;
            if (level instanceof ServerLevel serverLevel) {
                slash(serverLevel, player, stack, held >= FULL_CHARGE_TICKS);
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            player.getCooldowns().addCooldown(this, 80);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    private static void slash(ServerLevel level, Player player, ItemStack stack, boolean fullyCharged) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        DamageSource source = player.damageSources().playerAttack(player);
        float baseDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float yaw = player.getYRot() * Mth.DEG_TO_RAD;
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(3.5D), target -> isSlashTarget(player, target, eye, look));
        for (LivingEntity target : targets) {
            float damage = EnchantmentHelper.modifyDamage(level, stack, target, source, baseDamage);
            if (target.hurt(source, damage)) {
                target.knockback(0.4F, Mth.sin(yaw), -Mth.cos(yaw));
                EnchantmentHelper.doPostAttackEffects(level, target, source);
            }
        }
        if (fullyCharged) {
            Vec3 forward = Vec3.directionFromRotation(0.0F, player.getYRot());
            Vec3 start = player.position().add(forward).add(0.0D, player.getBbHeight() * 0.55D - 0.25D, 0.0D);
            float slashDamage = Math.max(0.0F, 4.0F + (baseDamage - 6.0F) / 2.0F);
            level.addFreshEntity(new TachiSlash(level, player, start, player.getYRot(), slashDamage, targets));
        }
        Vec3 front = eye.add(look.scale(1.5D));
        level.sendParticles(ParticleTypes.SWEEP_ATTACK, front.x, front.y - 0.3D, front.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, fullyCharged ? 0.7F : 0.9F);
    }

    private static boolean isSlashTarget(Player player, LivingEntity target, Vec3 eye, Vec3 look) {
        Vec3 offset = target.getBoundingBox().getCenter().subtract(eye);
        boolean valid = target != player && target.isAlive() && !player.isAlliedTo(target) && !(target instanceof ArmorStand stand && stand.isMarker());
        return valid && offset.length() <= 3.5D + target.getBbWidth() / 2.0D && offset.normalize().dot(look) > 0.5D && player.hasLineOfSight(target);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!player.level().isClientSide) {
            if (willSweepOthers(stack, player, entity)) {
                SINGLE_TARGET_SWINGS.remove(player);
            } else {
                float strength = player.getAttackStrengthScale(0.5F);
                SINGLE_TARGET_SWINGS.put(player, 0.2F + strength * strength * 0.8F);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        float bonus = super.getAttackDamageBonus(target, damage, damageSource);
        if (damageSource.getEntity() instanceof Player player) {
            Float scale = SINGLE_TARGET_SWINGS.remove(player);
            if (scale != null) {
                bonus += 2.0F * scale;
            }
        }
        return bonus;
    }

    private static boolean willSweepOthers(ItemStack stack, Player player, Entity target) {
        boolean charged = player.getAttackStrengthScale(0.5F) > 0.9F;
        boolean sweeps = charged && !player.isSprinting() && player.onGround() && player.walkDist - player.walkDistO < player.getSpeed() && stack.canPerformAction(ItemAbilities.SWORD_SWEEP);
        boolean hitsOthers = false;
        if (sweeps) {
            double reach = Mth.square(player.entityInteractionRange());
            hitsOthers = !player.level().getEntitiesOfClass(LivingEntity.class, stack.getSweepHitBox(player, target), living -> living != player && living != target && !player.isAlliedTo(living) && !(living instanceof ArmorStand stand && stand.isMarker()) && player.distanceToSqr(living) < reach).isEmpty();
        }
        return hitsOthers;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(OFTooltips.description("item.opposing_force.tachi.description"));
        tooltipComponents.add(OFTooltips.description("item.opposing_force.tachi.description.two"));
    }
}
