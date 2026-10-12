package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class OFItemTagsProvider extends ItemTagsProvider {

	public OFItemTagsProvider(PackOutput output, CompletableFuture<Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> lookup, ExistingFileHelper helper) {
		super(output, provider, lookup, OpposingForce.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {

		this.tag(ItemTags.SWORDS).add(
				OFItems.LASER_BLADE.get(),
				OFItems.EMERALD_SWORD.get(),
				OFItems.CHITIN_LANCE.get(),
				OFItems.LAPIS_LAZULI_SWORD.get()
		);

		this.tag(ItemTags.PICKAXES).add(
				OFItems.EMERALD_PICKAXE.get(),
				OFItems.LAPIS_LAZULI_PICKAXE.get()
		);

		this.tag(ItemTags.AXES).add(
				OFItems.EMERALD_AXE.get(),
				OFItems.LAPIS_LAZULI_AXE.get()
		);

		this.tag(ItemTags.SHOVELS).add(
				OFItems.EMERALD_SHOVEL.get(),
				OFItems.LAPIS_LAZULI_SHOVEL.get()
		);

		this.tag(ItemTags.HOES).add(
				OFItems.EMERALD_HOE.get(),
				OFItems.LAPIS_LAZULI_HOE.get()
		);

		this.tag(ItemTags.MEAT).add(
				OFItems.TERROR_LEG.get(),
				OFItems.FRIED_TERROR_LEG.get()
		);

		this.tag(Tags.Items.BUCKETS_ENTITY_WATER).add(
				OFItems.FILLET_BUCKET.get()
		);

		this.tag(Tags.Items.MUSIC_DISCS).add(
				OFItems.MUSIC_DISC_SLAYSER.get()
		);
	}
}