package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.TerrorModel;
import com.barl_inc.opposing_force.entity.Terror;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class TerrorGlowLayer extends RenderLayer<Terror, TerrorModel> {

    private static final RenderType TEXTURE_LOCATION = RenderType.eyes(OpposingForce.location("textures/entity/terror/terror_glow.png"));

    public TerrorGlowLayer(RenderLayerParent<Terror, TerrorModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Terror terror, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (terror.isInvisible()) {
            return;
        }
        VertexConsumer consumer = buffer.getBuffer(TEXTURE_LOCATION);
        this.getParentModel().renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(terror, 0.0F), SinewColorUtils.packColor(1.0F, 1.0F, 1.0F, 1.0F));
    }
}