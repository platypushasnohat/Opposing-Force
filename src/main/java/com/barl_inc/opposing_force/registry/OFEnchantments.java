package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class OFEnchantments {

    public static final ResourceKey<Enchantment> MAGIC_ATTUNEMENT = ResourceKey.create(Registries.ENCHANTMENT, OpposingForce.location("magic_attunement"));
}
