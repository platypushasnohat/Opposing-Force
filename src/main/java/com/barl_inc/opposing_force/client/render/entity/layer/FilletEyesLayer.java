package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.FilletModel;
import com.barl_inc.opposing_force.entity.Fillet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class FilletEyesLayer extends RenderLayer<Fillet, FilletModel> {

    private static final RenderType TEXTURE_LOCATION = RenderType.eyes(OpposingForce.location("textures/entity/fillet/fillet_eyes.png"));

    public FilletEyesLayer(RenderLayerParent<Fillet, FilletModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Fillet fillet, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer consumer = buffer.getBuffer(TEXTURE_LOCATION);
        this.getParentModel().renderToBuffer(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }
}