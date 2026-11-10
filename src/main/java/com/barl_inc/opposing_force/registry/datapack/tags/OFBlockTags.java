package com.barl_inc.opposing_force.registry.datapack.tags;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class OFBlockTags {

    public static final TagKey<Block> IGNORED_BY_GHOSTS = modBlockTag("ignored_by_ghosts");

    private static TagKey<Block> modBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OpposingForce.MOD_ID, name));
    }
}
