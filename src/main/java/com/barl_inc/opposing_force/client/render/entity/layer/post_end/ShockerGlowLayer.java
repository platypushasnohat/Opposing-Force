package com.barl_inc.opposing_force.client.render.entity.layer.post_end;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.post_end.ShockerModel;
import com.barl_inc.opposing_force.client.render.entity.OFRenderTypes;
import com.barl_inc.opposing_force.entity.post_end.Shocker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;

public class ShockerGlowLayer extends RenderLayer<Shocker, ShockerModel> {

    private static final RenderType GLOW = OFRenderTypes.getPhasingGlow(OpposingForce.location("textures/entity/shocker/shocker_glow.png"));

    public ShockerGlowLayer(RenderLayerParent<Shocker, ShockerModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Shocker shocker, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!shocker.isInvisible()) {
            int brightness = (int) (255.0F * (0.3F + 0.55F * shocker.getGlow(partialTicks)));
            this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(GLOW), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(255, brightness, brightness, brightness));
        }
    }
}