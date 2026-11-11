package com.barl_inc.opposing_force.network;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.melee.HeroSwordItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record HeroSwordSwingPayload() implements CustomPacketPayload {

    public static final HeroSwordSwingPayload INSTANCE = new HeroSwordSwingPayload();
    public static final Type<HeroSwordSwingPayload> TYPE = new Type<>(OpposingForce.location("hero_sword_swing"));
    public static final StreamCodec<ByteBuf, HeroSwordSwingPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(HeroSwordSwingPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                HeroSwordItem.shootBeam(player);
            }
        });
    }
}
