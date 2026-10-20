package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.item.base.BulletGunItem;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class GorloffItem extends BulletGunItem {

    private static final int PROJECTILE_DAMAGE_DIVISOR = 2;

    public GorloffItem(Properties properties) {
        super(properties.durability(650), 1.0F, 3.0F, 7.0F, 100);
    }

    @Override
    public int getProjectileCount() {
        return PROJECTILE_DAMAGE_DIVISOR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), OFSoundEvents.GUN_WIND_DOWN, SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(entity));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.canShoot(player, hand)) {
            player.startUsingItem(hand);
        } else if (!level.isClientSide) {
            player.getCooldowns().addCooldown(this, 10);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof Player player) {
            int shots = getShots(stack);
            if (!this.hasAmmo(player)) {
                player.releaseUsingItem();
            } else if (!level.isClientSide && shots < (this.getUseDuration(stack, player) - remainingUseDuration) * 2 / 5 + 1) {
                ItemStack ammo = this.getAmmo(player);
                if (entity.tickCount % 3 == 0) {
                    this.shoot(level, player, stack, ammo);
                    setShots(stack, shots + 1);
                    if ((shots + 1) % 4 == 0) {
                        this.useBullet(player, stack, ammo);
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (entity instanceof Player player) {
            if (level.isClientSide) {
                this.shootAnimationState.animateWhen(player.isUsingItem() && player.getUseItem().is(this), player.tickCount, player);
            } else if (getShots(stack) > 0 && !(player.isUsingItem() && player.getUseItem() == stack)) {
                if (getShots(stack) % 4 != 0) {
                    this.useBullet(player, stack, this.getAmmo(player));
                }
                setShots(stack, 0);
                player.getCooldowns().addCooldown(this, this.getCooldown(stack, ItemStack.EMPTY));
            }
        }
    }

    private void useBullet(Player player, ItemStack stack, ItemStack ammo) {
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.hasInfiniteMaterials()) {
            this.consumeAmmo(player, stack, ammo);
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        super.shoot(level, player, stack, ammo);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.GUN_SHOOT_SHORT, SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(player));
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
