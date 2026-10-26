package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.GrellModel;
import com.barl_inc.opposing_force.client.render.entity.layer.GrellBrainLayer;
import com.barl_inc.opposing_force.entity.Grell;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class GrellRenderer extends MobRenderer<Grell, GrellModel> {

    public static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/grell/grell.png");

    public GrellRenderer(EntityRendererProvider.Context context) {
        super(context, new GrellModel(context.bakeLayer(OFModelLayers.GRELL)), 0.0F);
        this.addLayer(new GrellBrainLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Grell grell) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(Grell grell, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model.alpha = grell.getMaterialize(partialTicks);
        super.render(grell, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected int getBlockLightLevel(Grell grell, BlockPos pos) {
        int light = super.getBlockLightLevel(grell, pos);
        return light + Math.round((15 - light) * 0.6F);
    }
}
