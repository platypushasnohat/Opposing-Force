package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.client.model.entity.GrellModel;
import com.barl_inc.opposing_force.entity.Grell;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class GrellBrainLayer extends RenderLayer<Grell, GrellModel> {

    public GrellBrainLayer(RenderLayerParent<Grell, GrellModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Grell grell, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getParentModel().alpha > 0.0F) {
            int overlay = LivingEntityRenderer.getOverlayCoords(grell, 0.0F);
            ResourceLocation texture = this.getTextureLocation(grell);
            int brainLight = LightTexture.pack(15, LightTexture.sky(packedLight));
            if (grell.isElite()) {
                brainLight = LightTexture.FULL_BRIGHT;
            }
            this.getParentModel().renderBrain(poseStack, buffer.getBuffer(this.getParentModel().renderType(texture)), brainLight, overlay, 0xFFFFFFFF);
            this.getParentModel().renderBrainLines(poseStack, buffer.getBuffer(RenderType.entityTranslucentEmissive(texture)), LightTexture.FULL_BRIGHT, overlay, 0xFFFFFFFF);
        }
    }
}
