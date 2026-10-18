package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.entity.projectile.Shrapnel;
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
import net.minecraft.world.phys.Vec3;

public class BoomstickItem extends BulletGunItem {

    public final ItemAnimationState reloadAnimationState = new ItemAnimationState();

    public BoomstickItem(Properties properties) {
        super(properties.durability(640), 1.0F, 2.5F, 20.0F, 10);
    }

    @Override
    public boolean hasAmmo(Player player) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (this.isAmmo(stack)) {
                count += stack.getCount();
            }
        }
        return player.hasInfiniteMaterials() || count >= 2;
    }

    @Override
    public void consumeAmmo(Player player, ItemStack stack, ItemStack ammo) {
        super.consumeAmmo(player, stack, ammo);
        super.consumeAmmo(player, stack, this.getAmmo(player));
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
            player.getCooldowns().addCooldown(this, 25);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.PLAYERS, 1.0F, 1.2F * SinewSoundUtils.randomizePitch(level));
        }
        return result;
    }

    @Override
    protected void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        Vec3 look = player.getLookAngle();
        int count = 10;
        for (int i = 0; i < count; i++) {
            Shrapnel shrapnel = new Shrapnel(level, player, ammo.copyWithCount(1));
            shrapnel.setDamage(this.getBulletDamage(ammo));
            shrapnel.setProjectileCount(count);
            shrapnel.setLifespan(this.getBulletLifespan());
            shrapnel.shoot(look.x, look.y, look.z, this.getVelocity(ammo), this.getInaccuracy(ammo));
            level.addFreshEntity(shrapnel);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.6F, 1.4F * SinewSoundUtils.randomizePitch(level));
    }

    @Override
    protected void onShot(Player player, InteractionHand hand, ItemStack stack, ItemStack ammo) {
        super.onShot(player, hand, stack, ammo);
        setShots(stack, getShots(stack) + 1);
    }

    @Override
    protected int getBulletLifespan() {
        return 10;
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
