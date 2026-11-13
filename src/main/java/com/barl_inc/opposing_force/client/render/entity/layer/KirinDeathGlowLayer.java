package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.KirinModel;
import com.barl_inc.opposing_force.entity.Kirin;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class KirinDeathGlowLayer extends RenderLayer<Kirin, KirinModel> {

    private static final ResourceLocation WHITE = OpposingForce.location("textures/entity/kirin/kirin_white.png");

    public KirinDeathGlowLayer(RenderLayerParent<Kirin, KirinModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Kirin kirin, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (kirin.isDeadOrDying()) {
            float glow = Mth.clamp((kirin.getDeathSeconds(partialTicks) - Kirin.DEATH_GLOW_START) / (Kirin.DEATH_LENGTH - Kirin.DEATH_GLOW_START), 0.0F, 1.0F);
            if (glow > 0.0F) {
                this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucentEmissive(WHITE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int) (255.0F * glow), 255, 255, 255));
                poseStack.pushPose();
                poseStack.scale(1.08F, 1.08F, 1.08F);
                this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(WHITE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int) (160.0F * glow), 153, 217, 234));
                poseStack.popPose(); //this isnt really even visible i might just delete it
            }
        }
    }
}
