package com.barl_inc.opposing_force.client.render.block;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.scroll.TornScrollItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;

public class TornScrollBookshelfRenderer implements BlockEntityRenderer<ChiseledBookShelfBlockEntity> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/block/torn_scroll_bookshelf.png");
    private static final float[][] SLOTS = {{0.0F, 0.0F, 6.0F, 8.0F}, {6.0F, 0.0F, 11.0F, 8.0F}, {11.0F, 0.0F, 16.0F, 8.0F}, {0.0F, 8.0F, 6.0F, 16.0F}, {6.0F, 8.0F, 11.0F, 16.0F}, {11.0F, 8.0F, 16.0F, 16.0F}};

    public TornScrollBookshelfRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ChiseledBookShelfBlockEntity bookshelf, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = bookshelf.getLevel();
        Direction facing = bookshelf.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        Direction right = facing.getCounterClockWise();
        int light = LevelRenderer.getLightColor(level, bookshelf.getBlockPos().relative(facing));
        float shade = level.getShade(facing, true);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        for (int i = 0; i < SLOTS.length; i++) {
            if (bookshelf.getItem(i).getItem() instanceof TornScrollItem) {
                float[] slot = SLOTS[i];
                vertex(consumer, pose, facing, right, slot[0], slot[3], light, shade);
                vertex(consumer, pose, facing, right, slot[2], slot[3], light, shade);
                vertex(consumer, pose, facing, right, slot[2], slot[1], light, shade);
                vertex(consumer, pose, facing, right, slot[0], slot[1], light, shade);
            }
        }
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, Direction facing, Direction right, float u, float v, int light, float shade) {
        float x = 0.5F + facing.getStepX() * 0.501F + right.getStepX() * (u / 16.0F - 0.5F);
        float y = 1.0F - v / 16.0F;
        float z = 0.5F + facing.getStepZ() * 0.501F + right.getStepZ() * (u / 16.0F - 0.5F);
        consumer.addVertex(pose, x, y, z).setColor(shade, shade, shade, 1.0F).setUv(u / 16.0F, v / 16.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}