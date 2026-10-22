package com.barl_inc.opposing_force.registry.datapack.tags;

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
    public static final TagKey<Biome> HAS_BIRCH_FOREST_MONSTERS = modBiomeTag("has_monster/birch_forest");
    public static final TagKey<Biome> HAS_DARK_FOREST_MONSTERS = modBiomeTag("has_monster/dark_forest");
    public static final TagKey<Biome> HAS_WINDSWEPT_MONSTERS = modBiomeTag("has_monster/windswept");
    public static final TagKey<Biome> HAS_WINDSWEPT_SAVANNA_MONSTERS = modBiomeTag("has_monster/windswept_savanna");
    public static final TagKey<Biome> HAS_WOODED_BADLANDS_MONSTERS = modBiomeTag("has_monster/wooded_badlands");

    public static final TagKey<Biome> HAS_MOTIF_RUIN = modBiomeTag("has_structure/motif_ruin");

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
