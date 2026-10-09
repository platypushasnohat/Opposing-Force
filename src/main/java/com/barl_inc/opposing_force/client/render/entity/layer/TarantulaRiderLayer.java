package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.client.model.entity.TarantulaModel;
import com.barl_inc.opposing_force.entity.Tarantula;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.render.RiderLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.Entity;

public class TarantulaRiderLayer extends RiderLayer<Tarantula, TarantulaModel> {

    public TarantulaRiderLayer(RenderLayerParent<Tarantula, TarantulaModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Tarantula entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float bodyYaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
        if (entity.isVehicle()) {
            for (Entity passenger : entity.getPassengers()) {
                if (passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    continue;
                }
                Sinew.PROXY.releaseRenderingEntity(passenger.getUUID());
                poseStack.pushPose();
                this.getParentModel().translateRiderToBody(poseStack);
                poseStack.translate(0.0F, 0.0F, 0.25F);
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
