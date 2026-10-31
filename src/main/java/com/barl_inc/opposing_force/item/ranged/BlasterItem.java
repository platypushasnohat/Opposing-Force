package com.barl_inc.opposing_force.item.ranged;

import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.barl_inc.opposing_force.item.base.PoweredItem;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.utils.ClientItemData;
import com.platypushasnohat.sinew.events.custom.ScreenShakeEvent;
import com.platypushasnohat.sinew.item.SinewItemUtils;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class BlasterItem extends GunItem {

    public BlasterItem(Properties properties) {
        this(properties, 1024);
    }

    protected BlasterItem(Properties properties, int durability) {
        super(properties.stacksTo(1).durability(durability));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isAmmo(ItemStack stack) {
        return stack.is(OFItems.POWER_CELL) && stack.getItem() instanceof PoweredItem poweredItem && poweredItem.getPower(stack) < poweredItem.getMaxPower(stack);
    }

    @Override
    public void consumeAmmo(Player player, ItemStack stack, ItemStack ammo) {
        if (ammo.getItem() instanceof PoweredItem poweredItem && SinewItemUtils.shouldConsumeAmmo(player, stack)) {
            poweredItem.drainPower(ammo, 1, null);
        }
    }

    @Override
    protected boolean canShoot(Player player, InteractionHand hand) {
        return this.hasAmmo(player);
    }

    @Override
    public int getCooldown(ItemStack stack, ItemStack ammo) {
        return 12;
    }

    @Override
    public float getDamage() {
        return 5.5F;
    }

    @Override
    public String getAmmoTooltip() {
        return "item.opposing_force.tooltip.requires_power_cell";
    }

    @Override
    protected boolean canDualWield() {
        return true;
    }

    @Override
    public Predicate<ItemStack> swapPredicate() {
        return itemStack -> itemStack.is(OFItems.BLASTER.get());
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        if (level.isClientSide) {
            ClientItemData.getOrCreateClientData(stack).put(GUN_FLASH, true);
            Sinew.PROXY.screenShake(new ScreenShakeEvent(player.position(), 5, 1.0F, 4, false));
        }
        LaserBolt laserBolt = new LaserBolt(player, level, stack);
        laserBolt.setDamage(this.getDamage());
        Vec3 look = player.getLookAngle();
        laserBolt.shoot(look.x, look.y, look.z, 3.0F, 1.0F);
        level.addFreshEntity(laserBolt);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), OFSoundEvents.BLASTER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(level));
    }
}
