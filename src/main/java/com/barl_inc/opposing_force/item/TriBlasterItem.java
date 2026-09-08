package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TriBlasterItem extends BlasterItem {

    public TriBlasterItem(Properties properties) {
        super(properties.stacksTo(1).durability(1280));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !(oldStack.is(OFItems.TRI_BLASTER)) || !(newStack.is(OFItems.TRI_BLASTER));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
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
        if (getShootTime(stack) == 0 && !isShooting(stack)) {
            setShooting(stack, true);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        boolean shooting = isShooting(stack);
        int shootTime = getShootTime(stack);
        if (isSelected && entity instanceof Player player) {
            ItemStack ammoStack = this.getAmmo(player);
            if (shootTime == 1 && shooting) {
                this.shootLaser(level, player);
                if (!level.isClientSide) {
                    player.getCooldowns().addCooldown(stack.getItem(), 16);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (!player.getAbilities().instabuild) {
                        ammoStack.shrink(1);
                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                    }
                }
            }
            if (shootTime == 4 && shooting) {
                this.shootLaser(level, player);
            }
            if (shootTime == 7 && shooting) {
                this.shootLaser(level, player);
                setShooting(stack, false);
            }
            if (shooting && shootTime <= 7) {
                setShootTime(stack, shootTime + 1);
            }
            if (!shooting && shootTime > 0) {
                setShootTime(stack, 0);
            }
        } else {
            setShooting(stack, false);
        }
    }

    private void shootLaser(Level level, Player player) {
        if (level.isClientSide) {
            this.blastAnimationState.start(player.tickCount, player);
        } else {
            LaserBolt laserBolt = new LaserBolt(level, player, player.getX(), player.getY() + player.getBbHeight() * 0.8F, player.getZ());
            laserBolt.setDamage(5.0F);
            Vec3 look = player.getLookAngle();
            laserBolt.shoot(look.x, look.y, look.z, 1.8F, 0.5F);
            level.addFreshEntity(laserBolt);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BLASTER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(level));
        }
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
