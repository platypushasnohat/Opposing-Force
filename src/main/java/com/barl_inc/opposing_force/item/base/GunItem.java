package com.barl_inc.opposing_force.item.base;

import com.platypushasnohat.sinew.client.animation.ItemAnimationState;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class GunItem extends ProjectileWeaponItem {

    public final ItemAnimationState shootAnimationState = new ItemAnimationState();

    public GunItem(Properties properties) {
        super(properties);
    }

    public abstract boolean isAmmo(ItemStack stack);

    public abstract void consumeAmmo(Player player, ItemStack stack, ItemStack ammo);

    public abstract void shoot(Level level, Player player, ItemStack stack, ItemStack ammo);

    public abstract int getCooldown(ItemStack stack, ItemStack ammo);

    public ItemStack getAmmo(Player player) {
        ItemStack ammo = this.isAmmo(player.getOffhandItem()) ? player.getOffhandItem() : ItemStack.EMPTY;
        for (int i = 0; i < player.getInventory().getContainerSize() && ammo.isEmpty(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (this.isAmmo(stack)) {
                ammo = stack;
            }
        }
        return ammo;
    }

    public boolean hasAmmo(Player player) {
        return player.hasInfiniteMaterials() || !this.getAmmo(player).isEmpty();
    }

    protected boolean canShoot(Player player, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND && this.hasAmmo(player);
    }

    public int getShotCooldown(Player player, InteractionHand hand, ItemStack stack, ItemStack ammo) {
        float attackSpeed = hand == InteractionHand.MAIN_HAND ? (float) player.getAttributeValue(Attributes.ATTACK_SPEED) : 4.0F;
        return Math.max(1, Math.round(this.getCooldown(stack, ammo) * 4.0F / attackSpeed));
    }

    protected void onShot(Player player, InteractionHand hand, ItemStack stack, ItemStack ammo) {
        player.getCooldowns().addCooldown(this, this.getShotCooldown(player, hand, stack, ammo));
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.hasInfiniteMaterials()) {
            this.consumeAmmo(player, stack, ammo);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.pass(stack);
        if (!this.canShoot(player, hand)) {
            if (!level.isClientSide) {
                player.getCooldowns().addCooldown(this, 10);
            }
            return InteractionResultHolder.consume(stack);
        }
        if (level.isClientSide) {
            this.shootAnimationState.start(player.tickCount, player);
        } else {
            ItemStack ammo = this.getAmmo(player);
            this.shoot(level, player, stack, ammo);
            this.onShot(player, hand, stack, ammo);
        }
        return result;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return this::isAmmo;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.is(this) || !newStack.is(this);
    }
}
