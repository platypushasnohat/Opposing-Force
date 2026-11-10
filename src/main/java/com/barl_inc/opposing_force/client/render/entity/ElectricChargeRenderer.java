package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.entity.projectile.ElectricCharge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ElectricChargeRenderer extends EntityRenderer<ElectricCharge> {

    private static final float BALL_RADIUS = 0.45F;
    private static final float CORE_RADIUS = 0.16F;
    private static final float BURST_RADIUS = ElectricCharge.IMPACT_RADIUS;

    public static final float[][] LAYERS = {
            {3.2F, 0.20F, 0.40F, 1.00F, 0.22F},
            {1.8F, 0.45F, 0.70F, 1.00F, 0.50F},
            {0.7F, 1.00F, 1.00F, 1.00F, 1.00F}
    };

    private static final float BALL_SPEED = 1.15F;
    private static final float BALL_REFRESH_TICKS = 2.0F;

    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);

    private static final float[][] CUBE_LAYERS = {
            {0.20F, 0.30F, 0.55F, 1.00F, 0.30F},
            {0.14F, 0.60F, 0.80F, 1.00F, 0.55F},
            {0.09F, 1.00F, 1.00F, 1.00F, 1.00F}
    };

    private final RandomSource random = RandomSource.create();

    public ElectricChargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public boolean shouldRender(ElectricCharge entity, Frustum frustum, double x, double y, double z) {
        AABB box = entity.getBoundingBox().inflate(entity.isDischarging() ? BURST_RADIUS : 0.5D);
        return frustum.isVisible(box);
    }

    @Override
    public void render(ElectricCharge entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        boolean tooClose = entity.tickCount < 2 && this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < MIN_CAMERA_DISTANCE_SQUARED;
        if (!tooClose) {
            this.renderCharge(entity, partialTicks, poseStack, bufferSource);
        }
    }

    private void renderCharge(ElectricCharge entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
        boolean discharging = entity.isDischarging();
        int tick = discharging ? entity.getDischargeTicks() + 10000 : (int) ((entity.tickCount + partialTicks) * BALL_SPEED / BALL_REFRESH_TICKS);

        if (entity.lastArcTick != tick || entity.arcs.isEmpty()) {
            entity.lastArcTick = tick;
            entity.arcs = discharging ? this.createBurst(entity.getDischargeTicks()) : this.createBall();
        }

        float alpha = 1.0F;
        if (discharging) {
            float progress = Mth.clamp((entity.getDischargeTicks() + partialTicks) / ElectricCharge.DISCHARGE_DURATION, 0.0F, 1.0F);
            alpha = 1.0F - progress * progress;
        }

        double centerX = Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double centerY = Mth.lerp(partialTicks, entity.yOld, entity.getY()) + entity.getBbHeight() / 2.0F;
        double centerZ = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
        Vec3 camera = this.entityRenderDispatcher.camera.getPosition().subtract(centerX, centerY, centerZ);

        poseStack.pushPose();
        poseStack.translate(0.0F, entity.getBbHeight() / 2.0F, 0.0F);
        Matrix4f pose = poseStack.last().pose();
        VertexConsumer consumer = bufferSource.getBuffer(OFRenderTypes.getElectricArc());

        float baseWidth = discharging ? 0.032F : 0.021F;
        for (float[] layer : LAYERS) {
            float width = baseWidth * layer[0];
            for (List<Vec3> arc : entity.arcs) {
                drawArc(consumer, pose, camera, arc, width, layer[1], layer[2], layer[3], layer[4] * alpha);
            }
        }

        float age = entity.tickCount + partialTicks;
        float cubeScale = 1.0F + Mth.sin(age * 0.6F * BALL_SPEED) * 0.08F;
        if (discharging) {
            cubeScale *= 1.0F + (1.0F - alpha) * 0.8F;
        }
        Quaternionf spin = new Quaternionf().rotationXYZ(age * 0.11F * BALL_SPEED, age * 0.17F * BALL_SPEED, age * 0.07F * BALL_SPEED);
        for (int i = CUBE_LAYERS.length - 1; i >= 0; i--) {
            float[] layer = CUBE_LAYERS[i];
            this.drawCube(consumer, pose, spin, layer[0] * cubeScale, layer[1], layer[2], layer[3], layer[4] * alpha);
        }

        poseStack.popPose();
    }

    public static void drawArc(VertexConsumer consumer, Matrix4f pose, Vec3 camera, List<Vec3> points, float width, float r, float g, float b, float a) {
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3 start = points.get(i);
            Vec3 end = points.get(i + 1);
            Vec3 direction = end.subtract(start);
            Vec3 side = direction.cross(camera.subtract(start));
            if (direction.lengthSqr() >= 1.0E-6D && side.lengthSqr() >= 1.0E-6D) {
                Vec3 extension = direction.normalize().scale(width * 0.5F);
                Vec3 from = start.subtract(extension);
                Vec3 to = end.add(extension);
                side = side.normalize().scale(width);

                vertex(consumer, pose, from.subtract(side), r, g, b, a);
                vertex(consumer, pose, from.add(side), r, g, b, a);
                vertex(consumer, pose, to.add(side), r, g, b, a);
                vertex(consumer, pose, to.subtract(side), r, g, b, a);
            }
        }
    }

    private void drawCube(VertexConsumer consumer, Matrix4f pose, Quaternionf rotation, float halfSize, float r, float g, float b, float a) {
        Vector3f[] corners = new Vector3f[8];
        for (int i = 0; i < 8; i++) {
            Vector3f corner = new Vector3f((i & 1) == 0 ? -halfSize : halfSize, (i & 2) == 0 ? -halfSize : halfSize, (i & 4) == 0 ? -halfSize : halfSize);
            corners[i] = rotation.transform(corner);
        }
        int[][] faces = {{0, 1, 3, 2}, {4, 6, 7, 5}, {0, 4, 5, 1}, {2, 3, 7, 6}, {0, 2, 6, 4}, {1, 5, 7, 3}};
        for (int[] face : faces) {
            for (int index : face) {
                Vector3f corner = corners[index];
                consumer.addVertex(pose, corner.x(), corner.y(), corner.z()).setColor(r, g, b, a);
            }
        }
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Vec3 pos, float r, float g, float b, float a) {
        consumer.addVertex(pose, (float) pos.x, (float) pos.y, (float) pos.z).setColor(r, g, b, a);
    }

    private List<List<Vec3>> createBall() {
        List<List<Vec3>> arcs = new ArrayList<>();
        int shellArcs = 6 + this.random.nextInt(3);
        for (int i = 0; i < shellArcs; i++) {
            arcs.add(this.createShellArc(BALL_RADIUS, 5 + this.random.nextInt(4)));
        }
        int coreArcs = 3 + this.random.nextInt(2);
        for (int i = 0; i < coreArcs; i++) {
            arcs.add(this.createCoreArc());
        }
        return arcs;
    }

    private List<Vec3> createShellArc(float radius, int segments) {
        List<Vec3> points = new ArrayList<>();
        Vec3 normal = this.randomUnitVector();
        Vec3 tangent = this.randomUnitVector().cross(normal).normalize();
        float step = 0.35F + this.random.nextFloat() * 0.2F;

        for (int i = 0; i <= segments; i++) {
            float jitter = 0.8F + this.random.nextFloat() * 0.45F;
            points.add(normal.scale(radius * jitter));

            Vec3 bend = this.randomUnitVector().scale(0.45F);
            tangent = tangent.add(bend).subtract(normal.scale(tangent.add(bend).dot(normal))).normalize();
            normal = normal.scale(Math.cos(step)).add(tangent.scale(Math.sin(step))).normalize();
        }
        return points;
    }

    private List<Vec3> createCoreArc() {
        List<Vec3> points = new ArrayList<>();
        Vec3 direction = this.randomUnitVector();
        int segments = 4;
        for (int i = 0; i <= segments; i++) {
            float t = -1.0F + 2.0F * i / segments;
            Vec3 offset = i == 0 || i == segments ? Vec3.ZERO : this.randomUnitVector().scale(CORE_RADIUS * 0.45F);
            points.add(direction.scale(CORE_RADIUS * t).add(offset));
        }
        return points;
    }

    private List<List<Vec3>> createBurst(int age) {
        List<List<Vec3>> arcs = new ArrayList<>();
        float growth = Mth.clamp((age + 1.0F) / 3.0F, 0.0F, 1.0F);
        float reach = BURST_RADIUS * growth;

        int bolts = 10 + this.random.nextInt(5);
        for (int i = 0; i < bolts; i++) {
            float length = reach * (0.55F + this.random.nextFloat() * 0.45F);
            this.createBolt(arcs, Vec3.ZERO, this.randomUnitVector(), length, 7, true);
        }

        float shell = BALL_RADIUS + (reach * 0.35F);
        for (int i = 0; i < 5; i++) {
            arcs.add(this.createShellArc(shell, 6));
        }
        for (int i = 0; i < 3; i++) {
            arcs.add(this.createCoreArc());
        }
        return arcs;
    }

    private void createBolt(List<List<Vec3>> arcs, Vec3 origin, Vec3 direction, float length, int segments, boolean canBranch) {
        List<Vec3> points = new ArrayList<>();
        points.add(origin);
        Vec3 end = origin.add(direction.scale(length));
        float spread = length * 0.12F;

        for (int i = 1; i <= segments; i++) {
            float t = (float) i / segments;
            Vec3 point = origin.lerp(end, t);
            if (i < segments) {
                point = point.add(this.randomUnitVector().scale(spread * Mth.sin(t * Mth.PI) + spread * 0.3F));
            }
            points.add(point);

            if (canBranch && i < segments - 1 && this.random.nextFloat() < 0.22F) {
                Vec3 branchDirection = direction.add(this.randomUnitVector().scale(0.9F)).normalize();
                this.createBolt(arcs, point, branchDirection, length * (1.0F - t) * 0.6F, 4, false);
            }
        }
        arcs.add(points);
    }

    private Vec3 randomUnitVector() {
        double theta = this.random.nextDouble() * Math.PI * 2.0D;
        double y = this.random.nextDouble() * 2.0D - 1.0D;
        double horizontal = Math.sqrt(1.0D - y * y);
        return new Vec3(horizontal * Math.cos(theta), y, horizontal * Math.sin(theta));
    }

    @Override
    public ResourceLocation getTextureLocation(ElectricCharge entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}