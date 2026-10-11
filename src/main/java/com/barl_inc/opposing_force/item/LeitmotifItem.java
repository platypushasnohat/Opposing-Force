package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.Mudball;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.client.animation.ItemAnimationState;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeitmotifItem extends Item {

    public final ItemAnimationState shootAnimationState = new ItemAnimationState();

    public LeitmotifItem(Properties properties) {
        super(properties.stacksTo(1).durability(512));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !(oldStack.is(OFItems.LEITMOTIF)) || !(newStack.is(OFItems.LEITMOTIF));
    }

    public ItemStack getAmmo(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(Items.MUD)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
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
        if (level.isClientSide) {
            this.shootAnimationState.start(player.tickCount, player);
        } else {
            this.shootMudball(level, player);
            player.getCooldowns().addCooldown(stack.getItem(), 10);
            if (!player.getAbilities().instabuild) {
                ammoStack.shrink(1);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    private void shootMudball(Level level, Player player) {
        Mudball mudball = new Mudball(level, player, player.getX(), player.getY() + player.getBbHeight() * 0.8F, player.getZ());
        Vec3 look = player.getLookAngle();
        mudball.setDamage(3.5F);
        mudball.shoot(look.x, look.y, look.z, 1.5F, 2.0F);
        level.addFreshEntity(mudball);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS, 1.0F, 0.6F * SinewSoundUtils.randomizePitch(level));
    }
}
