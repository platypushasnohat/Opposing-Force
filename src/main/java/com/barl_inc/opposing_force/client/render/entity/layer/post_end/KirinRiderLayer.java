package com.barl_inc.opposing_force.client.render.entity.layer.post_end;

import com.barl_inc.opposing_force.client.model.entity.post_end.KirinModel;
import com.barl_inc.opposing_force.entity.post_end.Kirin;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.render.RiderLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.Entity;

public class KirinRiderLayer extends RiderLayer<Kirin, KirinModel> {

    public KirinRiderLayer(RenderLayerParent<Kirin, KirinModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Kirin entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float bodyYaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
        for (Entity passenger : entity.getPassengers()) {
            boolean firstPerson = passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson();
            if (!firstPerson) {
                Sinew.PROXY.releaseRenderingEntity(passenger.getUUID());
                poseStack.pushPose();
                this.getParentModel().translateRiderToBody(poseStack);
                poseStack.translate(0.0F, 0.2875F, 0.0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.mulPose(Axis.YN.rotationDegrees(360.0F - bodyYaw));
                passenger.setYBodyRot(entity.getYRot());
                renderPassenger(passenger, 0.0F, partialTicks, poseStack, bufferSource, packedLight);
                poseStack.popPose();
                Sinew.PROXY.blockRenderingEntity(passenger.getUUID());
            }
        }
    }
}
