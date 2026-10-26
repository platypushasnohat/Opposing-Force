package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.entity.base.PhasingMonster;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.renderer.RenderType;

public abstract class PhasingMonsterModel<T extends PhasingMonster> extends SinewEntityModel<T> {

    public float alpha = 1.0F;

    public PhasingMonsterModel() {
        super(RenderType::entityTranslucent);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        int alpha = (int) (((color >>> 24) & 0xFF) * this.alpha);
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, (alpha << 24) | (color & 0xFFFFFF));
    }
}
