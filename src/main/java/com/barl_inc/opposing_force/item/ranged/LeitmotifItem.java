package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.entity.projectile.Mudball;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeitmotifItem extends GunItem {

    public LeitmotifItem(Properties properties) {
        super(properties.stacksTo(1).durability(512));
    }

    @Override
    public boolean isAmmo(ItemStack stack) {
        return stack.is(Items.MUD);
    }

    @Override
    public void consumeAmmo(Player player, ItemStack ammo) {
        ammo.shrink(1);
    }

    @Override
    protected int getCooldown(ItemStack stack, ItemStack ammo) {
        return 10;
    }

    @Override
    protected void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        Mudball mudball = new Mudball(level, player, player.getX(), player.getY() + player.getBbHeight() * 0.8F, player.getZ());
        Vec3 look = player.getLookAngle();
        mudball.setDamage(3.5F);
        mudball.shoot(look.x, look.y, look.z, 1.5F, 2.0F);
        level.addFreshEntity(mudball);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS, 1.0F, 0.6F * SinewSoundUtils.randomizePitch(level));
    }
}
