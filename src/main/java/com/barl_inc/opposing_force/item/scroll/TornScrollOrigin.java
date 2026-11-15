package com.barl_inc.opposing_force.item.scroll;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public record TornScrollOrigin(Optional<ResourceKey<Structure>> structure, Optional<ResourceKey<Biome>> biome) {

    public static final TornScrollOrigin NONE = new TornScrollOrigin(Optional.empty(), Optional.empty());

    public static final Codec<TornScrollOrigin> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.STRUCTURE).optionalFieldOf("structure").forGetter(TornScrollOrigin::structure),
            ResourceKey.codec(Registries.BIOME).optionalFieldOf("biome").forGetter(TornScrollOrigin::biome)
    ).apply(instance, TornScrollOrigin::new));

    public static final StreamCodec<ByteBuf, TornScrollOrigin> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.STRUCTURE)), TornScrollOrigin::structure,
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.BIOME)), TornScrollOrigin::biome,
            TornScrollOrigin::new
    );
}
