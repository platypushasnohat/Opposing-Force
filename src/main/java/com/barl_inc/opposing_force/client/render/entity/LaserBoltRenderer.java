package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.LaserBoltModel;
import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class LaserBoltRenderer extends EntityRenderer<LaserBolt> {

    private static final ResourceLocation OUTER_LOCATION = OpposingForce.location("textures/entity/projectile/laser_bolt_outer.png");
    private static final ResourceLocation INNER_LOCATION = OpposingForce.location("textures/entity/projectile/laser_bolt_inner.png");
    private static final ResourceLocation TRAIL_LOCATION = OpposingForce.location("textures/particle/trail.png");

    private final LaserBoltModel model;

    public LaserBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new LaserBoltModel(context.bakeLayer(OFModelLayers.LASER_BOLT));
    }

    @Override
    public void render(LaserBolt laserBolt, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (laserBolt.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(laserBolt) < 12.25F)) {
            poseStack.pushPose();
            VertexConsumer innerTexture = buffer.getBuffer(OFRenderTypes.getLaserBolt(this.getTextureLocation(laserBolt)));
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            float yRot = Mth.rotLerp(partialTicks, laserBolt.yRotO, laserBolt.getYRot());
            float xRot = Mth.lerp(partialTicks, laserBolt.xRotO, laserBolt.getXRot());
            this.model.setupRotation(yRot, xRot);
            this.model.renderToBuffer(poseStack, innerTexture, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1);
            VertexConsumer outerTexture = buffer.getBuffer(OFRenderTypes.eyes(this.getOuterTextureLocation()));
            this.model.renderToBuffer(poseStack, outerTexture, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, SinewColorUtils.packColor(1.0F, 1.0F, 1.0F, 1.0F));
            poseStack.popPose();
            if (laserBolt.hasTrail()) {
                double x = Mth.lerp(partialTicks, laserBolt.xOld, laserBolt.getX());
                double y = Mth.lerp(partialTicks, laserBolt.yOld, laserBolt.getY());
                double z = Mth.lerp(partialTicks, laserBolt.zOld, laserBolt.getZ());
                poseStack.pushPose();
                poseStack.translate(-x, -y, -z);
                int color = 0xff246d;
                float trailAlpha = 1.0F;
                this.renderTrail(laserBolt, partialTicks, poseStack, buffer, SinewColorUtils.unpackRed(color), SinewColorUtils.unpackGreen(color), SinewColorUtils.unpackBlue(color), trailAlpha);
                poseStack.popPose();
            }
            super.render(laserBolt, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    private void renderTrail(LaserBolt laserBolt, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, float red, float green, float blue, float alpha) {
        int samples = 0;
        int sampleSize = 2;
        float trailHeight = 0.1F;
        float trailZRot = 0;
        Vec3 topAngleVec = new Vec3(0, trailHeight, 0).zRot(trailZRot);
        Vec3 bottomAngleVec = new Vec3(0, -trailHeight, 0).zRot(trailZRot);
        Vec3 drawFrom = laserBolt.getTrailPosition(0, partialTicks);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(OFRenderTypes.getLaserBolt(TRAIL_LOCATION));
        while (samples < sampleSize) {
            Vec3 sample = laserBolt.getTrailPosition(samples + 2, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;
            Vec3 draw1 = drawFrom;
            PoseStack.Pose last = poseStack.last();
            Matrix4f matrix4f = last.pose();
            vertexconsumer.addVertex(matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y, (float) draw1.z + (float) bottomAngleVec.z).setColor(red, green, blue, alpha).setUv(u1, 1.0F).setOverlay(NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0F, 1.0F, 0.0F);
            vertexconsumer.addVertex(matrix4f, (float) sample.x + (float) bottomAngleVec.x, (float) sample.y + (float) bottomAngleVec.y, (float) sample.z + (float) bottomAngleVec.z).setColor(red, green, blue, alpha).setUv(u2, 1.0F).setOverlay(NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0F, 1.0F, 0.0F);
            vertexconsumer.addVertex(matrix4f, (float) sample.x + (float) topAngleVec.x, (float) sample.y + (float) topAngleVec.y, (float) sample.z + (float) topAngleVec.z).setColor(red, green, blue, alpha).setUv(u2, 0).setOverlay(NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0F, 1.0F, 0.0F);
            vertexconsumer.addVertex(matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y, (float) draw1.z + (float) topAngleVec.z).setColor(red, green, blue, alpha).setUv(u1, 0).setOverlay(NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0F, 1.0F, 0.0F);
            samples++;
            drawFrom = sample;
        }
    }

    @Override
    protected int getBlockLightLevel(LaserBolt laserBolt, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(LaserBolt laserBolt) {
        return INNER_LOCATION;
    }

    public ResourceLocation getOuterTextureLocation() {
        return OUTER_LOCATION;
    }
}