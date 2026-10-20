package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.item.base.BulletGunItem;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrackshotItem extends BulletGunItem {

    public CrackshotItem(Properties properties) {
        super(properties.durability(400), 4.0F, 3.0F, 2.0F, 12);
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        super.shoot(level, player, stack, ammo);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.GUN_SHOOT, SoundSource.PLAYERS, 1.5F, SinewSoundUtils.randomizePitch(player));
    }
}
