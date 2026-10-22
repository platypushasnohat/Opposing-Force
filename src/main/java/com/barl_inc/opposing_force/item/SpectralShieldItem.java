package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.client.sound.SpectralShieldSoundInstance;
import com.platypushasnohat.sinew.item.MovementModifyingItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;

public class SpectralShieldItem extends ShieldItem implements MovementModifyingItem {

    public SpectralShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide && entity.isBlocking()) {
            SpectralShieldSoundInstance.play(entity);
        }
    }
    @Override
    public float getUseMovementMultiplier() {
        return 0.40F;
    }

    @Override
    public boolean canSprintWhileUsing() {
        return false;
    }
}
