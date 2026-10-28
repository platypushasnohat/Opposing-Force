package com.barl_inc.opposing_force.item.melee;

import com.barl_inc.opposing_force.entity.projectile.Soundwave;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class TuningBidentItem extends Item {

    private static final Map<Player, Attack> ATTACKS = new WeakHashMap<>();

    public TuningBidentItem(Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 6.5D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.8D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    private static float getDamageBonus(Player player) {
        return Math.max(0.0F, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 8.0F) / 4.0F;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!player.level().isClientSide) {
            boolean charged = player.getAttackStrengthScale(0.5F) > 0.9F;
            boolean crit = charged && player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && !player.isSprinting();
            ATTACKS.put(player, new Attack(charged, crit));
        }
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.level().isClientSide) {
            Attack attack = ATTACKS.remove(player);
            if (attack != null && attack.charged()) {
                int count = 1;
                if (attack.crit() && player.getRandom().nextBoolean()) {
                    count = 2;
                }
                Soundwave.releaseSplit(player.level(), player, target, 0, count, getDamageBonus(player));
            }
        }
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            if (!level.isClientSide) {
                int phase = 4;
                if (this.getUseDuration(stack, entity) - timeLeft >= 16) {
                    phase = 0;
                }
                Vec3 look = player.getLookAngle();
                level.addFreshEntity(new Soundwave(level, player, Soundwave.Type.BIDENT, phase, getDamageBonus(player), player.getEyePosition().add(look.scale(0.8D)), look));
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.0F, 1.2F);
            player.getCooldowns().addCooldown(this, 60);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(OFTooltips.description("item.opposing_force.tuning_bident.description"));
        tooltipComponents.add(OFTooltips.description("item.opposing_force.tuning_bident.description.two"));
    }

    private record Attack(boolean charged, boolean crit) {
    }
}
