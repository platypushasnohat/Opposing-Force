package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.tags.OFBiomeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
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
	}
}