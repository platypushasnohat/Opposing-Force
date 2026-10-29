package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.ParasoulModel;
import com.barl_inc.opposing_force.entity.Parasoul;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class ParasoulRenderer extends MobRenderer<Parasoul, ParasoulModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/parasoul/parasoul.png");

    public ParasoulRenderer(EntityRendererProvider.Context context) {
        super(context, new ParasoulModel(context.bakeLayer(OFModelLayers.PARASOUL)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Parasoul parasoul) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(Parasoul parasoul, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model.alpha = parasoul.getFade(partialTicks);
        super.render(parasoul, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected int getBlockLightLevel(Parasoul parasoul, BlockPos pos) {
        int light = super.getBlockLightLevel(parasoul, pos);
        return light + Math.round((15 - light) * 0.6F);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(Parasoul parasoul, boolean bodyVisible, boolean translucent, boolean glowing) {
        return this.model.alpha > 0.0F ? super.getRenderType(parasoul, bodyVisible, translucent, glowing) : null;
    }
}
