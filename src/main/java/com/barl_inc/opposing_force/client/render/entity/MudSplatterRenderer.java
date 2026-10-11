package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.misc.MudSplatter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MudSplatterRenderer extends EntityRenderer<MudSplatter> {
    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/misc/mud_splat.png");

    public MudSplatterRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MudSplatter entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float size = entity.getBbWidth() * 0.5F * entity.getFade(partialTicks);
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.015F + entity.getLayer() * 0.004F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE_LOCATION));
        this.vertex(consumer, pose, -size, -size, 0.0F, 0.0F, packedLight);
        this.vertex(consumer, pose, -size, size, 0.0F, 1.0F, packedLight);
        this.vertex(consumer, pose, size, size, 1.0F, 1.0F, packedLight);
        this.vertex(consumer, pose, size, -size, 1.0F, 0.0F, packedLight);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float z, float u, float v, int packedLight) {
        consumer.addVertex(pose, x, 0.0F, z).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(MudSplatter entity) {
        return TEXTURE_LOCATION;
    }
}
