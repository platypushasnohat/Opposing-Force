package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ScatterBlasterItem extends BlasterItem {

    public ScatterBlasterItem(Properties properties) {
        super(properties, 1280);
    }

    @Override
    protected boolean canShoot(Player player, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND && super.canShoot(player, hand);
    }

    @Override
    public int getCooldown(ItemStack stack, ItemStack ammo) {
        return 40;
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        Vec3 look = player.getLookAngle();
        for (int i = 0; i < 8; i++) {
            LaserBolt laserBolt = new LaserBolt(level, player, player.getX(), player.getY() + player.getBbHeight() * 0.8F, player.getZ());
            laserBolt.setDamage(4.0F);
            laserBolt.shoot(look.x, look.y, look.z, 2.5F, 9.5F);
            level.addFreshEntity(laserBolt);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BLASTER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 0.5F * SinewSoundUtils.randomizePitch(level));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        boolean canShoot = this.canShoot(player, hand);
        InteractionResultHolder<ItemStack> result = super.use(level, player, hand);
        if (canShoot) {
            player.push(player.getLookAngle().reverse().normalize().scale(0.8F));
        }
        return result;
    }
}
