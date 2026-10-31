package com.barl_inc.opposing_force.client.render.entity.layer;

import com.barl_inc.opposing_force.item.SpectralShieldItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.WeakHashMap;

public class SpectralShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final Direction[] SIDES = {null, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final RandomSource RANDOM = RandomSource.create();
    private static final Map<LivingEntity, Retract> RETRACTING = new WeakHashMap<>();

    public SpectralShieldLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderAnimatedShields(poseStack, buffer, packedLight, entity, ageInTicks, partialTicks);
    }

    public static boolean isShielding(LivingEntity entity) {
        return entity.isUsingItem() && entity.getUseItem().getItem() instanceof SpectralShieldItem;
    }

    public static boolean hasShields(LivingEntity entity) {
        return isShielding(entity) || RETRACTING.containsKey(entity);
    }

    public static void renderAnimatedShields(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, float ageInTicks, float partialTicks) {
        ItemStack stack = ItemStack.EMPTY;
        float grow = 0.0F;
        if (isShielding(entity)) {
            stack = entity.getUseItem();
            grow = getGrow(entity, partialTicks);
            RETRACTING.put(entity, new Retract(stack, grow, ageInTicks));
        } else if (RETRACTING.containsKey(entity)) {
            Retract retract = RETRACTING.get(entity);
            float progress = Mth.clamp((ageInTicks - retract.start()) / 14.0F, 0.0F, 1.0F);
            stack = retract.stack();
            grow = retract.grow() * (1.0F - progress * progress * progress);
            if (progress >= 1.0F) {
                RETRACTING.remove(entity);
            }
        }
        if (grow > 0.0F) {
            renderShields(poseStack, buffer, packedLight, entity, stack, grow, ageInTicks, partialTicks);
        }
    }

    public static float getGrow(LivingEntity entity, float partialTicks) {
        float progress = 1.0F - Mth.clamp((entity.getTicksUsingItem() - 5 + partialTicks) / 14.0F, 0.0F, 1.0F);
        return 1.0F - progress * progress * progress;
    }

    @SuppressWarnings("deprecation")
    public static void renderShields(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, ItemStack stack, float grow, float ageInTicks, float partialTicks) {
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, Sheets.cutoutBlockSheet(), true, stack.hasFoil());
        float pulse = Mth.sin(ageInTicks * 0.15F);
        float size = grow * (1.0F + pulse * 0.04F);
        float wobble = SpectralShieldItem.getWobble(entity, stack, partialTicks);
        float bodyYaw = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.6F + Mth.sin((ageInTicks + i * 10.0F) * 0.12F) * 0.06F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(i * 90.0F + ageInTicks * 3.0F - bodyYaw));
            poseStack.translate(0.0F, 0.0F, -1.0F - pulse * 0.06F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin((ageInTicks + i * 7.0F) * 2.5F) * 25.0F * wobble));
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos((ageInTicks + i * 5.0F) * 3.1F) * 10.0F * wobble));
            poseStack.scale(-size, -size, size);
            poseStack.mulPose(Axis.XP.rotationDegrees(15.0F));
            poseStack.translate(-0.5F, -0.625F, -0.40625F);
            for (Direction side : SIDES) {
                for (BakedQuad quad : model.getQuads(null, side, RANDOM)) {
                    if (!isHandle(quad)) {
                        consumer.putBulkData(poseStack.last(), quad, 1.0F, 1.0F, 1.0F, 1.0F, packedLight, OverlayTexture.NO_OVERLAY);
                    }
                }
            }
            poseStack.popPose();
        }
    }

    private static boolean isHandle(BakedQuad quad) {
        boolean handle = true;
        for (int i = 0; i < 4; i++) {
            float x = Float.intBitsToFloat(quad.getVertices()[i * 8]);
            float z = Float.intBitsToFloat(quad.getVertices()[i * 8 + 2]);
            handle &= x > 0.43F && x < 0.57F && z > 0.43F;
        }
        return handle;
    }

    private record Retract(ItemStack stack, float grow, float start) {
    }
}
