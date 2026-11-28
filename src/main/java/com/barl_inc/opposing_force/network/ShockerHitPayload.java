package com.barl_inc.opposing_force.network;

import com.barl_inc.opposing_force.OpposingForce;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ShockerHitPayload() implements CustomPacketPayload {

    public static final Type<ShockerHitPayload> TYPE = new Type<>(OpposingForce.location("shocker_hit"));
    public static final StreamCodec<ByteBuf, ShockerHitPayload> STREAM_CODEC = StreamCodec.unit(new ShockerHitPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ShockerHitPayload payload, IPayloadContext context) {
        context.enqueueWork(OpposingForce.PROXY::shockerHitFov);
    }
}