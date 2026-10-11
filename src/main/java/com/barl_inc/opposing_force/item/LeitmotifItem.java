package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.Mudball;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.client.animation.ItemAnimationState;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeitmotifItem extends Item {
    private static final int[] SHOT_TICKS = {5, 14, 23, 33};

    public final ItemAnimationState blastAnimationState = new ItemAnimationState();

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
        if (!isShooting(stack)) {
            if (level.isClientSide) {
                this.blastAnimationState.start(player.tickCount, player);
            }
            else {
                setShooting(stack, true);
                setShootTime(stack, 0);
                player.getCooldowns().addCooldown(stack.getItem(), 80);
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    ammoStack.shrink(1);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide && isShooting(stack)) {
            if (isSelected && entity instanceof Player player) {
                int shootTime = getShootTime(stack);
                for (int shotTick : SHOT_TICKS) {
                    if (shootTime == shotTick && !stack.isEmpty()) {
                        this.shootMudball(level, player);
                        if (!player.getAbilities().instabuild) {
                            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                        }
                    }
                }
                if (shootTime >= 34 || stack.isEmpty()) {
                    setShooting(stack, false);
                }
                else {
                    setShootTime(stack, shootTime + 1);
                }
            }
            else {
                setShooting(stack, false);
            }
        }
    }

    private void shootMudball(Level level, Player player) {
        Mudball mudball = new Mudball(level, player, player.getX(), player.getEyeY() - 0.2D, player.getZ());
        Vec3 look = player.getLookAngle();
        mudball.setDamage(3.5F);
        mudball.setSize(1.2F);
        mudball.shoot(look.x, look.y, look.z, 1.4F, 1.0F);
        level.addFreshEntity(mudball);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS, 1.0F, 0.6F * SinewSoundUtils.randomizePitch(level));
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
