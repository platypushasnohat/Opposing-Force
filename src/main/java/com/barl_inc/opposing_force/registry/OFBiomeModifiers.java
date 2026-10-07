package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.tags.OFBiomeTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OFBiomeModifiers {

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        addSpawn(context, "dicer", OFBiomeTags.HAS_OVERWORLD_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.DICER.get(), 5, 1, 1));
        addSpawn(context, "bewilder", OFBiomeTags.HAS_SAVANNA_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.BEWILDER.get(), 25, 1, 1));
        addSpawn(context, "gusher", OFBiomeTags.HAS_OVERWORLD_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.GUSHER.get(), 20, 1, 1));
        addSpawn(context, "gnat", OFBiomeTags.HAS_PLAINS_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.GNAT.get(), 100, 4, 4));
        addSpawn(context, "scorcher", OFBiomeTags.HAS_OVERWORLD_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.SCORCHER.get(), 20, 2, 2));
        addSpawn(context, "furball", OFBiomeTags.HAS_FOREST_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.FURBALL.get(), 100, 2, 2));
        addSpawn(context, "tarantula", OFBiomeTags.HAS_DESERT_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.TARANTULA.get(), 10, 1, 1));
        addSpawn(context, "octovine", OFBiomeTags.HAS_MANGROVE_MONSTERS, new MobSpawnSettings.SpawnerData(OFEntities.OCTOVINE.get(), 15, 1, 2));
    }

    @SafeVarargs
    private static void addFeature(BootstrapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, Decoration step, ResourceKey<PlacedFeature>... features) {
        register(context, "add_feature/" + name, () -> new BiomeModifiers.AddFeaturesBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), featureSet(context, features), step));
    }

    private static void addSpawn(BootstrapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, MobSpawnSettings.SpawnerData... spawns) {
        register(context, "add_spawn/" + name, () -> new BiomeModifiers.AddSpawnsBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), List.of(spawns)));
    }

    private static void register(BootstrapContext<BiomeModifier> context, String name, Supplier<? extends BiomeModifier> modifier) {
        context.register(ResourceKey.create(Keys.BIOME_MODIFIERS, OpposingForce.location(name)), modifier.get());
    }

    @SafeVarargs
    private static HolderSet<PlacedFeature> featureSet(BootstrapContext<?> context, ResourceKey<PlacedFeature>... features) {
        return HolderSet.direct(Stream.of(features).map(key -> context.lookup(Registries.PLACED_FEATURE).getOrThrow(key)).collect(Collectors.toList()));
    }
}