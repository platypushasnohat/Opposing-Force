package com.barl_inc.opposing_force.client.render.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.item.*;
import com.barl_inc.opposing_force.item.magic.ManiaItem;
import com.barl_inc.opposing_force.item.ranged.LeitmotifItem;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class OFItemRenderer extends BlockEntityWithoutLevelRenderer {

    public static final LeitmotifModel LEITMOTIF_MODEL = new LeitmotifModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.LEITMOTIF));
    public static final ResourceLocation LEITMOTIF_TEXTURE = OpposingForce.location("textures/item/leitmotif.png");

    public OFItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        LocalPlayer player = Minecraft.getInstance().player;
        float ageInTicks = player == null ? 0.0F : player.tickCount + partialTicks;

        // Translate so item model transformations are correct
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(1.0F, 1.0F, 1.0F);

        if (stack.is(OFItems.BLASTER)) {
            BlasterModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
        if (stack.is(OFItems.TRI_BLASTER)) {
            TriBlasterModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
        if (stack.is(OFItems.SCATTER_BLASTER)) {
            ScatterBlasterModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
        if (stack.is(OFItems.LEITMOTIF)) {
            poseStack.pushPose();
            LEITMOTIF_MODEL.setupAnim(player, (LeitmotifItem) stack.getItem(), stack, displayContext, ageInTicks);
            LEITMOTIF_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(LEITMOTIF_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                LEITMOTIF_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(LEITMOTIF_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.CRACKSHOT)) {
            CrackshotModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
        if (stack.is(OFItems.JAEGER)) {
            JaegerModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
        if (stack.is(OFItems.BOOMSTICK)) {
            BoomstickModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
        if (stack.is(OFItems.GORLOFF)) {
            GorloffModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
        if (stack.getItem() instanceof ManiaItem) {
            ManiaModel.renderItem(player, stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ageInTicks);
        }
    }
}
