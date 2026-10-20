package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class TriBlasterItem extends BlasterItem {

    public TriBlasterItem(Properties properties) {
        super(properties, 1280);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    protected boolean canShoot(Player player, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND && super.canShoot(player, hand);
    }

    @Override
    public int getCooldown(ItemStack stack, ItemStack ammo) {
        return 16;
    }

    @Override
    protected boolean canDualWield() {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.pass(stack);
        if (!this.canShoot(player, hand)) {
            if (!level.isClientSide) {
                player.getCooldowns().addCooldown(this, 10);
            }
            result = InteractionResultHolder.consume(stack);
        } else if (getShootTime(stack) == 0 && !isShooting(stack)) {
            setShooting(stack, true);
        }
        return result;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        boolean shooting = isShooting(stack);
        int shootTime = getShootTime(stack);
        if (isSelected && entity instanceof Player player) {
            if (shooting && (shootTime == 1 || shootTime == 5 || shootTime == 9)) {
                if (level.isClientSide) {
                    this.shootAnimationState.start(player.tickCount, player);
                } else {
                    ItemStack ammo = this.getAmmo(player);
                    this.shoot(level, player, stack, ammo);
                    if (shootTime == 1) {
                        this.onShot(level, player, InteractionHand.MAIN_HAND, stack, ammo);
                    }
                }
                if (shootTime == 9) {
                    setShooting(stack, false);
                }
            }
            if (shooting && shootTime <= 9) {
                setShootTime(stack, shootTime + 1);
            }
            if (!shooting && shootTime > 0) {
                setShootTime(stack, 0);
            }
        } else {
            setShooting(stack, false);
        }
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        LaserBolt laserBolt = new LaserBolt(player, level, stack);
        laserBolt.setDamage(7.0F);
        laserBolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3.25F, 0.5F);
        level.addFreshEntity(laserBolt);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BLASTER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 0.8F * SinewSoundUtils.randomizePitch(level));
    }

    public static int getShootTime(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return compoundtag.getInt("ShootTime");
    }

    public static void setShootTime(ItemStack stack, int shootTime) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        compoundtag.putInt("ShootTime", shootTime);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundtag));
    }

    public static boolean isShooting(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return compoundtag.getBoolean("Shooting");
    }

    public static void setShooting(ItemStack stack, boolean shooting) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        compoundtag.putBoolean("Shooting", shooting);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundtag));
    }
}
