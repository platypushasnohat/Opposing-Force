package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.utils.ClientItemData;
import com.platypushasnohat.sinew.events.custom.ScreenShakeEvent;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScatterBlasterItem extends BlasterItem {

    private static final int PROJECTILE_COUNT = 8;

    public ScatterBlasterItem(Properties properties) {
        super(properties, 1280);
    }

    @Override
    protected boolean canShoot(Player player, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND && super.canShoot(player, hand);
    }

    @Override
    public int getCooldown(ItemStack stack, ItemStack ammo) {
        return 45;
    }

    @Override
    public float getDamage() {
        return 3.5F;
    }

    @Override
    public int getShotCount() {
        return PROJECTILE_COUNT;
    }

    @Override
    protected boolean canDualWield() {
        return false;
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        if (level.isClientSide) {
            ClientItemData.getOrCreateClientData(stack).put(GUN_FLASH, true);
            Sinew.PROXY.screenShake(new ScreenShakeEvent(player.position(), 10, 2.0F, 5, false));
        }
        for (int i = 0; i < PROJECTILE_COUNT; i++) {
            LaserBolt laserBolt = new LaserBolt(player, level, stack);
            laserBolt.setDamage(this.getDamage());
            laserBolt.setProjectileCount(PROJECTILE_COUNT);
            laserBolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5F, 10.0F);
            level.addFreshEntity(laserBolt);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BLASTER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 0.5F * SinewSoundUtils.randomizePitch(level));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        boolean canShoot = this.canShoot(player, hand);
        InteractionResultHolder<ItemStack> result = super.use(level, player, hand);
        if (canShoot) {
            player.push(player.getLookAngle().reverse().normalize().scale(0.4F));
        }
        return result;
    }
}
