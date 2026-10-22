package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.RingProjectileModel;
import com.barl_inc.opposing_force.entity.projectile.RingProjectile;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RingProjectileRenderer extends EntityRenderer<RingProjectile> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/entity/projectiles/ring_projectile.png");

    private final RingProjectileModel model;

    public RingProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RingProjectileModel(context.bakeLayer(OFModelLayers.RING_PROJECTILE));
    }

    @Override
    public void render(RingProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float ageInTicks = entity.tickCount + partialTicks;
        poseStack.pushPose();
        poseStack.translate(0.0F, entity.getBbHeight() / 2.0F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getIndex() * 72.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks + entity.getIndex() * 4.0F, 0.0F, 0.0F);
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(OFRenderTypes.getPhasingGlow(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFF404040);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RingProjectile entity) {
        return TEXTURE;
    }
}
