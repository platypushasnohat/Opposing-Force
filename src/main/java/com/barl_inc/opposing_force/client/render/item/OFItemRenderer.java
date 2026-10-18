package com.barl_inc.opposing_force.client.render.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.item.*;
import com.barl_inc.opposing_force.item.ranged.*;
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

    public static final BlasterModel<BlasterItem> BLASTER_MODEL = new BlasterModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.BLASTER));
    public static final ResourceLocation BLASTER_TEXTURE = OpposingForce.location("textures/item/blaster.png");
    public static final ResourceLocation BLASTER_GLOW_TEXTURE = OpposingForce.location("textures/item/blaster_glow.png");

    public static final TriBlasterModel TRI_BLASTER_MODEL = new TriBlasterModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.TRI_BLASTER));
    public static final ResourceLocation TRI_BLASTER_TEXTURE = OpposingForce.location("textures/item/tri_blaster.png");
    public static final ResourceLocation TRI_BLASTER_GLOW_TEXTURE = OpposingForce.location("textures/item/tri_blaster_glow.png");

    public static final ScatterBlasterModel SCATTER_BLASTER_MODEL = new ScatterBlasterModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.SCATTER_BLASTER));
    public static final ResourceLocation SCATTER_BLASTER_TEXTURE = OpposingForce.location("textures/item/scatter_blaster.png");
    public static final ResourceLocation SCATTER_BLASTER_GLOW_TEXTURE = OpposingForce.location("textures/item/scatter_blaster_glow.png");

    public static final LeitmotifModel LEITMOTIF_MODEL = new LeitmotifModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.LEITMOTIF));
    public static final ResourceLocation LEITMOTIF_TEXTURE = OpposingForce.location("textures/item/leitmotif.png");

    public static final CrackshotModel CRACKSHOT_MODEL = new CrackshotModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.CRACKSHOT));
    public static final ResourceLocation CRACKSHOT_TEXTURE = OpposingForce.location("textures/item/crackshot.png");

    public static final JaegerModel JAEGER_MODEL = new JaegerModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.JAEGER));
    public static final ResourceLocation JAEGER_TEXTURE = OpposingForce.location("textures/item/jaeger.png");

    public static final BoomstickModel BOOMSTICK_MODEL = new BoomstickModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.BOOMSTICK));
    public static final ResourceLocation BOOMSTICK_TEXTURE = OpposingForce.location("textures/item/boomstick.png");

    public static final GorloffModel GORLOFF_MODEL = new GorloffModel(Minecraft.getInstance().getEntityModels().bakeLayer(OFModelLayers.GORLOFF));
    public static final ResourceLocation GORLOFF_TEXTURE = OpposingForce.location("textures/item/gorloff.png");

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

        if (stack.is(OFItems.BLASTER.get())) {
            poseStack.pushPose();
            BLASTER_MODEL.setupAnim(player, (BlasterItem) stack.getItem(), stack, displayContext, ageInTicks);
            BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(BLASTER_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(BLASTER_GLOW_TEXTURE)), LightTexture.FULL_BRIGHT, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                BLASTER_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(BLASTER_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.TRI_BLASTER.get())) {
            poseStack.pushPose();
            TRI_BLASTER_MODEL.setupAnim(player, (TriBlasterItem) stack.getItem(), stack, displayContext, ageInTicks);
            TRI_BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(TRI_BLASTER_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            TRI_BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(TRI_BLASTER_GLOW_TEXTURE)), LightTexture.FULL_BRIGHT, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                TRI_BLASTER_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(TRI_BLASTER_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.SCATTER_BLASTER.get())) {
            poseStack.pushPose();
            SCATTER_BLASTER_MODEL.setupAnim(player, (ScatterBlasterItem) stack.getItem(), stack, displayContext, ageInTicks);
            SCATTER_BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(SCATTER_BLASTER_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            SCATTER_BLASTER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(SCATTER_BLASTER_GLOW_TEXTURE)), LightTexture.FULL_BRIGHT, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                SCATTER_BLASTER_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(SCATTER_BLASTER_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.LEITMOTIF.get())) {
            poseStack.pushPose();
            LEITMOTIF_MODEL.setupAnim(player, (LeitmotifItem) stack.getItem(), stack, displayContext, ageInTicks);
            LEITMOTIF_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(LEITMOTIF_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                LEITMOTIF_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(LEITMOTIF_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.CRACKSHOT.get())) {
            poseStack.pushPose();
            CRACKSHOT_MODEL.setupAnim(player, (CrackshotItem) stack.getItem(), stack, displayContext, ageInTicks);
            CRACKSHOT_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(CRACKSHOT_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                CRACKSHOT_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(CRACKSHOT_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.JAEGER.get())) {
            poseStack.pushPose();
            JAEGER_MODEL.setupAnim(player, (JaegerItem) stack.getItem(), stack, displayContext, ageInTicks);
            JAEGER_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(JAEGER_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                JAEGER_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(JAEGER_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.BOOMSTICK.get())) {
            poseStack.pushPose();
            BOOMSTICK_MODEL.setupAnim(player, (BoomstickItem) stack.getItem(), stack, displayContext, ageInTicks);
            BOOMSTICK_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(BOOMSTICK_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                BOOMSTICK_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(BOOMSTICK_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        if (stack.is(OFItems.GORLOFF.get())) {
            poseStack.pushPose();
            GORLOFF_MODEL.setupAnim(player, (GorloffItem) stack.getItem(), stack, displayContext, ageInTicks);
            GORLOFF_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(GORLOFF_TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
            if (stack.hasFoil()) {
                GORLOFF_MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(GORLOFF_TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
            }
            poseStack.popPose();
        }
    }
}
