package com.barl_inc.opposing_force.tags;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class OFBiomeTags {

    public static final TagKey<Biome> HAS_OVERWORLD_MONSTERS = modBiomeTag("has_monster/overworld");
    public static final TagKey<Biome> HAS_SAVANNA_MONSTERS = modBiomeTag("has_monster/savanna");
    public static final TagKey<Biome> HAS_PLAINS_MONSTERS = modBiomeTag("has_monster/plains");
    public static final TagKey<Biome> HAS_FOREST_MONSTERS = modBiomeTag("has_monster/forest");
    public static final TagKey<Biome> HAS_DESERT_MONSTERS = modBiomeTag("has_monster/desert");
    public static final TagKey<Biome> HAS_MANGROVE_MONSTERS = modBiomeTag("has_monster/mangrove");

    private static TagKey<Biome> modBiomeTag(String name) {
        return biomeTag(OpposingForce.MOD_ID, name);
    }

    private static TagKey<Biome> commonBiomeTag(String name) {
        return biomeTag("c", name);
    }

    public static TagKey<Biome> biomeTag(String modid, String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
