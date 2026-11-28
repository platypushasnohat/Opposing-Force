package com.barl_inc.opposing_force.client.particle;

import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ShockerBoltParticleOptions(int targetId, float aimX, float aimY, float aimZ) implements ParticleOptions {

    public static final MapCodec<ShockerBoltParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("target").forGetter(ShockerBoltParticleOptions::targetId),
            Codec.FLOAT.fieldOf("aim_x").forGetter(ShockerBoltParticleOptions::aimX),
            Codec.FLOAT.fieldOf("aim_y").forGetter(ShockerBoltParticleOptions::aimY),
            Codec.FLOAT.fieldOf("aim_z").forGetter(ShockerBoltParticleOptions::aimZ)
    ).apply(instance, ShockerBoltParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShockerBoltParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ShockerBoltParticleOptions::targetId,
            ByteBufCodecs.FLOAT, ShockerBoltParticleOptions::aimX,
            ByteBufCodecs.FLOAT, ShockerBoltParticleOptions::aimY,
            ByteBufCodecs.FLOAT, ShockerBoltParticleOptions::aimZ,
            ShockerBoltParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return OFParticleTypes.SHOCKER_BOLT.get();
    }
}