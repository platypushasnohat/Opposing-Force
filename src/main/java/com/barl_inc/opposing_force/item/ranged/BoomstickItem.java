package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.item.base.BulletGunItem;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.item.ItemAnimations;
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

    public static final String RELOAD_ANIMATION = "reload";

    public BoomstickItem(Properties properties) {
        super(properties.durability(650), 0.75F, 2.5F, 14.0F, 12);
    }

    @Override
    public int getProjectileCount() {
        return PROJECTILE_COUNT;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.pass(stack);
        if (getShots(stack) < 2 || !this.canShoot(player, hand)) {
            player.push(player.getLookAngle().reverse().normalize().scale(0.65F));
            result = super.use(level, player, hand);
        } else {
            ItemAnimations.start(player, stack, RELOAD_ANIMATION);
            if (!level.isClientSide) {
                setShots(stack, 0);
                player.getCooldowns().addCooldown(this, 40);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.PLAYERS, 1.0F, 1.2F * SinewSoundUtils.randomizePitch(player));
            }
        }
        return result;
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        for (int i = 0; i < PROJECTILE_COUNT; i++) {
            super.shoot(level, player, stack, ammo);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.GUN_SHOOT, SoundSource.PLAYERS, 2.0F, 0.8F * SinewSoundUtils.randomizePitch(player));
    }

    @Override
    protected void onShot(Level level, Player player, InteractionHand hand, ItemStack stack, ItemStack ammo) {
        super.onShot(level, player, hand, stack, ammo);
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
