package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.GhostModel;
import com.barl_inc.opposing_force.entity.Ghost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class GhostGlowLayer extends RenderLayer<Ghost, GhostModel> {

    private static final RenderType TEXTURE_LOCATION = RenderType.eyes(OpposingForce.location("textures/entity/ghost/ghost_glow.png"));

    public GhostGlowLayer(RenderLayerParent<Ghost, GhostModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Ghost ghost, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (SpectralSight.isPerceivedByClient(ghost)) {
            this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(TEXTURE_LOCATION), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        }
    }
}
