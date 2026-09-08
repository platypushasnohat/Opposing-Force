package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.misc.DicerLaser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DicerLaserRenderer extends EntityRenderer<DicerLaser> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/misc/dicer_laser.png");

    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.3F;
    private static final float BEAM_RADIUS = 1.0F;

    public DicerLaserRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(DicerLaser laser) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(DicerLaser laser, float entityYaw, float delta, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        double collidePosX = laser.prevCollidePosX + (laser.collidePosX - laser.prevCollidePosX) * delta;
        double collidePosY = laser.prevCollidePosY + (laser.collidePosY - laser.prevCollidePosY) * delta;
        double collidePosZ = laser.prevCollidePosZ + (laser.collidePosZ - laser.prevCollidePosZ) * delta;
        double posX = laser.xo + (laser.getX() - laser.xo) * delta;
        double posY = laser.yo + (laser.getY() - laser.yo) * delta;
        double posZ = laser.zo + (laser.getZ() - laser.zo) * delta;
        float yaw = laser.prevYaw + (laser.renderYaw - laser.prevYaw) * delta;
        float pitch = laser.prevPitch + (laser.renderPitch - laser.prevPitch) * delta;

        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = Mth.floor((laser.timer - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        VertexConsumer consumer = bufferSource.getBuffer(OFRenderTypes.getGlowingEffect(this.getTextureLocation(laser)));

        this.renderStart(frame, poseStack, consumer, packedLight);
        this.renderBeam(length, 180.0F / (float) Math.PI * yaw, 180.0F / (float) Math.PI * pitch, frame, poseStack, consumer, packedLight);

        poseStack.pushPose();
        poseStack.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        this.renderEnd(frame, laser.blockSide, poseStack, consumer, packedLight);
        poseStack.popPose();
    }

    private void renderFlatQuad(int frame, PoseStack poseStack, VertexConsumer builder, int packedLight) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Matrix3f matrix3f = last.normal();
        this.drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, -START_RADIUS, 0, minU, minV, 1, packedLight);
        this.drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, START_RADIUS, 0, minU, maxV, 1, packedLight);
        this.drawVertex(matrix4f, matrix3f, builder, START_RADIUS, START_RADIUS, 0, maxU, maxV, 1, packedLight);
        this.drawVertex(matrix4f, matrix3f, builder, START_RADIUS, -START_RADIUS, 0, maxU, minV, 1, packedLight);
    }

    private void renderStart(int frame, PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        poseStack.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(quat);
        this.renderFlatQuad(frame, poseStack, consumer, packedLight);
        poseStack.popPose();
    }

    private void renderEnd(int frame, Direction direction, PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        poseStack.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(quat);
        this.renderFlatQuad(frame, poseStack, consumer, packedLight);
        poseStack.popPose();
        if (direction == null) {
            return;
        }
        poseStack.pushPose();
        Quaternionf sideQuat = direction.getRotation();
        sideQuat.mul(this.quatFromRotationXYZ(90, 0, 0, true));
        poseStack.mulPose(sideQuat);
        poseStack.translate(0, 0, -0.01f);
        this.renderFlatQuad(frame, poseStack, consumer, packedLight);
        poseStack.popPose();
    }

    private void drawBeam(float length, int frame, PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Matrix3f matrix3f = last.normal();
        float offset = 0;
        this.drawVertex(matrix4f, matrix3f, consumer, -BEAM_RADIUS, offset, 0, minU, minV, 1, packedLight);
        this.drawVertex(matrix4f, matrix3f, consumer, -BEAM_RADIUS, length, 0, minU, maxV, 1, packedLight);
        this.drawVertex(matrix4f, matrix3f, consumer, BEAM_RADIUS, length, 0, maxU, maxV, 1, packedLight);
        this.drawVertex(matrix4f, matrix3f, consumer, BEAM_RADIUS, offset, 0, maxU, minV, 1, packedLight);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame,  PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(this.quatFromRotationXYZ(90, 0, 0, true));
        poseStack.mulPose(this.quatFromRotationXYZ(0, 0, yaw - 90f, true));
        poseStack.mulPose(this.quatFromRotationXYZ(-pitch, 0, 0, true));
        poseStack.pushPose();
        poseStack.mulPose(this.quatFromRotationXYZ(0, Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 90, 0, true));
        this.drawBeam(length, frame, poseStack, consumer, packedLight);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(this.quatFromRotationXYZ(0, -Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 90, 0, true));
        this.drawBeam(length, frame, poseStack, consumer, packedLight);
        poseStack.popPose();
        poseStack.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        VertexConsumer vertex = vertexBuilder.addVertex(matrix, offsetX, offsetY, offsetZ).setColor(1, 1, 1, 1 * alpha).setUv(textureX, textureY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLightIn);
        this.transformNormals(vertex, normals, 1, 0, 1);
    }

    public void transformNormals(VertexConsumer consumer, Matrix3f normals, float x, float y, float z) {
        Vector3f transformed = this.transformNormals(normals, x, y, z);
        consumer.setNormal(transformed.x(), transformed.y(), transformed.z());
    }

    public Vector3f transformNormals(Matrix3f normals, float x, float y, float z) {
        Vector3f input = new Vector3f(x, y, z);
        return normals.transform(input);
    }

    public Quaternionf quatFromRotationXYZ(float x, float y, float z, boolean degrees) {
        if (degrees) {
            x *= Mth.DEG_TO_RAD;
            y *= Mth.DEG_TO_RAD;
            z *= Mth.DEG_TO_RAD;
        }
        return (new Quaternionf()).rotationXYZ(x, y, z);
    }
}