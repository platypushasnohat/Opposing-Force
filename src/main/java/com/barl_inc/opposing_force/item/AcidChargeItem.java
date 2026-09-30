package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.AcidCharge;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
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
import net.minecraft.world.level.Level;

public class AcidChargeItem extends Item implements ProjectileItem {

    public AcidChargeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F * SinewSoundUtils.randomizePitch(level));
        if (!level.isClientSide) {
            AcidCharge acidCharge = new AcidCharge(level, player);
            acidCharge.setItem(stack);
            acidCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 1.0F);
            level.addFreshEntity(acidCharge);
        }
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        AcidCharge acidCharge = new AcidCharge(level, pos.x(), pos.y(), pos.z());
        acidCharge.setItem(stack);
        return acidCharge;
    }
}