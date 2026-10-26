package com.barl_inc.opposing_force.client.render.shader;

import com.barl_inc.opposing_force.registry.OFItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;
import top.theillusivec4.curios.api.CuriosApi;

public class SpectralLensOverlay {

    public static ShaderInstance shader;
    private static int ticks = -1;
    private static boolean wasEquipped;

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null && !minecraft.isPaused()) {
            boolean equipped = CuriosApi.getCuriosInventory(player).map(inventory -> inventory.isEquipped(OFItems.SPECTRAL_GOGGLES.get())).orElse(false);
            if (equipped && !wasEquipped && ticks < 0 && player.tickCount > 20) {
                ticks = 0;
            }
            wasEquipped = equipped;
        }
        if (ticks >= 0) {
            ticks++;
            if (ticks >= 70 || player == null) {
                ticks = -1;
            }
        }
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (ticks >= 0 && shader != null) {
            float progress = Math.min((ticks + deltaTracker.getGameTimeDeltaPartialTick(false)) / 70, 1.0F);
            float width = guiGraphics.guiWidth();
            float height = guiGraphics.guiHeight();
            Matrix4f pose = guiGraphics.pose().last().pose();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(() -> shader);
            shader.safeGetUniform("Progress").set(progress);
            BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            builder.addVertex(pose, 0.0F, height, -90.0F);
            builder.addVertex(pose, width, height, -90.0F);
            builder.addVertex(pose, width, 0.0F, -90.0F);
            builder.addVertex(pose, 0.0F, 0.0F, -90.0F);
            BufferUploader.drawWithShader(builder.buildOrThrow());
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
    }
}
