package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.projectile.Bullet;
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

public class BulletRenderer extends EntityRenderer<Bullet> {

    private final ItemRenderer itemRenderer;

    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(Bullet entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25D)) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            poseStack.scale(0.6F, 0.6F, 0.6F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
            this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Bullet entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
