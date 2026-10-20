package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.client.model.entity.GhostModel;
import com.barl_inc.opposing_force.entity.Ghost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GhostItemLayer extends RenderLayer<Ghost, GhostModel> {

    private final ItemInHandRenderer itemInHandRenderer;

    public GhostItemLayer(RenderLayerParent<Ghost, GhostModel> renderer, ItemInHandRenderer itemInHandRenderer) {
        super(renderer);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Ghost ghost, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderHandItem(poseStack, buffer, packedLight, ghost, ghost.getMainHandItem(), false);
        if (SpectralSight.isPerceivedByClient(ghost)) {
            this.renderHandItem(poseStack, buffer, packedLight, ghost, ghost.getOffhandItem(), true);
        }
    }

    private void renderHandItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Ghost ghost, ItemStack stack, boolean left) {
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            this.getParentModel().translateToHand(poseStack, left);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.translate(left ? -0.09375F : 0.09375F, 0.125F, -0.72F);
            this.itemInHandRenderer.renderItem(ghost, stack, left ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, left, poseStack, buffer, packedLight);
            poseStack.popPose();
        }
    }
}
