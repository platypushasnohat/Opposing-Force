package com.barl_inc.opposing_force.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ShockerHitFov {

    private static int hitTick = -100;

    public static void trigger(int tickCount) {
        hitTick = tickCount;
    }

    public static double modifyFov(double fov, float partialTicks) {
        double modified = fov;
        if (Minecraft.getInstance().player != null) {
            float time = Minecraft.getInstance().player.tickCount - hitTick + partialTicks;
            float zoom = time < 2.0F ? Mth.clamp(time / 2.0F, 0.0F, 1.0F) : 1.0F - Mth.clamp((time - 6.0F) / 12.0F, 0.0F, 1.0F);
            modified = fov * (1.0D - 0.2D * zoom * zoom * (3.0F - 2.0F * zoom));
        }
        return modified;
    }
}