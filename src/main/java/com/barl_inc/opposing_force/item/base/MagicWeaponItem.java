package com.barl_inc.opposing_force.item.base;

import com.barl_inc.opposing_force.tags.OFItemTags;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import com.platypushasnohat.sinew.registry.SinewDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class MagicWeaponItem extends Item {

    private static final Map<TagKey<Item>, TagKey<Item>> MANA_TYPES = Map.of(
            OFItemTags.CONSUMES_BASIC_MANA, OFItemTags.BASIC_MANA
    );

    public MagicWeaponItem(Properties properties) {
        super(properties);
    }

    public int getManaCost(ItemStack weapon) {
        return 1;
    }

    public boolean isMana(ItemStack weapon, ItemStack stack) {
        return !stack.isEmpty() && MANA_TYPES.entrySet().stream().anyMatch(entry -> weapon.is(entry.getKey()) && stack.is(entry.getValue()));
    }

    public int countMana(Player player, ItemStack weapon) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (this.isMana(weapon, stack)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public boolean hasMana(Player player, ItemStack weapon) {
        return player.hasInfiniteMaterials() || this.countMana(player, weapon) >= this.getManaCost(weapon);
    }

    public void consumeMana(Player player, ItemStack weapon) {
        if (!player.hasInfiniteMaterials()) {
            int remaining = this.consumeFrom(player.getOffhandItem(), weapon, this.getManaCost(weapon));
            for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
                remaining = this.consumeFrom(player.getInventory().getItem(i), weapon, remaining);
            }
        }
    }

    private int consumeFrom(ItemStack stack, ItemStack weapon, int remaining) {
        int taken = this.isMana(weapon, stack) ? Math.min(remaining, stack.getCount()) : 0;
        stack.shrink(taken);
        return remaining - taken;
    }

    public static DamageSource causeMagicDamage(Level level, Entity direct, @Nullable Entity owner) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SinewDamageTypes.MAGIC_DAMAGE), direct, owner);
    }

    public static float getMagicDamage(@Nullable Entity owner, float damage) {
        return owner instanceof LivingEntity living && living.getAttribute(SinewAttributes.MAGIC_DAMAGE) != null ? damage + (float) living.getAttributeValue(SinewAttributes.MAGIC_DAMAGE) : damage;
    }
}
