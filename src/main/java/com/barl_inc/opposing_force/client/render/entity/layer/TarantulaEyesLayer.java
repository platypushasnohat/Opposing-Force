package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.TarantulaModel;
import com.barl_inc.opposing_force.entity.Tarantula;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TarantulaEyesLayer extends RenderLayer<Tarantula, TarantulaModel> {

    private static final RenderType TEXTURE_LOCATION = RenderType.entityTranslucentEmissive(OpposingForce.location("textures/entity/tarantula/tarantula_eyes.png"));

    public TarantulaEyesLayer(RenderLayerParent<Tarantula, TarantulaModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Tarantula tarantula, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer consumer = buffer.getBuffer(TEXTURE_LOCATION);
        this.getParentModel().renderToBuffer(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }
}