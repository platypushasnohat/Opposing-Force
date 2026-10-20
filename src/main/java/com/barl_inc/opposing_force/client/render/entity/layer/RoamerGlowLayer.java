package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.RoamerModel;
import com.barl_inc.opposing_force.entity.Roamer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.sinew.entity.variant.SinewMobVariant;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RoamerGlowLayer extends RenderLayer<Roamer, RoamerModel> {

    private static final ResourceLocation GLOW_LOCATION = OpposingForce.location("textures/entity/roamer/roamer_glow.png");

    public RoamerGlowLayer(RenderLayerParent<Roamer, RoamerModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Roamer roamer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(roamer.getVariantHolder().map(holder -> SinewMobVariant.fullTextureId(holder.value().rawTexture().withSuffix("_glow"))).orElse(GLOW_LOCATION))), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }
}
