package com.barl_inc.opposing_force.network;

import com.barl_inc.opposing_force.OpposingForce;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ReconDashEffectPayload(int entityId, Vec3 start, Vec3 end) implements CustomPacketPayload {

    public static final Type<ReconDashEffectPayload> TYPE = new Type<>(OpposingForce.location("recon_dash_effect"));
    private static final StreamCodec<ByteBuf, Vec3> VEC3 = StreamCodec.of(
            (buf, vec) -> {
                buf.writeDouble(vec.x);
                buf.writeDouble(vec.y);
                buf.writeDouble(vec.z);
            },
            buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())
    );
    public static final StreamCodec<ByteBuf, ReconDashEffectPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ReconDashEffectPayload::entityId,
            VEC3, ReconDashEffectPayload::start,
            VEC3, ReconDashEffectPayload::end,
            ReconDashEffectPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReconDashEffectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> OpposingForce.PROXY.spawnReconDashAfterImages(payload.entityId(), payload.start(), payload.end()));
    }
}
