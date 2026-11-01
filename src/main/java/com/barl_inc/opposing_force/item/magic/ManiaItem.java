package com.barl_inc.opposing_force.item.magic;

import com.barl_inc.opposing_force.entity.projectile.ManiaBrain;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.registry.OFEnchantments;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.item.ItemAnimations;
import com.platypushasnohat.sinew.item.MovementModifyingItem;
import com.platypushasnohat.sinew.item.SinewItemUtils;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class ManiaItem extends GunItem implements MovementModifyingItem {

    public static final String RELOAD_ANIMATION = "reload";

    public ManiaItem(Properties properties) {
        super(properties);
    }

    public static int getAttunement(ItemStack stack) {
        return stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet().stream().filter(entry -> entry.getKey().is(OFEnchantments.MAGIC_ATTUNEMENT)).mapToInt(Object2IntMap.Entry::getIntValue).sum();
    }

    public int getBrainCount(ItemStack stack) {
        return 6 + getAttunement(stack) * 2;
    }

    public boolean isElite(ItemStack stack) {
        return getAttunement(stack) >= 3;
    }

    @Override
    public boolean isAmmo(ItemStack stack) {
        return stack.is(OFItems.ECTOPLASM.get());
    }

    @Override
    public ItemStack getAmmo(Player player) {
        ItemStack ammo = super.getAmmo(player);
        return ammo.isEmpty() && player.hasInfiniteMaterials() ? new ItemStack(OFItems.ECTOPLASM.get()) : ammo;
    }

    public int countAmmo(Player player) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (this.isAmmo(stack)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    @Override
    public boolean hasAmmo(Player player) {
        return player.hasInfiniteMaterials() || this.countAmmo(player) >= 1 + getAttunement(player.getMainHandItem());
    }

    @Override
    public void consumeAmmo(Player player, ItemStack stack, ItemStack ammo) {
        int remaining = 0;
        int cost = 1 + player.getRandom().nextInt(2) + getAttunement(stack);
        for (int i = 0; i < cost; i++) {
            if (SinewItemUtils.shouldConsumeMana(player, stack)) {
                remaining++;
            }
        }
        int taken = Math.min(remaining, ammo.getCount());
        ammo.shrink(taken);
        remaining -= taken;
        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (this.isAmmo(slot)) {
                taken = Math.min(remaining, slot.getCount());
                slot.shrink(taken);
                remaining -= taken;
            }
        }
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        if (!level.isClientSide) {
            for (ManiaBrain brain : getChargingBrains(level, player)) {
                brain.release(player);
            }
            setReloadPending(stack, true);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.GUN_SHOOT, SoundSource.PLAYERS, 1.5F, 1.3F * SinewSoundUtils.randomizePitch(player));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.6F * SinewSoundUtils.randomizePitch(player));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.consume(stack);
        if (!this.canShoot(player, hand)) {
            result = super.use(level, player, hand);
        } else if (!isReloadPending(stack)) {
            player.startUsingItem(hand);
        } else {
            ItemAnimations.start(player, stack, RELOAD_ANIMATION);
            if (!level.isClientSide) {
                setReloadPending(stack, false);
                player.getCooldowns().addCooldown(this, 20);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS, 1.0F, 0.7F * SinewSoundUtils.randomizePitch(player));
            }
        }
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide && entity instanceof Player player) {
            int count = this.getBrainCount(stack);
            int charge = this.getUseDuration(stack, entity) - remainingUseDuration;
            int wanted = Math.min(count, 1 + charge * count / 40);
            if (charge == 0) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.PLAYERS, 0.8F, 0.8F); //this does not play correctly and i dont want to fucking fix it rn
            }
            int maniaAmplifier = 0;
            if (this.isElite(stack)) {
                maniaAmplifier = 1;
            }
            for (int i = getChargingBrains(level, player).size(); i < wanted; i++) {
                level.addFreshEntity(new ManiaBrain(level, player, stack, i, count, this.isElite(stack), maniaAmplifier));
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 0.4F, 1.6F + level.getRandom().nextFloat() * 0.3F);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            ItemAnimations.start(player, stack, SHOOT_ANIMATION);
            ItemStack ammo = this.getAmmo(player);
            this.shoot(level, player, stack, ammo);
            this.onShot(level, player, player.getUsedItemHand(), stack, ammo);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public float getUseMovementMultiplier() {
        return 0.6F;
    }

    @Override
    public boolean canSprintWhileUsing() {
        return false;
    }

    public static List<ManiaBrain> getChargingBrains(Level level, Player player) {
        return level.getEntitiesOfClass(ManiaBrain.class, player.getBoundingBox().inflate(8.0D), brain -> brain.getOwner() == player && brain.isCharging());
    }

    @Override
    public int getCooldown(ItemStack stack, ItemStack ammo) {
        return 10;
    }

    @Override
    public float getDamage() {
        return ManiaBrain.DAMAGE;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(OFTooltips.description("item.opposing_force.mania.description"));
        tooltipComponents.add(OFTooltips.description(this.getAmmoTooltip()));
        OFTooltips.addStats(tooltipComponents, "item.opposing_force.tooltip.when_fired", OFTooltips.magicDamage(this.getDamage(), this.getBrainCount(stack)));
    }

    @Override
    public String getAmmoTooltip() {
        return "item.opposing_force.tooltip.uses_ectoplasm";
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    public static boolean isReloadPending(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("ReloadPending");
    }

    public static void setReloadPending(ItemStack stack, boolean pending) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        compoundtag.putBoolean("ReloadPending", pending);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundtag));
    }
}
