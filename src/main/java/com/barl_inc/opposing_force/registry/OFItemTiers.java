package com.barl_inc.opposing_force.registry;

import com.platypushasnohat.sinew.utils.SinewItemTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class OFItemTiers {

	public static final Tier LASER_BLADE = new SinewItemTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1280, 9.0F, 3.5F, 16, () -> Ingredient.of(OFItems.LASER_FOCUS));
	public static final Tier EMERALD = new SinewItemTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0F, 3.0F, 15, () -> Ingredient.of(Tags.Items.GEMS_EMERALD));
	public static final Tier CHITIN = new SinewItemTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 300, 6.0F, 2.0F, 14, () -> Ingredient.of(OFItems.CHITIN));
	public static final Tier LAPIS_LAZULI = new SinewItemTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 200, 7.0F, 0.0F, 25, () -> Ingredient.of(Tags.Items.GEMS_LAPIS));
	public static final Tier BUZZSAW = new SinewItemTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 560, 12.0F, 2.0F, 12, () -> Ingredient.of(Tags.Items.INGOTS_IRON));

}