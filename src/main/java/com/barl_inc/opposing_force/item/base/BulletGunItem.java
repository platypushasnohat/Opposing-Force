package com.barl_inc.opposing_force.item.base;

import com.barl_inc.opposing_force.entity.projectile.Bullet;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BulletGunItem extends GunItem {

    private final float damage;
    private final float velocity;
    private final float inaccuracy;
    private final int cooldown;

    public BulletGunItem(Properties properties, float damage, float velocity, float inaccuracy, int cooldown) {
        super(properties.stacksTo(1));
        this.damage = damage;
        this.velocity = velocity;
        this.inaccuracy = inaccuracy;
        this.cooldown = cooldown;
    }

    @Override
    public boolean isAmmo(ItemStack stack) {
        return stack.getItem() instanceof BulletItem;
    }

    @Override
    public ItemStack getAmmo(Player player) {
        ItemStack ammo = super.getAmmo(player);
        return ammo.isEmpty() && player.hasInfiniteMaterials() ? new ItemStack(OFItems.IRON_BULLET.get()) : ammo;
    }

    @Override
    public void consumeAmmo(Player player, ItemStack ammo) {
        ammo.shrink(1);
    }

    public BulletTier getTier(ItemStack ammo) {
        return ammo.getItem() instanceof BulletItem bulletItem ? bulletItem.getTier() : BulletTier.IRON;
    }

    public float getBulletDamage(ItemStack ammo) {
        return this.damage * this.getTier(ammo).getDamageMultiplier();
    }

    public float getInaccuracy(ItemStack ammo) {
        return this.inaccuracy * this.getTier(ammo).getInaccuracyMultiplier();
    }

    public float getVelocity(ItemStack ammo) {
        return this.velocity * this.getTier(ammo).getVelocityMultiplier();
    }

    @Override
    protected int getCooldown(ItemStack stack, ItemStack ammo) {
        return Math.round(this.cooldown * this.getTier(ammo).getCooldownMultiplier());
    }

    protected int getBulletLifespan() {
        return 40;
    }

    @Override
    protected void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        this.shootBullet(level, player, ammo, 0.0F);
    }

    protected void shootBullet(Level level, Player player, ItemStack ammo, float extraInaccuracy) {
        Bullet bullet = new Bullet(level, player, ammo.copyWithCount(1));
        bullet.setDamage(this.getBulletDamage(ammo));
        bullet.setLifespan(this.getBulletLifespan());
        Vec3 look = player.getLookAngle();
        bullet.shoot(look.x, look.y, look.z, this.getVelocity(ammo), this.getInaccuracy(ammo) + extraInaccuracy);
        level.addFreshEntity(bullet);
    }
}