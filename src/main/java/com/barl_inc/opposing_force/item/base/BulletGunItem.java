package com.barl_inc.opposing_force.item.base;

import com.barl_inc.opposing_force.entity.projectile.Bullet;
import com.barl_inc.opposing_force.item.ranged.ammo.BulletComponent;
import com.barl_inc.opposing_force.registry.OFDataComponents;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.item.SinewItemUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BulletGunItem extends GunItem {

    public final float damage;
    public final float velocity;
    public final float inaccuracy;
    public final int cooldown;

    public BulletGunItem(Properties properties, float damage, float velocity, float inaccuracy, int cooldown) {
        super(properties.stacksTo(1));
        this.damage = damage;
        this.velocity = velocity;
        this.inaccuracy = inaccuracy;
        this.cooldown = cooldown;
    }

    @Override
    public boolean isAmmo(ItemStack stack) {
        return stack.get(OFDataComponents.BULLET) != null;
    }

    @Override
    public ItemStack getAmmo(Player player) {
        ItemStack ammo = super.getAmmo(player);
        return ammo.isEmpty() && player.hasInfiniteMaterials() ? new ItemStack(OFItems.IRON_BULLET.get()) : ammo;
    }

    @Override
    public void consumeAmmo(Player player, ItemStack stack, ItemStack ammo) {
        if (SinewItemUtils.shouldConsumeAmmo(player, stack)) {
            ammo.shrink(1);
        }
    }

    @Override
    public void shoot(Level level, Player player, ItemStack stack, ItemStack ammo) {
        this.shootBullet(level, player, stack, ammo);
    }

    @Override
    public float getDamage() {
        return this.damage;
    }

    @Override
    public String getAmmoTooltip() {
        return "item.opposing_force.tooltip.uses_bullets";
    }

    public int getProjectileCount() {
        return 1;
    }

    @Override
    public int getCooldown(ItemStack stack, ItemStack ammo) {
        return this.cooldown;
    }

    public void shootBullet(Level level, Player player, ItemStack stack, ItemStack ammo) {
        Bullet bullet = new Bullet(player, level, stack);
        BulletComponent bulletComponent = ammo.get(OFDataComponents.BULLET);
        float damage = this.damage;
        float velocity = this.velocity;
        float inaccuracy = this.inaccuracy;
        ItemStack renderItem = OFItems.SHOT_IRON_BULLET.toStack();
        if (bulletComponent != null) {
            renderItem = bulletComponent.renderItem();
            damage = this.damage * bulletComponent.damageMultiplier();
            velocity = this.velocity * bulletComponent.velocityMultiplier();
            inaccuracy = this.inaccuracy * bulletComponent.inaccuracyMultiplier();
        }
        bullet.setDamage(damage);
        bullet.setItem(renderItem);
        bullet.setProjectileCount(this.getProjectileCount());
        bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);
        level.addFreshEntity(bullet);
    }
}