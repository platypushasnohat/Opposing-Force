package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.item.base.GunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.item.AnimatedItemModel;
import com.platypushasnohat.sinew.client.utils.ClientItemData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class GunModel<I extends GunItem> extends AnimatedItemModel<I> {

    public boolean outsideParticleContext(ItemDisplayContext context) {
        return context != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND && context != ItemDisplayContext.FIRST_PERSON_LEFT_HAND && context != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND && context != ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

    public void renderGunFlashParticle(ItemStack stack, LivingEntity entity, PoseStack poseStack, ItemDisplayContext context, ParticleOptions flashParticle, ModelPart particleRoot, Vector3f transformation) {
        if (this.outsideParticleContext(context)) {
            return;
        }
        poseStack.pushPose();
        particleRoot.translateAndRotate(poseStack);
        Vector3f position = poseStack.last().pose().transformPosition(transformation);
        poseStack.popPose();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        double x = cameraPos.x + position.x;
        double y = cameraPos.y + position.y;
        double z = cameraPos.z + position.z;
        entity.level().addAlwaysVisibleParticle(flashParticle, true, x, y, z, 0.0D, 0.0D, 0.0D);
        ClientItemData.getOrCreateClientData(stack).remove(GunItem.GUN_FLASH);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root().render(poseStack, buffer, packedLight, packedOverlay);
    }
}
