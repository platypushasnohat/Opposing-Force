package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.projectile.LaserBlade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LaserBladeRenderer extends EntityRenderer<LaserBlade> {

    public final ItemRenderer itemRenderer;

    public LaserBladeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(LaserBlade laserBlade, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(2.0F, 2.0F, 2.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, laserBlade.yRotO, laserBlade.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, laserBlade.xRotO, laserBlade.getXRot()) - 135.0F));
        Vector3f rotation = new Vector3f(1.0F, 1.0F, 0.0F);
        rotation.normalize();
        poseStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI, rotation.x, rotation.y, rotation.z));
        rotation = new Vector3f(1.0F, -1.0F, 0.0F);
        rotation.normalize();
        poseStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI / 2.0F, rotation.x, rotation.y, rotation.z));
        poseStack.mulPose(Axis.ZP.rotation((laserBlade.tickCount + partialTicks) * 0.9F));
        poseStack.translate(0.0F, 0.0F, laserBlade.getBbHeight() * 0.25F);
        Vector3f nextRotateAxis = new Vector3f(1.0F, 1.0F, 0.0F);
        nextRotateAxis.normalize();
        poseStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI, nextRotateAxis.x, nextRotateAxis.y, nextRotateAxis.z));
        poseStack.scale(1.0F, 1.0F, 0.5F);
        this.itemRenderer.renderStatic(laserBlade.getItem(), ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, laserBlade.level(), laserBlade.getId());
        poseStack.popPose();
        super.render(laserBlade, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @SuppressWarnings("deprecation")
    @Override public ResourceLocation getTextureLocation(LaserBlade laserBlade) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}