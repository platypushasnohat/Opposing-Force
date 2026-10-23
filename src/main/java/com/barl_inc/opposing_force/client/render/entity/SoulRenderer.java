package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.SoulModel;
import com.barl_inc.opposing_force.entity.projectile.Soul;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SoulRenderer extends EntityRenderer<Soul> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/entity/projectiles/soul.png");

    private final SoulModel model;

    public SoulRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SoulModel(context.bakeLayer(OFModelLayers.SOUL));
    }

    @Override
    public void render(Soul entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.5F, -1.5F, 1.5F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        float ageInTicks = entity.tickCount + partialTicks;
        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, (int) (204 * Math.min(ageInTicks / 30.0F, 1.0F)) << 24 | 0xFFFFFF);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Soul entity) {
        return TEXTURE;
    }
}
