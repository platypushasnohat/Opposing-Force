package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.client.render.ElectricArcRendering;
import com.barl_inc.opposing_force.entity.misc.SuperLightningBolt;
import com.barl_inc.opposing_force.utils.OFLightning;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class OFLightningBoltRenderer extends EntityRenderer<LightningBolt> {

    private final LightningBoltRenderer vanilla;

    public OFLightningBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new LightningBoltRenderer(context);
    }

    @Override
    public void render(LightningBolt bolt, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (OFLightning.isEnabled()) {
            this.renderBolt(bolt, partialTicks, poseStack);
        } else {
            this.vanilla.render(bolt, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    private void renderBolt(LightningBolt bolt, float partialTicks, PoseStack poseStack) {
        boolean large = bolt instanceof SuperLightningBolt;
        RandomSource random = RandomSource.create(bolt.seed);
        double height = large ? 160.0D : 128.0D;
        double jitter = large ? 3.2D : 2.4D;
        int segments = Mth.ceil(height / 1.6D);

        List<Vec3> points = new ArrayList<>();
        List<List<Vec3>> branches = new ArrayList<>();
        List<Float> branchStarts = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            Vec3 point = new Vec3(0.0D, height * (1.0F - t), 0.0D);
            if (i < segments) {
                point = point.add((random.nextDouble() - 0.5D) * jitter, (random.nextDouble() - 0.5D) * 0.8D, (random.nextDouble() - 0.5D) * jitter);
            }
            points.add(point);
            if (i > 0 && i < segments - 1 && random.nextFloat() < (large ? 0.4F : 0.25F)) {
                Vec3 direction = new Vec3((random.nextDouble() - 0.5D) * 2.0D, -1.0D, (random.nextDouble() - 0.5D) * 2.0D).normalize();
                double length = (large ? 6.0D : 4.0D) + random.nextDouble() * (large ? 8.0D : 6.0D);
                List<Vec3> branch = new ArrayList<>();
                for (int j = 0; j <= 5; j++) {
                    Vec3 branchPoint = point.add(direction.scale(length * j / 5.0D));
                    if (j > 0) {
                        branchPoint = branchPoint.add((random.nextDouble() - 0.5D) * 1.2D, (random.nextDouble() - 0.5D) * 0.6D, (random.nextDouble() - 0.5D) * 1.2D);
                    }
                    branch.add(branchPoint);
                }
                branches.add(branch);
                branchStarts.add(t);
            }
        }

        float head = Mth.clamp((bolt.tickCount + partialTicks) / 2.0F, 0.0F, 1.0F);
        List<Vec3> visible = this.slice(points, head);
        if (visible.size() > 1) {
            Vec3 toCamera = this.entityRenderDispatcher.camera.getPosition().subtract(bolt.position());
            Matrix4f pose = poseStack.last().pose();
            VertexConsumer consumer = ElectricArcRendering.getBuffer();
            float width = large ? 0.4F : 0.22F;
            for (float[] layer : ElectricChargeRenderer.LAYERS) {
                ElectricChargeRenderer.drawArc(consumer, pose, toCamera, visible, width * layer[0], layer[1], layer[2], layer[3], layer[4]);
                for (int i = 0; i < branches.size(); i++) {
                    float grow = Mth.clamp((head - branchStarts.get(i)) * 6.0F, 0.0F, 1.0F);
                    List<Vec3> branch = this.slice(branches.get(i), grow);
                    if (branch.size() > 1) {
                        ElectricChargeRenderer.drawArc(consumer, pose, toCamera, branch, width * 0.45F * layer[0], layer[1], layer[2], layer[3], layer[4]);
                    }
                }
            }
        }
    }

    private List<Vec3> slice(List<Vec3> line, float to) {
        List<Vec3> result = new ArrayList<>();
        int last = line.size() - 1;
        float end = to * last;
        if (end > 0.0F) {
            result.add(line.get(0));
            for (int i = 1; i < end; i++) {
                result.add(line.get(i));
            }
            int i = Math.min(Mth.floor(end), last - 1);
            result.add(line.get(i).lerp(line.get(i + 1), end - i));
        }
        return result;
    }

    @Override
    public ResourceLocation getTextureLocation(LightningBolt bolt) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}