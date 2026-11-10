package com.barl_inc.opposing_force.network;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.armor.ReconKnightArmorItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ReconDashPayload(int direction) implements CustomPacketPayload {

    public static final Type<ReconDashPayload> TYPE = new Type<>(OpposingForce.location("recon_dash"));
    public static final StreamCodec<ByteBuf, ReconDashPayload> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(ReconDashPayload::new, ReconDashPayload::direction);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReconDashPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ReconKnightArmorItem.dash(player, payload.direction());
            }
        });
    }
}
