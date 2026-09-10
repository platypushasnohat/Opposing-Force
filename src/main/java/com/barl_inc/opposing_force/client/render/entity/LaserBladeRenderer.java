package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.projectile.LaserBlade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class LaserBladeRenderer extends EntityRenderer<LaserBlade> {

    public final ItemRenderer itemRenderer;

    public LaserBladeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(LaserBlade laserBlade, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, laserBlade.yRotO, laserBlade.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        if (laserBlade.getReturnTime() > 0) {
            poseStack.mulPose(Axis.ZP.rotation((laserBlade.tickCount + partialTicks) * 1.2F));
        } else {
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, laserBlade.xRotO, laserBlade.getXRot()) + 135.0F));
        }
        poseStack.scale(2.0F, 2.0F, 1.0F);
        poseStack.translate(0.0F, 0.0F, -0.475F);
        Minecraft.getInstance().getItemRenderer().renderStatic(laserBlade.getItem(), ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, laserBlade.level(), laserBlade.getId());
        poseStack.popPose();
        super.render(laserBlade, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ResourceLocation getTextureLocation(LaserBlade laserBlade) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}