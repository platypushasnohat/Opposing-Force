package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.entity.projectile.Shrapnel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class ShrapnelRenderer extends EntityRenderer<Shrapnel> {

    private final ItemRenderer itemRenderer;

    public ShrapnelRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(Shrapnel entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25D)) {
            TextureAtlasSprite sprite = this.itemRenderer.getModel(entity.getItem(), entity.level(), null, entity.getId()).getParticleIcon();
            RandomSource random = RandomSource.create(entity.getId());
            float uo = random.nextFloat() * 3.0F;
            float vo = random.nextFloat() * 3.0F;
            float u0 = sprite.getU(uo / 4.0F);
            float u1 = sprite.getU((uo + 1.0F) / 4.0F);
            float v0 = sprite.getV(vo / 4.0F);
            float v1 = sprite.getV((vo + 1.0F) / 4.0F);
            poseStack.pushPose();
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            PoseStack.Pose pose = poseStack.last();
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
            vertex(consumer, pose, packedLight, -0.1F, -0.1F, u1, v1);
            vertex(consumer, pose, packedLight, 0.1F, -0.1F, u0, v1);
            vertex(consumer, pose, packedLight, 0.1F, 0.1F, u0, v0);
            vertex(consumer, pose, packedLight, -0.1F, 0.1F, u1, v0);
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, float y, float u, float v) {
        consumer.addVertex(pose, x, y, 0.0F).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Shrapnel entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
