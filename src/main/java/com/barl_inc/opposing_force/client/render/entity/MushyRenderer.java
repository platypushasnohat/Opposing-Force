package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.MushyModel;
import com.barl_inc.opposing_force.client.render.entity.layer.MushyEyesLayer;
import com.barl_inc.opposing_force.entity.Mushy;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MushyRenderer extends MobRenderer<Mushy, MushyModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/mushy/mushy.png");

    public MushyRenderer(EntityRendererProvider.Context context) {
        super(context, new MushyModel(context.bakeLayer(OFModelLayers.MUSHY)), 0.2F);
        this.addLayer(new MushyEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Mushy mushy) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected void setupRotations(Mushy mushy, PoseStack poseStack, float bob, float yBodyRot, float partialTicks, float scale) {
        super.setupRotations(mushy, poseStack, bob, yBodyRot, partialTicks, scale);
        float tilt = mushy.getTilt(partialTicks);
        if (tilt > 0.0F) {
            float relativeYaw = yBodyRot - mushy.getLaunchTiltYaw();
            float pivot = mushy.getBbHeight() * 0.5F;
            poseStack.translate(0.0F, pivot, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(relativeYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(-tilt));
            poseStack.mulPose(Axis.YP.rotationDegrees(-relativeYaw));
            poseStack.translate(0.0F, -pivot, 0.0F);
        }
    }
}