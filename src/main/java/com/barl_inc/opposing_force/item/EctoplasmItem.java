package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.Soul;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class EctoplasmItem extends Item {

    public EctoplasmItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.PLAYERS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        if (!level.isClientSide) {
            Vec3 look = player.getLookAngle();
            Vec3 position = player.getEyePosition().add(look.scale(0.5D)).subtract(0.0D, 0.1875D, 0.0D);
            Soul soul = new Soul(level, player, this.getTarget(player), position, 3.0F, 0.6F);
            soul.setDeltaMovement(look.scale(0.6D));
            level.addFreshEntity(soul);
        }
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Nullable
    private LivingEntity getTarget(Player player) {
        LivingEntity target = player.getLastHurtMob();
        return target != null && target.isAlive() && target != player && player.tickCount - player.getLastHurtMobTimestamp() < 100 ? target : null;
    }
}
