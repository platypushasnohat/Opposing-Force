package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.projectile.ManiaBrain;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class ManiaBrainRenderer extends EntityRenderer<ManiaBrain> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/entity/projectiles/mania_brain.png");

    private final ModelPart brain;

    public ManiaBrainRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.brain = createLayer().bakeRoot().getChild("brain");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        meshdefinition.getRoot().addOrReplaceChild("brain", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -9.0F, -10.0F, 24.0F, 18.0F, 20.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void render(ManiaBrain entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float age = entity.tickCount + partialTicks;
        int color = entity.getShade().color & 0xFFFFFF;
        RandomSource random = RandomSource.create(entity.getId() * 31L + (long) (age * 3.0F));
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));
        Vec3 motion = entity.getDeltaMovement();
        float shrink = Mth.clamp((ManiaBrain.LIFETIME - age) / 2.5F, 0.0F, 1.0F);
        RandomSource jumpRandom = RandomSource.create(entity.getId() * 17L + (long) (age * 3.0F));
        poseStack.pushPose();
        poseStack.translate(entity.getX() - Mth.lerp(partialTicks, entity.xo, entity.getX()), entity.getY() - Mth.lerp(partialTicks, entity.yo, entity.getY()) + entity.getBbHeight() * 0.5F, entity.getZ() - Mth.lerp(partialTicks, entity.zo, entity.getZ()));
        poseStack.mulPose(Axis.YP.rotation((float) Mth.atan2(motion.x, motion.z)));
        poseStack.mulPose(Axis.XP.rotation((float) -Mth.atan2(motion.y, motion.horizontalDistance())));
        for (int i = 0; i < 3; i++) {
            float phase = 0.5F + 0.5F * Mth.sin(age * 1.7F + i * 2.1F + entity.getId());
            float alpha = 0.35F * (0.4F + 0.6F * phase);
            poseStack.pushPose();
            poseStack.translate((jumpRandom.nextFloat() - 0.5F) * 2.6F, (jumpRandom.nextFloat() - 0.5F) * 2.1F, (jumpRandom.nextFloat() - 0.5F) * 2.1F);
            if (i > 0) {
                alpha = (0.08F + random.nextFloat() * 0.2F) * phase;
                poseStack.translate((random.nextFloat() - 0.5F) * 0.6F, (random.nextFloat() - 0.5F) * 0.6F, (random.nextFloat() - 0.5F) * 3.0F);
                poseStack.scale(0.6F + random.nextFloat() * 1.0F, 0.6F + random.nextFloat() * 0.6F, 1.0F);
            }
            poseStack.scale(0.6F * shrink, 0.6F * shrink, 0.6F * shrink);
            this.brain.render(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, (int) (Mth.clamp(alpha * shrink, 0.0F, 1.0F) * 255.0F) << 24 | color);
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ManiaBrain entity) {
        return TEXTURE;
    }
}
