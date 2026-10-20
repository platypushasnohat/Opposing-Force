package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.item.base.BulletGunItem;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
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

public class JaegerItem extends BulletGunItem {

    public final ItemAnimationState cockAnimationState = new ItemAnimationState();

    public JaegerItem(Properties properties) {
        super(properties.durability(650), 6.0F, 3.5F, 0.5F, 20);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.pass(stack);
        if (isCocked(stack) || !this.canShoot(player, hand)) {
            result = super.use(level, player, hand);
        } else if (level.isClientSide) {
            this.cockAnimationState.start(player.tickCount, player);
        } else {
            setCocked(stack, true);
            player.getCooldowns().addCooldown(this, this.getShotCooldown() - 5);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 1.4F * SinewSoundUtils.randomizePitch(player));
        }
        return result;
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        super.shoot(level, player, stack, ammo);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.GUN_SHOOT, SoundSource.PLAYERS, 1.5F, 0.9F * SinewSoundUtils.randomizePitch(player));
    }

    @Override
    protected void onShot(Level level, Player player, InteractionHand hand, ItemStack stack, ItemStack ammo) {
        super.onShot(level, player, hand, stack, ammo);
        setCocked(stack, false);
    }

    public static boolean isCocked(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("Cocked");
    }

    public static void setCocked(ItemStack stack, boolean cocked) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        compoundtag.putBoolean("Cocked", cocked);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundtag));
    }
}
