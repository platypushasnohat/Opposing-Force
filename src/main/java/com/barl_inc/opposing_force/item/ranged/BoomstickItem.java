package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.item.base.BulletGunItem;
import com.platypushasnohat.sinew.client.animation.ItemAnimationState;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class BoomstickItem extends BulletGunItem {

    private static final int PROJECTILE_COUNT = 10;

    public final ItemAnimationState reloadAnimationState = new ItemAnimationState();

    public BoomstickItem(Properties properties) {
        super(properties.durability(650), 1.0F, 2.8F, 10.0F, 10);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.pass(stack);
        if (getShots(stack) < 2 || !this.canShoot(player, hand)) {
            result = super.use(level, player, hand);
        } else if (level.isClientSide) {
            this.reloadAnimationState.start(player.tickCount, player);
        } else {
            setShots(stack, 0);
            player.getCooldowns().addCooldown(this, 30);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.PLAYERS, 1.0F, 1.2F * SinewSoundUtils.randomizePitch(level));
        }
        return result;
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        for (int i = 0; i < PROJECTILE_COUNT; i++) {
            super.shoot(level, player, stack, ammo);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.6F, 1.4F * SinewSoundUtils.randomizePitch(level));
    }

    @Override
    protected void onShot(Player player, InteractionHand hand, ItemStack stack, ItemStack ammo) {
        super.onShot(player, hand, stack, ammo);
        setShots(stack, getShots(stack) + 1);
    }

    public static int getShots(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("Shots");
    }

    public static void setShots(ItemStack stack, int shots) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        compoundtag.putInt("Shots", shots);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundtag));
    }
}
