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

	public SawbladeRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(Sawblade entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
		float despawnTime = entity.getDespawnTime(partialTicks);
		float testScale = 0.75F;
		float ageInTicks = partialTicks + entity.tickCount;
		float minAge = Math.min(1.0F, Math.min(ageInTicks, despawnTime) / 5.0F);
		poseStack.translate(0, entity.getBbHeight() / 2, 0);
		poseStack.scale(testScale, testScale, testScale);
		poseStack.scale(minAge, minAge, minAge);
		poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 90.0F));
		poseStack.mulPose(Axis.ZN.rotationDegrees((float) (Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 5.0F * Math.sin(ageInTicks * 0.2F))));
		poseStack.mulPose(Axis.ZP.rotationDegrees((entity.tickCount + partialTicks) * 45.0F));
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