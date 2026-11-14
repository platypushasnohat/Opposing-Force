package com.barl_inc.opposing_force.client.utils;

import com.barl_inc.opposing_force.network.MidairJumpPayload;
import com.barl_inc.opposing_force.registry.OFAttributes;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class MidairJumpHandler {

    private static boolean jumpWasDown;
    private static int jumpsUsed;

    public static void tick(LocalPlayer player) {
        boolean jumpDown = Minecraft.getInstance().options.keyJump.isDown();
        boolean jumpPressed = jumpDown && !jumpWasDown;
        jumpWasDown = jumpDown;
        if (player.onGround()) {
            jumpsUsed = 0;
        }
        boolean free = !player.onGround() && !player.isInWater() && !player.isInLava() && !player.getAbilities().flying && !player.isFallFlying() && !player.isPassenger() && !player.onClimbable();
        if (free && jumpPressed && jumpsUsed < (int) player.getAttributeValue(OFAttributes.KIRIN_JUMPS)) {
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x, 0.6D, motion.z);
            player.resetFallDistance();
            jumpsUsed++;
            effect(player);
            PacketDistributor.sendToServer(MidairJumpPayload.INSTANCE);
        }
    }

    public static void effect(int entityId) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getEntity(entityId) instanceof Player player) {
            effect(player);
        }
    }

    private static void effect(Player player) {
        player.level().addParticle(OFParticleTypes.KIRIN_STEP.get(), player.getX(), player.getY() + 0.02D, player.getZ(), player.yBodyRot, 2.5D, 0.0D);
        player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_STEP, player.getSoundSource(), 1.0F, 0.6F + player.getRandom().nextFloat() * 0.2F, false);
    }
}
