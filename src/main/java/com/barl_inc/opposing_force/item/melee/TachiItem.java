package com.barl_inc.opposing_force.item.melee;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.Map;
import java.util.WeakHashMap;

public class TachiItem extends SwordItem {

    private static final float SINGLE_TARGET_BONUS = 2.0F;
    private static final Map<Player, Float> SINGLE_TARGET_SWINGS = new WeakHashMap<>();

    public TachiItem(Tier tier, Properties properties) {
        super(tier, properties);
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
                bonus += SINGLE_TARGET_BONUS * scale;
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
}
