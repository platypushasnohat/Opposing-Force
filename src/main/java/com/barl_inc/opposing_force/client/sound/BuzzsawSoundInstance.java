package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.item.BuzzsawItem;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.sound.ItemTickableSound;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BuzzsawSoundInstance extends ItemTickableSound {

    public BuzzsawSoundInstance(LivingEntity user) {
        super(user, OFSoundEvents.BUZZSAW_LOOP.get());
    }

    @Override
    public void tickVolume(ItemStack itemStack) {
        float useAmount = BuzzsawItem.getLerpedUseTime(itemStack, 1.0F) / 5.0F;
        this.volume = useAmount;
        this.pitch = 0.2F + 0.8F * useAmount;
    }

    @Override
    public boolean isValidItem(ItemStack itemStack) {
        return itemStack.is(OFItems.BUZZSAW.get());
    }
}