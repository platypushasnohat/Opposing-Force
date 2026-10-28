package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.projectile.Soundwave;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class SoundwaveRenderer extends EntityRenderer<Soundwave> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/entity/projectiles/soundwave.png");

    public SoundwaveRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Soundwave entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Vec3 head = entity.getPosition(partialTicks);
        List<Vec3> points = entity.getSegmentPoints(head);
        Camera camera = this.entityRenderDispatcher.camera;
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.eyes(TEXTURE));
        int brightness = (int) (entity.getAlpha(partialTicks) * 255.0F);
        int color = 0xFF000000 | brightness << 16 | brightness << 8 | brightness;
        float size = 0.9F * entity.getSize();
        Vec3 direction = entity.getDeltaMovement();
        for (int i = 0; i < points.size(); i++) {
            Vec3 point = points.get(i);
            Vec3 ahead = i == 0 ? point.subtract(points.get(1)) : points.get(i - 1).subtract(point);
            if (ahead.lengthSqr() > 1.0E-6D) {
                direction = ahead;
            }
            Vector3f motion = direction.toVector3f();
            float right = -motion.dot(camera.getLeftVector());
            float up = motion.dot(camera.getUpVector());
            Vec3 offset = point.subtract(head);
            poseStack.pushPose();
            poseStack.translate(offset.x, offset.y, offset.z);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Axis.ZP.rotation((float) Mth.atan2(up, right)));
            poseStack.scale(size, size, size);
            PoseStack.Pose pose = poseStack.last();
            float minU = (2 - i) / 3.0F;
            float maxU = minU + 1.0F / 3.0F;
            vertex(consumer, pose, color, -0.5F, -0.5F, minU, 1.0F);
            vertex(consumer, pose, color, 0.5F, -0.5F, maxU, 1.0F);
            vertex(consumer, pose, color, 0.5F, 0.5F, maxU, 0.0F);
            vertex(consumer, pose, color, -0.5F, 0.5F, minU, 0.0F);
            poseStack.popPose();
        }
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int color, float x, float y, float u, float v) {
        consumer.addVertex(pose, x, y, 0.0F).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Soundwave entity) {
        return TEXTURE;
    }
}
