package com.barl_inc.opposing_force.registry.datapack;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class OFEnchantments {

    public static final ResourceKey<Enchantment> MAGIC_PROTECTION = ResourceKey.create(Registries.ENCHANTMENT, OpposingForce.location("magic_protection"));
}
