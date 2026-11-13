package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.KirinModel;
import com.barl_inc.opposing_force.client.render.entity.layer.KirinDeathGlowLayer;
import com.barl_inc.opposing_force.client.render.entity.layer.KirinGlowLayer;
import com.barl_inc.opposing_force.entity.Kirin;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class KirinRenderer extends MobRenderer<Kirin, KirinModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/kirin/kirin.png");

    public KirinRenderer(EntityRendererProvider.Context context) {
        super(context, new KirinModel(context.bakeLayer(OFModelLayers.KIRIN)), 0.0F);
        this.addLayer(new KirinGlowLayer(this));
        this.addLayer(new KirinDeathGlowLayer(this));
    }

    @Override
    public void render(Kirin kirin, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(kirin, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if (kirin.isDeadOrDying()) {
            this.renderBeam(kirin.getDeathSeconds(partialTicks), poseStack, buffer);
        }
    }

    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH
    private void renderBeam(float seconds, PoseStack poseStack, MultiBufferSource buffer) {
        if (seconds > 0.5F) {
            float descend = Mth.clamp((seconds - 0.5F) / 0.25F, 0.0F, 1.0F);
            float grow = Mth.clamp((seconds - 0.5F) / (Kirin.DEATH_GLOW_START - 0.5F), 0.0F, 1.0F);
            float close = Mth.clamp((seconds - (Kirin.DEATH_LENGTH - 0.1F)) / 0.1F, 0.0F, 1.0F);
            float radius = Mth.lerp(grow * grow, 0.05F, 1.0F) * (1.0F - close);
            float bottom = 64.0F * (1.0F - descend * descend);
            Matrix4f pose = poseStack.last().pose();
            VertexConsumer consumer = buffer.getBuffer(OFRenderTypes.getElectricArc());
            drawPrism(consumer, pose, radius, bottom, 0.6F, 0.85F, 0.92F, 0.6F);
            drawPrism(consumer, pose, radius * 0.55F, bottom, 1.0F, 1.0F, 1.0F, 0.9F);
        }
    }

    private static void drawPrism(VertexConsumer consumer, Matrix4f pose, float radius, float bottom, float r, float g, float b, float a) {
        float[][] corners = {{-radius, -radius}, {radius, -radius}, {radius, radius}, {-radius, radius}};
        for (int i = 0; i < 4; i++) {
            float[] from = corners[i];
            float[] to = corners[(i + 1) % 4];
            consumer.addVertex(pose, from[0], bottom, from[1]).setColor(r, g, b, a);
            consumer.addVertex(pose, to[0], bottom, to[1]).setColor(r, g, b, a);
            consumer.addVertex(pose, to[0], 64.0F, to[1]).setColor(r, g, b, a);
            consumer.addVertex(pose, from[0], 64.0F, from[1]).setColor(r, g, b, a);
        }
    }

    @Override
    public boolean shouldRender(Kirin kirin, Frustum frustum, double x, double y, double z) {
        boolean beamVisible = kirin.isDeadOrDying() && frustum.isVisible(kirin.getBoundingBox().inflate(1.0F).expandTowards(0.0D, 64.0F, 0.0D));
        return beamVisible || super.shouldRender(kirin, frustum, x, y, z);
    }

    @Override
    protected int getBlockLightLevel(Kirin kirin, BlockPos pos) {
        return Math.max(super.getBlockLightLevel(kirin, pos), Math.round(10.0F * kirin.getDarkness()));
    }

    @Override
    public ResourceLocation getTextureLocation(Kirin kirin) {
        return TEXTURE_LOCATION;
    }
}