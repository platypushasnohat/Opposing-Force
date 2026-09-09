package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ScatterBlasterItem extends BlasterItem {

    public ScatterBlasterItem(Properties properties) {
        super(properties.stacksTo(1).durability(1280));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !(oldStack.is(OFItems.SCATTER_BLASTER)) || !(newStack.is(OFItems.SCATTER_BLASTER));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack ammoStack = this.getAmmo(player);
        if (hand == InteractionHand.OFF_HAND || (ammoStack.isEmpty() && !player.isCreative())) {
            if (!level.isClientSide) {
                player.getCooldowns().addCooldown(stack.getItem(), 10);
            }
            return InteractionResultHolder.consume(stack);
        }

        Vec3 look = player.getLookAngle();
        if (!level.isClientSide) {
            int count = 7;
            for (int i = 0; i < count; i++) {
                LaserBolt laserBolt = new LaserBolt(level, player, player.getX(), player.getY() + player.getBbHeight() * 0.8F, player.getZ());
                laserBolt.setDamage(4.5F);
                laserBolt.shoot(look.x, look.y, look.z, 1.3F, 10.0F);
                level.addFreshEntity(laserBolt);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BLASTER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 0.5F * SinewSoundUtils.randomizePitch(level));
            player.getCooldowns().addCooldown(stack.getItem(), 45);
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                ammoStack.shrink(1);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
        } else {
            this.blastAnimationState.start(player.tickCount, player);
        }

        Vec3 pushBack = new Vec3(-look.x, -look.y, -look.z).normalize();
        player.push(pushBack.scale(0.8F));
        return InteractionResultHolder.pass(stack);
    }
}
