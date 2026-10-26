package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.client.model.entity.GrellModel;
import com.barl_inc.opposing_force.client.render.entity.GrellRenderer;
import com.barl_inc.opposing_force.entity.Grell;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class GrellBrainLayer extends RenderLayer<Grell, GrellModel> {

    private static final RenderType BRAIN_LINES = RenderType.entityTranslucentEmissive(GrellRenderer.TEXTURE_LOCATION);

    public GrellBrainLayer(RenderLayerParent<Grell, GrellModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Grell grell, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getParentModel().alpha > 0.0F) {
            this.getParentModel().renderBrainLines(poseStack, buffer.getBuffer(BRAIN_LINES), LightTexture.FULL_BRIGHT, LivingEntityRenderer.getOverlayCoords(grell, 0.0F), 0xFFFFFFFF);
        }
    }
}
