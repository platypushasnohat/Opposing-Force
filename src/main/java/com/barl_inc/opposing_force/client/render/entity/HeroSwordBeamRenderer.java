package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.projectile.HeroSwordBeam;
import com.barl_inc.opposing_force.registry.OFItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class HeroSwordBeamRenderer extends EntityRenderer<HeroSwordBeam> {

    private static final Direction[] SIDES = {null, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final RandomSource RANDOM = RandomSource.create();
    private static final float SIZE = 1.25F;
    private static final float RED = 0.45F;
    private static final float GREEN = 0.75F;
    private static final float BLUE = 1.0F;
    private static final float ALPHA = 0.35F;

    public HeroSwordBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(HeroSwordBeam entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Vec3 velocity = entity.getDeltaMovement();
        float yaw = (float) (Mth.atan2(velocity.x, velocity.z) * Mth.RAD_TO_DEG);
        float pitch = (float) (Mth.atan2(velocity.y, velocity.horizontalDistance()) * Mth.RAD_TO_DEG);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(new ItemStack(OFItems.HERO_SWORD.get()));
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TextureAtlas.LOCATION_BLOCKS));

        poseStack.pushPose();
        poseStack.translate(0.0F, entity.getBbHeight() / 2.0F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        poseStack.mulPose(Axis.YP.rotationDegrees(-45.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(SIZE, SIZE, SIZE);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        for (Direction side : SIDES) {
            for (BakedQuad quad : model.getQuads(null, side, RANDOM)) {
                consumer.putBulkData(poseStack.last(), quad, RED, GREEN, BLUE, ALPHA, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            }
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(HeroSwordBeam entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
