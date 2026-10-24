package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.client.sound.SpectralShieldSoundInstance;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.item.MovementModifyingItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class SpectralShieldItem extends ShieldItem implements MovementModifyingItem {

    public SpectralShieldItem(Properties properties) {
        super(properties);
    }

    public static void onBlock(Player player, ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int blocks = tag.getInt("Blocks") + 1;
        tag.putInt("Blocks", blocks % 6);
        tag.putLong("Wobble", player.level().getGameTime());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        if (blocks >= 6) {
            player.disableShield();
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
}
