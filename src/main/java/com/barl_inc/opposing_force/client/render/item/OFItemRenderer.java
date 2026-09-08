package com.barl_inc.opposing_force.client.render.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.item.BlasterModel;
import com.barl_inc.opposing_force.item.BlasterItem;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class OFItemRenderer extends BlockEntityWithoutLevelRenderer {

    public static final BlasterModel BLASTER_MODEL = new BlasterModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.BLASTER));
    private static final ResourceLocation BLASTER_TEXTURE = OpposingForce.location("textures/item/blaster.png");
    private static final ResourceLocation BLASTER_GLOW_TEXTURE = OpposingForce.location("textures/item/blaster_glow.png");

    public OFItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        LocalPlayer player = Minecraft.getInstance().player;
        float ageInTicks = player == null ? 0.0F : player.tickCount + partialTicks;
        if (stack.is(OFItems.BLASTER.get())) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.5F, 0.5F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.scale(1.0F, 1.0F, 1.0F);
            BLASTER_MODEL.setupAnim(player, (BlasterItem) stack.getItem(), stack, displayContext, ageInTicks);
            BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(BLASTER_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(BLASTER_GLOW_TEXTURE)), LightTexture.FULL_BRIGHT, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                BLASTER_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(BLASTER_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
    }
}
