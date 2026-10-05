package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.tags.OFBiomeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class OFBiomeTagsProvider extends BiomeTagsProvider {

	public OFBiomeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, OpposingForce.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(OFBiomeTags.HAS_OVERWORLD_MONSTERS).addTag(BiomeTags.IS_OVERWORLD).remove(Tags.Biomes.NO_DEFAULT_MONSTERS);
		this.tag(OFBiomeTags.HAS_SAVANNA_MONSTERS).addTag(BiomeTags.IS_SAVANNA);
		this.tag(OFBiomeTags.HAS_PLAINS_MONSTERS).addTag(Tags.Biomes.IS_PLAINS);
		this.tag(OFBiomeTags.HAS_FOREST_MONSTERS).add(
				Biomes.FOREST
		);
		this.tag(OFBiomeTags.HAS_DESERT_MONSTERS).addTag(Tags.Biomes.IS_DESERT);
	}
}