package com.barl_inc.opposing_force.network;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.armor.StormLordArmorItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MidairJumpPayload() implements CustomPacketPayload {

    public static final MidairJumpPayload INSTANCE = new MidairJumpPayload();
    public static final Type<MidairJumpPayload> TYPE = new Type<>(OpposingForce.location("midair_jump"));
    public static final StreamCodec<ByteBuf, MidairJumpPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MidairJumpPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                StormLordArmorItem.airJump(player);
            }
        });
    }
}
