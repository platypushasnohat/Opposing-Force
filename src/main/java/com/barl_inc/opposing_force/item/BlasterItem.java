package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.ItemAnimationState;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlasterItem extends Item {

    public final ItemAnimationState blastAnimationState = new ItemAnimationState();

    public BlasterItem(Properties properties) {
        super(properties.stacksTo(1).durability(1024));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !(oldStack.is(OFItems.BLASTER)) || !(newStack.is(OFItems.BLASTER));
    }

    public ItemStack getAmmo(Player player) {
        if (player.isCreative()) {
            return ItemStack.EMPTY;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(OFItems.POWER_CELL) && this.isPowerCellFilled(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void consumePower(ItemStack ammoStack) {
        if (ammoStack.getItem() instanceof PoweredItem poweredItem) {
            poweredItem.drainPower(ammoStack, 1, null);
        }
    }

    public boolean isPowerCellFilled(ItemStack ammoStack) {
        return ammoStack.getItem() instanceof PoweredItem poweredItem && poweredItem.getPower(ammoStack) < poweredItem.getMaxPower(ammoStack);
    }

    public boolean powerDepleted(Player player, ItemStack ammoStack) {
        return this.getAmmo(player).isEmpty() || !this.isPowerCellFilled(ammoStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack ammoStack = this.getAmmo(player);
        if (this.powerDepleted(player, ammoStack) && !player.isCreative()) {
            if (!level.isClientSide) {
                player.getCooldowns().addCooldown(stack.getItem(), 10);
            }
            return InteractionResultHolder.consume(stack);
        }
        if (level.isClientSide) {
            this.blastAnimationState.start(player.tickCount, player);
        }
        else {
            LaserBolt laserBolt = new LaserBolt(level, player, player.getX(), player.getY() + player.getBbHeight() * 0.8F, player.getZ());
            laserBolt.setDamage(6.0F);
            Vec3 look = player.getLookAngle();
            laserBolt.shoot(look.x, look.y, look.z, 1.4F, 1.0F);
            level.addFreshEntity(laserBolt);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BLASTER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(level));
            player.getCooldowns().addCooldown(stack.getItem(), 8);
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                this.consumePower(ammoStack);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
        }
        return InteractionResultHolder.pass(stack);
    }
}
