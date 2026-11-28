package com.barl_inc.opposing_force.client.particle;

import com.barl_inc.opposing_force.client.render.ElectricArcRendering;
import com.barl_inc.opposing_force.client.render.entity.ElectricChargeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ShockerBoltParticle extends Particle {

    private final int targetId;
    private final double[] jitterA = new double[22];
    private final double[] jitterB = new double[22];
    private Vec3 end;
    private Vec3 prevEnd;

    protected ShockerBoltParticle(ClientLevel level, double x, double y, double z, ShockerBoltParticleOptions options) {
        super(level, x, y, z);
        this.lifetime = 27;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.targetId = options.targetId();
        this.end = this.clip(new Vec3(options.aimX(), options.aimY(), options.aimZ()));
        this.trackTarget();
        this.prevEnd = this.end;
        this.rollJitter();
        this.updateBounds();
    }

    private Vec3 clip(Vec3 aim) {
        Vec3 start = new Vec3(this.x, this.y, this.z);
        return this.level.clip(new ClipContext(start, aim, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation().subtract(start);
    }

    private void trackTarget() {
        Entity target = this.level.getEntity(this.targetId);
        if (target != null && target.isAlive()) {
            this.end = this.clip(target.getBoundingBox().getCenter());
        }
    }

    private void rollJitter() {
        for (int i = 1; i < 21; i++) {
            this.jitterA[i] = (this.random.nextDouble() - 0.5D) * 0.7D;
            this.jitterB[i] = (this.random.nextDouble() - 0.5D) * 0.7D;
        }
    }

    private void updateBounds() {
        this.setBoundingBox(new AABB(this.x, this.y, this.z, this.x, this.y, this.z).expandTowards(this.end).inflate(1.0D));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.prevEnd = this.end;
        if (this.age < 5) {
            this.trackTarget();
            this.updateBounds();
        }
        if (this.age % 2 == 0) {
            this.rollJitter();
        }
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        float time = this.age + partialTicks;
        float head = (time >= 7.0F ? 6 : Math.min(5, Mth.floor(time) + 1)) / 6.0F;
        float alpha = 1.0F - Mth.clamp((time - 17.0F) / 10.0F, 0.0F, 1.0F);
        Vec3 reach = this.prevEnd.lerp(this.end, partialTicks);
        double length = reach.length();
        if (alpha > 0.0F && length > 0.01D) {
            Vec3 direction = reach.scale(1.0D / length);
            Vec3 axisA = Math.abs(direction.y) > 0.99D ? new Vec3(1.0D, 0.0D, 0.0D) : direction.cross(new Vec3(0.0D, 1.0D, 0.0D)).normalize();
            Vec3 axisB = direction.cross(axisA).normalize();

            List<Vec3> bolt = new ArrayList<>();
            int boltPoints = Mth.ceil(head * 21);
            for (int i = 0; i <= boltPoints; i++) {
                bolt.add(reach.scale(i / 21.0D).add(axisA.scale(this.jitterA[i])).add(axisB.scale(this.jitterB[i])));
            }

            List<Vec3> spiral = new ArrayList<>();
            int spiralPoints = Mth.ceil(head * length * 12.0D);
            for (int i = 0; i <= spiralPoints; i++) {
                double along = i / 12.0D;
                double angle = along * Mth.TWO_PI / 1.1D - time * 0.8D;
                double radius = 0.32D * Math.min(1.0D, along / 0.4D) + (this.random.nextDouble() - 0.5D) * 0.05D;
                spiral.add(direction.scale(along).add(axisA.scale(Math.cos(angle) * radius)).add(axisB.scale(Math.sin(angle) * radius)));
            }

            Vec3 cameraPos = camera.getPosition();
            Vec3 toCamera = cameraPos.subtract(this.x, this.y, this.z);
            PoseStack poseStack = new PoseStack();
            poseStack.translate(this.x - cameraPos.x, this.y - cameraPos.y, this.z - cameraPos.z);
            Matrix4f pose = poseStack.last().pose();

            VertexConsumer consumer = ElectricArcRendering.getBuffer();
            for (float[] layer : ElectricChargeRenderer.LAYERS) {
                ElectricChargeRenderer.drawArc(consumer, pose, toCamera, bolt, 0.08F * layer[0], layer[1], layer[2], layer[3], layer[4] * alpha);
            }
            if (spiral.size() > 1) {
                for (float[] layer : ElectricChargeRenderer.PINK_LAYERS) {
                    ElectricChargeRenderer.drawArc(consumer, pose, toCamera, spiral, 0.028F * layer[0], layer[1], layer[2], layer[3], layer[4] * alpha);
                }
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<ShockerBoltParticleOptions> {

        @Override
        public Particle createParticle(ShockerBoltParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ShockerBoltParticle(level, x, y, z, options);
        }
    }
}