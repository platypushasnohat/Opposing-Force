package com.barl_inc.opposing_force.item.base;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.entity.base.SummonableMonster;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class SummonWeaponItem extends Item {

    private final Supplier<? extends EntityType<? extends SummonableMonster>> summonType;
    private final int cooldown;

    public SummonWeaponItem(Properties properties, Supplier<? extends EntityType<? extends SummonableMonster>> summonType, double damage, double duration, int cooldown) {
        super(properties.attributes(ItemAttributeModifiers.builder()
                .add(SinewAttributes.SUMMON_DAMAGE, new AttributeModifier(OpposingForce.location("summon_damage"), damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(SinewAttributes.SUMMON_DURATION, new AttributeModifier(OpposingForce.location("summon_duration"), duration, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build()));
        this.summonType = summonType;
        this.cooldown = cooldown;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            SummonableMonster summon = SummonableMonster.summon(this.summonType.get(), serverLevel, player, stack, hand, player.getEyePosition().add(player.getLookAngle()));
            if (summon == null) {
                player.displayClientMessage(Component.translatable("message.sinew.not_enough_minion_slots"), true);
            } else {
                summon.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.8F, 1.0F);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                player.awardStat(Stats.ITEM_USED.get(this));
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 0.6F, 1.4F + level.getRandom().nextFloat() * 0.3F);
            }
        }
        player.getCooldowns().addCooldown(this, this.cooldown);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GRAY));
    }
}
