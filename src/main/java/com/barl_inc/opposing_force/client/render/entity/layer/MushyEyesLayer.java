package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.MushyModel;
import com.barl_inc.opposing_force.entity.Mushy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class MushyEyesLayer extends RenderLayer<Mushy, MushyModel> {

    private static final RenderType TEXTURE_LOCATION = RenderType.eyes(OpposingForce.location("textures/entity/mushy/mushy_eyes.png"));

    public MushyEyesLayer(RenderLayerParent<Mushy, MushyModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Mushy mushy, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer consumer = buffer.getBuffer(TEXTURE_LOCATION);
        this.getParentModel().renderToBuffer(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }
}