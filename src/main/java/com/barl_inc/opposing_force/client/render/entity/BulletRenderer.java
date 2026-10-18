package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.projectile.Bullet;
import com.barl_inc.opposing_force.item.base.BulletDefinition;
import com.barl_inc.opposing_force.registry.OFDataMaps;
import com.barl_inc.opposing_force.registry.OFItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BulletRenderer extends EntityRenderer<Bullet> {

    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public BulletRenderer(EntityRendererProvider.Context context, float scale, boolean fullBright) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = scale;
        this.fullBright = fullBright;
    }

    public BulletRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0F, false);
    }

    @Override
    protected int getBlockLightLevel(Bullet entity, BlockPos pos) {
        return this.fullBright ? 15 : super.getBlockLightLevel(entity, pos);
    }

    @Override
    public void render(Bullet entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < MIN_CAMERA_DISTANCE_SQUARED)) {
            poseStack.pushPose();
            poseStack.scale(this.scale, this.scale, this.scale);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            BulletDefinition definition = entity.getItem().getItemHolder().getData(OFDataMaps.BULLET_DEFINITION);
            ItemStack renderItem = definition != null ? definition.renderItem() : OFItems.SHOT_IRON_BULLET.toStack();
            this.itemRenderer.renderStatic(renderItem, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ResourceLocation getTextureLocation(Bullet entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}