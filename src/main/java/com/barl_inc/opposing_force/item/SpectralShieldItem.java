package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.client.sound.SpectralShieldSoundInstance;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.item.MovementModifyingItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

public class SpectralShieldItem extends ShieldItem implements MovementModifyingItem {

    public SpectralShieldItem(Properties properties) {
        super(properties);
    }

    public static void onBlock(Player player, ItemStack stack) {
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int blocks = compoundTag.getInt("Blocks") + 1;
        compoundTag.putInt("Blocks", blocks % 6);
        compoundTag.putLong("Wobble", player.level().getGameTime());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
        if (blocks >= 6) {
            player.disableShield();
            if (!player.isUsingItem()) {
                player.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BREAK, player.getSoundSource(), 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);
            }
        }
    }

    public static float getWobble(LivingEntity entity, ItemStack stack, float partialTicks) {
        long wobble = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getLong("Wobble");
        return wobble == 0L ? 0.0F : Mth.clamp(1.0F - (entity.level().getGameTime() - wobble + partialTicks) / 20.0F, 0.0F, 1.0F);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide && entity.isBlocking()) {
            SpectralShieldSoundInstance.play(entity);
        }
    }

    @Override
    public float getUseMovementMultiplier() {
        return 0.33F;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(OFItems.ECTOPLASM.get());
    }

    @Override
    public boolean canSprintWhileUsing() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(OFTooltips.description("item.opposing_force.spectral_shield.description"));
    }
}
