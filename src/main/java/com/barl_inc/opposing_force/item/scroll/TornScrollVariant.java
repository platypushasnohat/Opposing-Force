package com.barl_inc.opposing_force.item.scroll;

import com.barl_inc.opposing_force.registry.OFTornScrolls;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public record TornScrollVariant(Stage stage, Component name, int weight, Optional<HolderSet<Structure>> structures, Optional<HolderSet<Biome>> biomes) {

    public static final Codec<TornScrollVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Stage.CODEC.fieldOf("stage").forGetter(TornScrollVariant::stage),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(TornScrollVariant::name),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(TornScrollVariant::weight),
            RegistryCodecs.homogeneousList(Registries.STRUCTURE).optionalFieldOf("structures").forGetter(TornScrollVariant::structures),
            RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(TornScrollVariant::biomes)
    ).apply(instance, TornScrollVariant::new));

    public static final Codec<TornScrollVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Stage.CODEC.fieldOf("stage").forGetter(TornScrollVariant::stage),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(TornScrollVariant::name)
    ).apply(instance, (stage, name) -> new TornScrollVariant(stage, name, 1, Optional.empty(), Optional.empty())));

    public static final Codec<Holder<TornScrollVariant>> CODEC = RegistryFixedCodec.create(OFTornScrolls.TORN_SCROLL);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<TornScrollVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(OFTornScrolls.TORN_SCROLL);

    public enum Stage implements StringRepresentable {
        BLANK("blank", ChatFormatting.GRAY),
        PRE_NETHER("pre_nether", ChatFormatting.GREEN),
        POST_NETHER("post_nether", ChatFormatting.RED),
        POST_END("post_end", ChatFormatting.LIGHT_PURPLE);

        public static final Codec<Stage> CODEC = StringRepresentable.fromEnum(Stage::values);

        private final String name;
        private final ChatFormatting color;

        Stage(String name, ChatFormatting color) {
            this.name = name;
            this.color = color;
        }

        public Component getDisplayName() {
            return Component.translatable("torn_scroll.opposing_force.stage." + this.name).withStyle(this.color);
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}