package com.barl_inc.opposing_force.registry;

import com.platypushasnohat.sinew.utils.SinewItemTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class OFItemTiers {

	public static final Tier LASER_BLADE = new SinewItemTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1280, 9.0F, 4.0F, 10, () -> Ingredient.of(OFItems.LASER_FOCUS));
	public static final Tier EMERALD = new SinewItemTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0F, 3.0F, 15, () -> Ingredient.of(Tags.Items.GEMS_EMERALD));

}