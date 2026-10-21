package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.client.model.entity.PhasingMonsterModel;
import com.barl_inc.opposing_force.client.render.entity.OFRenderTypes;
import com.barl_inc.opposing_force.entity.base.PhasingMonster;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PhasingGlowLayer<T extends PhasingMonster, M extends PhasingMonsterModel<T>> extends RenderLayer<T, M> {

    private final RenderType renderType;

    public PhasingGlowLayer(RenderLayerParent<T, M> renderer, ResourceLocation texture) {
        super(renderer);
        this.renderType = OFRenderTypes.getPhasingMobGlow(texture);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float fade = this.getParentModel().alpha;
        if (fade > 0.0F) {
            int brightness = (int) (255 * fade);
            this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(this.renderType), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFF000000 | brightness << 16 | brightness << 8 | brightness);
        }
    }
}