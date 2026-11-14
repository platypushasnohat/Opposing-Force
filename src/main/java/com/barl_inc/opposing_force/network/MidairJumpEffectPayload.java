package com.barl_inc.opposing_force.network;

import com.barl_inc.opposing_force.OpposingForce;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MidairJumpEffectPayload(int entityId) implements CustomPacketPayload {

    public static final Type<MidairJumpEffectPayload> TYPE = new Type<>(OpposingForce.location("midair_jump_effect"));
    public static final StreamCodec<ByteBuf, MidairJumpEffectPayload> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(MidairJumpEffectPayload::new, MidairJumpEffectPayload::entityId);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MidairJumpEffectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> OpposingForce.PROXY.midairJumpEffect(payload.entityId()));
    }
}
