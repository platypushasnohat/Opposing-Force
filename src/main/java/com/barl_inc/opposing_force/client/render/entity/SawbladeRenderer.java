package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.projectile.Sawblade;
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

public class SawbladeRenderer extends EntityRenderer<Sawblade> {

	private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	private final float scale;

	public SawbladeRenderer(EntityRendererProvider.Context context, float scale) {
		super(context);
		this.scale = scale;
	}

	public SawbladeRenderer(EntityRendererProvider.Context context) {
		this(context, 1.0F);
	}

	@Override
	public void render(Sawblade entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
		poseStack.translate(0, entity.getBbHeight() / 2, 0);
		float testScale = 0.75F;
		poseStack.scale(this.scale, this.scale, this.scale);
		poseStack.scale(testScale, testScale, testScale);
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		poseStack.mulPose(Axis.ZP.rotationDegrees(-135.0F));
		if (!entity.inGround) {
			poseStack.mulPose(Axis.ZP.rotationDegrees((entity.tickCount + partialTicks) * -90.0F));
		}
		this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getTextureLocation(Sawblade entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}