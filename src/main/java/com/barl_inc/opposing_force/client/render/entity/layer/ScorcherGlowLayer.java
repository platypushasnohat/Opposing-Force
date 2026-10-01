package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.ScorcherModel;
import com.barl_inc.opposing_force.entity.Scorcher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.render.SinewRenderTypes;
import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class ScorcherGlowLayer extends RenderLayer<Scorcher, ScorcherModel> {

    private static final RenderType TEXTURE_LOCATION = SinewRenderTypes.getEyesAlphaEnabled(OpposingForce.location("textures/entity/scorcher/scorcher_glow.png"));

    public ScorcherGlowLayer(RenderLayerParent<Scorcher, ScorcherModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Scorcher scorcher, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (scorcher.isInvisible()) {
            return;
        }
        VertexConsumer consumer = buffer.getBuffer(TEXTURE_LOCATION);
        float fireProgress = scorcher.getFireProgress(partialTicks);
        if (fireProgress > 0.0F) {
            this.getParentModel().renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(scorcher, 0.0F), SinewColorUtils.packColor(1.0F, 1.0F, 1.0F, fireProgress));
        }
    }
}
