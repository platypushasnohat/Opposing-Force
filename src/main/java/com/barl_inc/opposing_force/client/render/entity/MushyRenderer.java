package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.MushyModel;
import com.barl_inc.opposing_force.client.render.entity.layer.MushyEyesLayer;
import com.barl_inc.opposing_force.entity.Mushy;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
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
        /*if (furball.getAnimationState() == Furball.JUMP_ANIMATION) {
            float xRot = -Mth.lerp(partialTicks, furball.xRotO, furball.getXRot());
            poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        }
         */
    }
}