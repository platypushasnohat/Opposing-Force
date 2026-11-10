package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.ElectricCharge;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ElectricChargeItem extends Item implements ProjectileItem {

    public ElectricChargeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F * SinewSoundUtils.randomizePitch(level));
        if (!level.isClientSide) {
            ElectricCharge charge = new ElectricCharge(level, player);
            charge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 1.0F);
            level.addFreshEntity(charge);
        }
        player.getCooldowns().addCooldown(this, 10);
        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new ElectricCharge(level, pos.x(), pos.y(), pos.z());
    }

    @Override
    public DispenseConfig createDispenseConfig() {
        return DispenseConfig.builder().power(0.75F).build();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GRAY));
    }
}
