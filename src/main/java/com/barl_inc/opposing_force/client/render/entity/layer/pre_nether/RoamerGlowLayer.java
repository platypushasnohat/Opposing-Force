package com.barl_inc.opposing_force.client.render.entity.layer.pre_nether;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.pre_nether.RoamerModel;
import com.barl_inc.opposing_force.entity.pre_nether.Roamer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class RoamerGlowLayer extends RenderLayer<Roamer, RoamerModel> {

    private static final RenderType GLOW = RenderType.eyes(OpposingForce.location("textures/entity/roamer/roamer_glow.png"));
    private static final RenderType FRESNO_GLOW = RenderType.eyes(OpposingForce.location("textures/entity/roamer/roamer_fresno_glow.png"));

    public RoamerGlowLayer(RenderLayerParent<Roamer, RoamerModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Roamer roamer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(roamer.isFresno() ? FRESNO_GLOW : GLOW), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }
}
