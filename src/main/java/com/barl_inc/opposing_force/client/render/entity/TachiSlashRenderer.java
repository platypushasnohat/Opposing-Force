package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.misc.TachiSlash;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TachiSlashRenderer extends EntityRenderer<TachiSlash> {

    private static final ResourceLocation[] FRAMES = new ResourceLocation[8];
    private static final float SIZE = 2.5F;

    static {
        for (int i = 0; i < FRAMES.length; i++) {
            FRAMES[i] = ResourceLocation.withDefaultNamespace("textures/particle/sweep_" + i + ".png");
        }
    }

    public TachiSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TachiSlash entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float age = entity.tickCount + partialTicks;
        int frame = Mth.clamp((int) (age / TachiSlash.LIFETIME * FRAMES.length), 0, FRAMES.length - 1);
        int alpha = (int) (255 * Mth.clamp(1.5F - age / TachiSlash.LIFETIME, 0.0F, 1.0F));
        float half = SIZE / 2.0F;

        poseStack.pushPose();
        poseStack.translate(0.0F, entity.getBbHeight() / 2.0F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(FRAMES[frame]));
        vertex(consumer, pose, -half, half, 0.0F, 0.0F, alpha);
        vertex(consumer, pose, half, half, 1.0F, 0.0F, alpha);
        vertex(consumer, pose, half, -half, 1.0F, 1.0F, alpha);
        vertex(consumer, pose, -half, -half, 0.0F, 1.0F, alpha);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float z, float u, float v, int alpha) {
        consumer.addVertex(pose, x, 0.0F, z).setColor(255, 255, 255, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(TachiSlash entity) {
        return FRAMES[0];
    }
}
