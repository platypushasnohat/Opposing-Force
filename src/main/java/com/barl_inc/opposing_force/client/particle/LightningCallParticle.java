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

public class LightningCallParticle extends Particle {

    private final List<Vec3> points = new ArrayList<>();
    private final List<List<Vec3>> branches = new ArrayList<>();
    private final List<Float> branchStarts = new ArrayList<>();

    protected LightningCallParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.lifetime = 14;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        Vec3 reach = new Vec3(xSpeed, ySpeed, zSpeed);
        double length = reach.length();
        int segments = Math.max(8, Mth.ceil(length / 0.9D));
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            Vec3 point = reach.scale(1.0F - t);
            if (i > 0 && i < segments) {
                point = point.add((this.random.nextDouble() - 0.5D) * 1.2D, (this.random.nextDouble() - 0.5D) * 0.4D, (this.random.nextDouble() - 0.5D) * 1.2D);
            }
            this.points.add(point);
            if (i > 0 && i < segments - 1 && this.random.nextFloat() < 0.25F) {
                this.addBranch(point, reach.normalize().reverse(), t);
            }
        }
        this.setBoundingBox(this.getBoundingBox().expandTowards(reach).inflate(2.0D));
    }

    private void addBranch(Vec3 origin, Vec3 down, float start) {
        Vec3 direction = down.add((this.random.nextDouble() - 0.5D) * 2.0D, 0.0D, (this.random.nextDouble() - 0.5D) * 2.0D).normalize();
        double length = 2.0D + this.random.nextDouble() * 3.0D;
        List<Vec3> branch = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            Vec3 point = origin.add(direction.scale(length * i / 4.0D));
            if (i > 0) {
                point = point.add((this.random.nextDouble() - 0.5D) * 0.6D, (this.random.nextDouble() - 0.5D) * 0.3D, (this.random.nextDouble() - 0.5D) * 0.6D);
            }
            branch.add(point);
        }
        this.branches.add(branch);
        this.branchStarts.add(start);
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
        float time = this.age + partialTicks;
        float head = Mth.clamp(time / 3.0F, 0.0F, 1.0F);
        float tail = Mth.clamp((time - 3.0F) / 9.0F, 0.0F, 1.0F);
        float alpha = 1.0F - Mth.clamp((time - 11.0F) / 3.0F, 0.0F, 1.0F);
        List<Vec3> visible = this.slice(this.points, tail, head);
        if (alpha > 0.0F && visible.size() > 1) {
            Vec3 cameraPos = camera.getPosition();
            Vec3 toCamera = cameraPos.subtract(this.x, this.y, this.z);

            PoseStack poseStack = new PoseStack();
            poseStack.translate(this.x - cameraPos.x, this.y - cameraPos.y, this.z - cameraPos.z);
            Matrix4f pose = poseStack.last().pose();

            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            RenderType renderType = OFRenderTypes.getElectricArc();
            VertexConsumer consumer = bufferSource.getBuffer(renderType);
            for (float[] layer : ElectricChargeRenderer.LAYERS) {
                ElectricChargeRenderer.drawArc(consumer, pose, toCamera, visible, 0.09F * layer[0], layer[1], layer[2], layer[3], layer[4] * alpha);
                for (int i = 0; i < this.branches.size(); i++) {
                    float start = this.branchStarts.get(i);
                    if (start <= head && start >= tail) {
                        float grow = Mth.clamp((head - start) * 6.0F, 0.0F, 1.0F);
                        List<Vec3> branch = this.slice(this.branches.get(i), 0.0F, grow);
                        if (branch.size() > 1) {
                            ElectricChargeRenderer.drawArc(consumer, pose, toCamera, branch, 0.045F * layer[0], layer[1], layer[2], layer[3], layer[4] * alpha);
                        }
                    }
                }
            }
            bufferSource.endBatch(renderType);
        }
    }

    private List<Vec3> slice(List<Vec3> line, float from, float to) {
        List<Vec3> result = new ArrayList<>();
        int last = line.size() - 1;
        float start = from * last;
        float end = to * last;
        if (end > start) {
            result.add(this.pointAt(line, start));
            for (int i = Mth.floor(start) + 1; i < end; i++) {
                result.add(line.get(i));
            }
            result.add(this.pointAt(line, end));
        }
        return result;
    }

    private Vec3 pointAt(List<Vec3> line, float index) {
        int i = Math.min(Mth.floor(index), line.size() - 2);
        return line.get(i).lerp(line.get(i + 1), index - i);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LightningCallParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
