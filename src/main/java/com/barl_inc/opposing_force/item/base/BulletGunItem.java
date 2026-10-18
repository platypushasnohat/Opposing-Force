package com.barl_inc.opposing_force.item.base;

import com.barl_inc.opposing_force.entity.projectile.Bullet;
import com.barl_inc.opposing_force.registry.OFDataMaps;
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
        return stack.getItemHolder().getData(OFDataMaps.BULLET_STATS) != null;
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

    public BulletStats getStats(ItemStack ammo) {
        BulletStats stats = ammo.getItemHolder().getData(OFDataMaps.BULLET_STATS);
        return stats == null ? new BulletStats(1.0F, 1.0F, 1.0F, 1.0F) : stats;
    }

    public float getBulletDamage(ItemStack ammo) {
        return this.damage * this.getStats(ammo).damage();
    }

    public float getInaccuracy(ItemStack ammo) {
        return this.inaccuracy * this.getStats(ammo).inaccuracy();
    }

    public float getVelocity(ItemStack ammo) {
        return this.velocity * this.getStats(ammo).velocity();
    }

    @Override
    protected int getCooldown(ItemStack stack, ItemStack ammo) {
        return Math.round(this.cooldown * this.getStats(ammo).cooldown());
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