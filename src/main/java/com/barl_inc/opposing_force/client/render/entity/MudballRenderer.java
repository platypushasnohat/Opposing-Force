package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.MudballModel;
import com.barl_inc.opposing_force.entity.projectile.Mudball;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MudballRenderer extends EntityRenderer<Mudball> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/projectiles/mudball.png");

    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);

    private final MudballModel model;

    public MudballRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new MudballModel(context.bakeLayer(OFModelLayers.MUDBALL));
    }

    @Override
    public void render(Mudball entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < (double) MIN_CAMERA_DISTANCE_SQUARED)) {
            poseStack.pushPose();
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0F, -1.501F, 0.0F);
            float ageInTicks = (float) entity.tickCount + partialTicks;
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE_LOCATION));
            this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
            this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Mudball entity) {
        return TEXTURE_LOCATION;
    }
}
