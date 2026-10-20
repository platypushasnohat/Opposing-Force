package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.GhostModel;
import com.barl_inc.opposing_force.client.render.entity.layer.GhostGlowLayer;
import com.barl_inc.opposing_force.client.render.entity.layer.GhostItemLayer;
import com.barl_inc.opposing_force.entity.Ghost;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class GhostRenderer extends MobRenderer<Ghost, GhostModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/ghost/ghost.png");

    public GhostRenderer(EntityRendererProvider.Context context) {
        super(context, new GhostModel(context.bakeLayer(OFModelLayers.GHOST)), 0.0F);
        this.addLayer(new GhostGlowLayer(this));
        this.addLayer(new GhostItemLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Ghost ghost) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(Ghost ghost, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model.alpha = ghost.getFade(partialTicks) * 0.9F;
        super.render(ghost, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void setupRotations(Ghost ghost, PoseStack poseStack, float bob, float yBodyRot, float partialTicks, float scale) {
        super.setupRotations(ghost, poseStack, bob, yBodyRot, partialTicks, scale);
        poseStack.translate(0.0F, 1.4F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-ghost.getTilt(partialTicks)));
        poseStack.translate(0.0F, -1.4F, 0.0F);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(Ghost ghost, boolean bodyVisible, boolean translucent, boolean glowing) {
        return this.model.alpha > 0.0F ? super.getRenderType(ghost, bodyVisible, translucent, glowing) : null;
    }
}