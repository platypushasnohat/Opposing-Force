package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.entity.Kirin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Inject(method = "handleSetEntityPassengersPacket", at = @At("TAIL"))
    private void opposingForce$kirinMountTip(ClientboundSetPassengersPacket packet, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.getVehicle() instanceof Kirin kirin && kirin.getId() == packet.getVehicle()) {
            minecraft.gui.setOverlayMessage(Component.translatable("entity.opposing_force.kirin.mount_tip"), false);
        }
    }
}
