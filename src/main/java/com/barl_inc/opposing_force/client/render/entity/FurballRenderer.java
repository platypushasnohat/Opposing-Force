package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.FurballModel;
import com.barl_inc.opposing_force.client.render.entity.layer.FurballEyesLayer;
import com.barl_inc.opposing_force.entity.Furball;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FurballRenderer extends MobRenderer<Furball, FurballModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/furball/furball.png");

    public FurballRenderer(EntityRendererProvider.Context context) {
        super(context, new FurballModel(context.bakeLayer(OFModelLayers.FURBALL)), 0.5F);
        this.addLayer(new FurballEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Furball furball) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected void setupRotations(Furball furball, PoseStack poseStack, float bob, float yBodyRot, float partialTicks, float scale) {
        super.setupRotations(furball, poseStack, bob, yBodyRot, partialTicks, scale);
        if (furball.getAnimationState() == Furball.JUMP_ANIMATION) {
            float xRot = -Mth.lerp(partialTicks, furball.xRotO, furball.getXRot());
            poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        }
    }
}