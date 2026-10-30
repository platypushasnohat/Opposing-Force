package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.debug.OFDebugItems;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.datapack.tags.OFItemTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosTags;

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
                OFItems.LAPIS_LAZULI_SWORD.get(),
                OFItems.SPECTRAL_CLEAVER.get(),
                OFItems.TUNING_FORK.get()
        );

        this.tag(ItemTags.PICKAXES).add(
                OFItems.EMERALD_PICKAXE.get(),
                OFItems.LAPIS_LAZULI_PICKAXE.get()
        );

        this.tag(ItemTags.AXES).add(
                OFItems.EMERALD_AXE.get(),
                OFItems.LAPIS_LAZULI_AXE.get(),
                OFItems.BUZZSAW.get()
        );

        this.tag(ItemTags.SHOVELS).add(
                OFItems.EMERALD_SHOVEL.get(),
                OFItems.LAPIS_LAZULI_SHOVEL.get()
        );

        this.tag(ItemTags.HOES).add(
                OFItems.EMERALD_HOE.get(),
                OFItems.LAPIS_LAZULI_HOE.get()
        );

        this.tag(ItemTags.SWORD_ENCHANTABLE).add(
                OFItems.BUZZSAW.get(),
                OFDebugItems.SOULCLEAVE.get()
        );

        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
                OFItems.BLASTER.get(),
                OFItems.TRI_BLASTER.get(),
                OFItems.SCATTER_BLASTER.get(),
                OFItems.LEITMOTIF.get(),
                OFItems.CRACKSHOT.get(),
                OFItems.JAEGER.get(),
                OFItems.BOOMSTICK.get(),
                OFItems.GORLOFF.get(),
                OFItems.SPECTRAL_SHIELD.get(),
                OFItems.CURSED_RINGLINE.get(),
                OFItems.MANIA.get(),
                OFItems.GNAT_STAFF.get(),
                OFItems.SHROOM_STAFF.get()
        );

        this.tag(ItemTags.HEAD_ARMOR).add(
                OFItems.EMERALD_MASK.get()
        );

        this.tag(ItemTags.CHEST_ARMOR).add(
                OFItems.EMERALD_CHESTPLATE.get()
        );

        this.tag(ItemTags.LEG_ARMOR).add(
                OFItems.EMERALD_LEGGINGS.get()
        );

        this.tag(ItemTags.FOOT_ARMOR).add(
                OFItems.EMERALD_BOOTS.get()
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

        this.tag(CuriosTags.CHARM).addTag(OFItemTags.TRINKETS);

        this.tag(OFItemTags.TRINKETS).add(
                OFItems.RELIC_FEATHER.get(),
                OFItems.OBSIDIAN_HUSK.get(),
                OFItems.ENCHANTED_CHISEL.get(),
                OFItems.POT_OF_GREEN.get(),
                OFItems.THORNY_BRACELET.get(),
                OFItems.RANGER_MANUAL.get(),
                OFItems.SHARPSHOOTER_COIN.get(),
                OFItems.TEAR_OF_AQUARIUS.get(),
                OFItems.BLACK_BELT.get(),
                OFItems.BLOOD_AMULET.get(),
                OFItems.FIERY_CARAPACE.get(),
                OFItems.URANIUM_KNUCKLES.get(),
                OFItems.MOMENTUM_GAUNTLET.get(),
                OFItems.LUCKY_TACKLEBOX.get(),
                OFItems.SLIMY_SHIELD.get(),
                OFItems.STALKER_GLOVES.get(),
                OFItems.MUNITIONS_BOX.get(),
                OFItems.MOON_SHOES.get(),
                OFItems.SPECTRAL_GOGGLES.get(),
                OFItems.CURSED_RING.get(),
                OFDebugItems.SLIMEWARD.get(),
                OFItems.WATER_WALKERS.get(),
                OFItems.FIREPROOF_SHOES.get(),
                OFItems.COMBO_BOOTS.get()
        );

        this.tag(Tags.Items.TOOLS_SHIELD).add(
                OFItems.SPECTRAL_SHIELD.get(),
                OFDebugItems.KNIGHTSOUL.get()
        );

        this.tag(OFItemTags.BASIC_MANA).add(
                Items.LAPIS_LAZULI
        );

        this.tag(OFItemTags.CONSUMES_BASIC_MANA).add(
                OFItems.CURSED_RINGLINE.get()
        );

        this.tag(OFItemTags.ENCHANTABLE_MAGIC).addTag(
                OFItemTags.CONSUMES_BASIC_MANA
        ).add(
                OFItems.MANIA.get()
        );

        this.tag(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS).add(
                OFDebugItems.RINGMASTER.get(),
                OFDebugItems.SOULCLEAVE.get(),
                OFDebugItems.KNIGHTSOUL.get(),
                OFDebugItems.SLIMEWARD.get()
        );
	}
}