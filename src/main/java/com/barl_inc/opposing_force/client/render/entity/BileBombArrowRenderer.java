package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.BileBombArrowModel;
import com.barl_inc.opposing_force.entity.projectile.BileBombArrow;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BileBombArrowRenderer extends EntityRenderer<BileBombArrow> {
    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/projectiles/bile_bomb_arrow.png");

    private final BileBombArrowModel model;

    public BileBombArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BileBombArrowModel(context.bakeLayer(OFModelLayers.BILE_BOMB_ARROW));
    }

    @Override
    public void render(BileBombArrow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        poseStack.scale(0.9F, 0.9F, 0.9F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.28125F, 0.0F);
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutout(TEXTURE_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BileBombArrow entity) {
        return TEXTURE_LOCATION;
    }
}
