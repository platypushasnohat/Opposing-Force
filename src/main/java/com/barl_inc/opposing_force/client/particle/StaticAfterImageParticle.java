package com.barl_inc.opposing_force.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaticAfterImageParticle extends Particle {

    private final Map<RenderType, List<float[]>> snapshot = new LinkedHashMap<>();
    private final int color;
    private final float baseAlpha;

    public StaticAfterImageParticle(LivingEntity entity, ClientLevel level, double x, double y, double z, int lifetime, int color, float alpha, float partialTicks) {
        super(level, x, y, z);
        this.lifetime = lifetime;
        this.color = color;
        this.baseAlpha = alpha;
        this.alpha = alpha;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.setBoundingBox(entity.getBoundingBox().move(x - entity.getX(), y - entity.getY(), z - entity.getZ()).inflate(1.0D));
        this.capture(entity, partialTicks);
    }

    @SuppressWarnings("unchecked")
    private void capture(LivingEntity entity, float partialTicks) {
        if (!entity.isInvisible() && Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity) instanceof LivingEntityRenderer<?, ?> renderer) {
            LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) renderer;
            EntityModel<LivingEntity> model = livingRenderer.getModel();
            float bodyYaw = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
            float headYaw = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
            float headPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
            float limbSpeed = 0.0F;
            float limbPosition = 0.0F;
            if (entity.isAlive()) {
                limbSpeed = Math.min(entity.walkAnimation.speed(partialTicks), 1.0F);
                limbPosition = entity.walkAnimation.position(partialTicks);
            }
            model.attackTime = entity.getAttackAnim(partialTicks);
            model.young = entity.isBaby();
            model.prepareMobModel(entity, limbPosition, limbSpeed, partialTicks);
            model.setupAnim(entity, limbPosition, limbSpeed, entity.tickCount + partialTicks, headYaw - bodyYaw, headPitch);

            PoseStack poseStack = new PoseStack();
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0F, -1.501F, 0.0F);
            model.renderToBuffer(poseStack, this.record(RenderType.entityTranslucentEmissive(livingRenderer.getTextureLocation(entity))), 15728880, OverlayTexture.NO_OVERLAY, -1);
            this.captureExtra(entity, model, poseStack);
        }
    }

    protected void captureExtra(LivingEntity entity, EntityModel<LivingEntity> model, PoseStack poseStack) {
    }

    protected VertexConsumer record(RenderType renderType) {
        return new Recorder(this.snapshot.computeIfAbsent(renderType, type -> new ArrayList<>()));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.age++;
        this.alpha = this.baseAlpha - (float) this.age / this.lifetime * this.baseAlpha;
        if (this.age >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        double offsetX = this.x - camera.getPosition().x;
        double offsetY = this.y - camera.getPosition().y;
        double offsetZ = this.z - camera.getPosition().z;
        int argb = FastColor.ARGB32.color(Mth.clamp((int) (this.alpha * 255.0F), 0, 255), this.color);
        for (Map.Entry<RenderType, List<float[]>> entry : this.snapshot.entrySet()) {
            VertexConsumer consumer = bufferSource.getBuffer(entry.getKey());
            for (float[] vertex : entry.getValue()) {
                consumer.addVertex((float) (vertex[0] + offsetX), (float) (vertex[1] + offsetY), (float) (vertex[2] + offsetZ))
                        .setColor(argb)
                        .setUv(vertex[3], vertex[4])
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(15728880)
                        .setNormal(vertex[5], vertex[6], vertex[7]);
            }
            bufferSource.endBatch(entry.getKey());
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    private static class Recorder implements VertexConsumer {

        private final List<float[]> vertices;
        private float[] current = new float[8];

        private Recorder(List<float[]> vertices) {
            this.vertices = vertices;
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            this.current = new float[]{x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
            this.vertices.add(this.current);
            return this;
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            this.current[3] = u;
            this.current[4] = v;
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            this.current[5] = x;
            this.current[6] = y;
            this.current[7] = z;
            return this;
        }
    }
}