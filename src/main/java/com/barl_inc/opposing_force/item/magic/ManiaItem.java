package com.barl_inc.opposing_force.item.magic;

import com.barl_inc.opposing_force.entity.projectile.ManiaBrain;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.registry.OFEnchantments;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.item.ItemAnimations;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ManiaItem extends GunItem {

    public static final String RELOAD_ANIMATION = "reload";


    public ManiaItem(Properties properties) {
        super(properties);
    }

    public static int getAttunement(ItemStack stack) {
        return stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet().stream().filter(entry -> entry.getKey().is(OFEnchantments.MAGIC_ATTUNEMENT)).mapToInt(Object2IntMap.Entry::getIntValue).sum();
    }

    public List<ManiaBrain.Shade> getShades(ItemStack stack) {
        List<ManiaBrain.Shade> shades = new ArrayList<>(List.of(ManiaBrain.Shade.BLACK, ManiaBrain.Shade.CYAN, ManiaBrain.Shade.MAGENTA, ManiaBrain.Shade.YELLOW));
        List<ManiaBrain.Shade> attuned = List.of(ManiaBrain.Shade.PURPLE, ManiaBrain.Shade.GREEN, ManiaBrain.Shade.BLUE);
        shades.addAll(attuned.subList(0, Math.min(getAttunement(stack), attuned.size())));
        return shades;
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

    @Override
    public void consumeAmmo(Player player, ItemStack stack, ItemStack ammo) {
        if (SinewItemUtils.shouldConsumeAmmo(player, stack)) {
            ammo.shrink(1);
        }
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        if (!level.isClientSide) {
            List<ManiaBrain.Shade> shades = this.getShades(stack);
            for (ManiaBrain.Shade shade : shades) {
                level.addFreshEntity(new ManiaBrain(level, player, stack, shade, shades.size()));
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
        if (!isReloadPending(stack) || !this.canShoot(player, hand)) {
            result = super.use(level, player, hand);
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
    public int getCooldown(ItemStack stack, ItemStack ammo) {
        return 10;
    }

    @Override
    public float getDamage() {
        return ManiaBrain.DAMAGE;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(OFTooltips.description(this.getAmmoTooltip()));
        OFTooltips.addStats(tooltipComponents, "item.opposing_force.tooltip.when_fired", OFTooltips.damage(this.getDamage(), this.getShades(stack).size()));
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
