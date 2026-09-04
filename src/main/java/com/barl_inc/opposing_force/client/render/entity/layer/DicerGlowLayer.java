package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.DicerModel;
import com.barl_inc.opposing_force.entity.Dicer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.render.SinewRenderTypes;
import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class DicerGlowLayer extends RenderLayer<Dicer, DicerModel> {

    private static final RenderType TEXTURE_LOCATION = SinewRenderTypes.getEyesAlphaEnabled(OpposingForce.location("textures/entity/dicer/dicer_glow.png"));

    public DicerGlowLayer(RenderLayerParent<Dicer, DicerModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Dicer fireBeetle, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (fireBeetle.isInvisible()) {
            return;
        }
        VertexConsumer consumer = buffer.getBuffer(TEXTURE_LOCATION);
        this.getParentModel().renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(fireBeetle, 0.0F), SinewColorUtils.packColor(1.0F, 1.0F, 1.0F, 0.5F));
    }
}