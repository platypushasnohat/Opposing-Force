package com.barl_inc.opposing_force.registry;

import com.platypushasnohat.sinew.utils.SinewItemTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class OFItemTiers {

	public static final Tier LASER_BLADE = new SinewItemTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1280, 9.0F, 4.0F, 10, () -> Ingredient.of(OFItems.LASER_FOCUS));
}