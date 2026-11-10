package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.HolderLookup;
import com.barl_inc.opposing_force.registry.datapack.tags.OFBlockTags;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class OFBlockTagsProvider extends BlockTagsProvider {

    public OFBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
        super(output, provider, OpposingForce.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(OFBlockTags.IGNORED_BY_GHOSTS).add(
                Blocks.GLOW_LICHEN,
                Blocks.MAGMA_BLOCK,
                Blocks.AMETHYST_CLUSTER,
                Blocks.LARGE_AMETHYST_BUD,
                Blocks.MEDIUM_AMETHYST_BUD,
                Blocks.SMALL_AMETHYST_BUD,
                Blocks.SCULK_SENSOR,
                Blocks.CALIBRATED_SCULK_SENSOR,
                Blocks.SCULK_CATALYST,
                Blocks.CAVE_VINES,
                Blocks.CAVE_VINES_PLANT,
                Blocks.SEA_PICKLE,
                Blocks.BROWN_MUSHROOM,
                Blocks.CRYING_OBSIDIAN,
                Blocks.TRIAL_SPAWNER,
                Blocks.VAULT
        );
    }
}
