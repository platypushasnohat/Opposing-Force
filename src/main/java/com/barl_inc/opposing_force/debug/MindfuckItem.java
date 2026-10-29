package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.entity.projectile.ManiaBrain;
import com.barl_inc.opposing_force.item.magic.ManiaItem;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class MindfuckItem extends ManiaItem {

    public MindfuckItem(Properties properties) {
        super(properties);
    }

    @Override
    public List<ManiaBrain.Shade> getShades(ItemStack stack) {
        List<ManiaBrain.Shade> shades = new ArrayList<>();
        for (ManiaBrain.Shade shade : ManiaBrain.Shade.values()) {
            shades.add(shade);
            shades.add(shade);
        }
        return shades;
    }

    @Override
    public boolean hasAmmo(Player player) {
        return true;
    }

    @Override
    public ItemStack getAmmo(Player player) {
        return new ItemStack(OFItems.ECTOPLASM.get());
    }

    @Override
    public void consumeAmmo(Player player, ItemStack stack, ItemStack ammo) {
    }

    @Override
    public String getAmmoTooltip() {
        return "item.opposing_force.tooltip.no_ammo";
    }

    @Override
    public boolean isElite(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }
}
