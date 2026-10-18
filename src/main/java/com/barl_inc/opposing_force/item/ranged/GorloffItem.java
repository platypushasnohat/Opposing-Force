package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.entity.projectile.Bullet;
import com.barl_inc.opposing_force.item.base.BulletGunItem;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.phys.Vec3;

public class GorloffItem extends BulletGunItem {

    public GorloffItem(Properties properties) {
        super(properties.durability(1024), 1.0F, 4.0F, 20.0F, 120);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
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
                this.shoot(level, player, stack, ammo);
                setShots(stack, shots + 1);
                if ((shots + 1) % 4 == 0) {
                    this.useBullet(player, stack, ammo);
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
            this.consumeAmmo(player, ammo);
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
    }

    @Override
    protected void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        Bullet bullet = new Bullet(level, player, ammo.copyWithCount(1));
        bullet.setDamage(this.getBulletDamage(ammo));
        bullet.setLifespan(this.getBulletLifespan());
        bullet.setProjectileCount(4);
        Vec3 look = player.getLookAngle();
        bullet.shoot(look.x, look.y, look.z, this.getVelocity(ammo), this.getInaccuracy(ammo));
        level.addFreshEntity(bullet);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.6F, 1.8F * SinewSoundUtils.randomizePitch(level));
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
