package com.barl_inc.opposing_force.client.particle;

import com.barl_inc.opposing_force.client.render.entity.ElectricChargeRenderer;
import com.barl_inc.opposing_force.client.render.entity.OFRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ElectricArcParticle extends Particle {

    private static final float WIDTH = 0.025F;
    private static final float ALPHA = 0.75F;

    private final List<Vec3> points = new ArrayList<>();

    protected ElectricArcParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 3 + this.random.nextInt(3);
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.createArc();
    }

    private void createArc() {
        Vec3 direction = this.randomUnitVector();
        float length = 0.2F + this.random.nextFloat() * 0.25F;
        int segments = 3 + this.random.nextInt(2);
        Vec3 start = direction.scale(-length / 2.0F);
        Vec3 end = direction.scale(length / 2.0F);
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            Vec3 point = start.lerp(end, t);
            if (i > 0 && i < segments) {
                point = point.add(this.randomUnitVector().scale(length * 0.18F));
            }
            this.points.add(point);
        }
    }

    private Vec3 randomUnitVector() {
        double theta = this.random.nextDouble() * Math.PI * 2.0D;
        double y = this.random.nextDouble() * 2.0D - 1.0D;
        double horizontal = Math.sqrt(1.0D - y * y);
        return new Vec3(horizontal * Math.cos(theta), y, horizontal * Math.sin(theta));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        float progress = Mth.clamp((this.age + partialTicks) / this.lifetime, 0.0F, 1.0F);
        float alpha = ALPHA * (1.0F - progress * progress);
        if (alpha <= 0.0F) return;

        Vec3 cameraPos = camera.getPosition();
        double x = Mth.lerp(partialTicks, this.xo, this.x);
        double y = Mth.lerp(partialTicks, this.yo, this.y);
        double z = Mth.lerp(partialTicks, this.zo, this.z);
        Vec3 toCamera = cameraPos.subtract(x, y, z);

        PoseStack poseStack = new PoseStack();
        poseStack.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
        Matrix4f pose = poseStack.last().pose();

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderType renderType = OFRenderTypes.getElectricArc();
        VertexConsumer consumer = bufferSource.getBuffer(renderType);
        for (float[] layer : ElectricChargeRenderer.LAYERS) {
            ElectricChargeRenderer.drawArc(consumer, pose, toCamera, this.points, WIDTH * layer[0], layer[1], layer[2], layer[3], layer[4] * alpha);
        }
        bufferSource.endBatch(renderType);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ElectricArcParticle(level, x, y, z);
        }
    }
}