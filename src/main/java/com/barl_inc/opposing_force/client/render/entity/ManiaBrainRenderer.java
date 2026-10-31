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

public class ManiaBrainRenderer extends EntityRenderer<ManiaBrain> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/entity/projectiles/mania_brain.png");
    private static final ResourceLocation ELITE_TEXTURE = OpposingForce.location("textures/entity/projectiles/mania_brain_elite.png");
    private static final ResourceLocation GLITCH_TEXTURE = OpposingForce.location("textures/entity/projectiles/mania_brain_glitch.png");
    private static final int[] GLITCH_COLORS = {0x00FFFF, 0xFFFF00, 0x101010, 0xFF00FF, 0x2BFF4A, 0x8A2BE2, 0x2B5BFF};

    private final ModelPart brain;

    public ManiaBrainRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.brain = createLayer().bakeRoot().getChild("brain");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        meshdefinition.getRoot().addOrReplaceChild("brain", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -7.0F, -4.0F, 5.0F, 7.0F, 7.0F)
                .texOffs(0, 14).addBox(-2.5F, -6.5F, -3.5F, 6.0F, 6.0F, 6.0F), PartPose.offset(-0.5F, 3.5F, 0.5F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(ManiaBrain entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        if (entity.snapTicks > 0) {
            poseStack.translate(entity.getX() - Mth.lerp(partialTicks, entity.xo, entity.getX()), entity.getY() - Mth.lerp(partialTicks, entity.yo, entity.getY()), entity.getZ() - Mth.lerp(partialTicks, entity.zo, entity.getZ()));
        }
        poseStack.translate(0.0F, entity.getBbHeight() * 0.5F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, entity.prevFacingYaw, entity.facingYaw)));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entity.prevFacingPitch, entity.facingPitch)));
        poseStack.scale(-entity.getBrainScale(), -entity.getBrainScale(), entity.getBrainScale());
        this.brain.render(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(entity.isElite() ? ELITE_TEXTURE : TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        if (entity.glitchTicks > 0) {
            RandomSource random = RandomSource.create(entity.glitchSeed);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(GLITCH_TEXTURE));
            int colors = entity.isElite() ? GLITCH_COLORS.length : 4;
            int ghosts = 2 + random.nextInt(2);
            for (int i = 0; i < ghosts; i++) {
                float scale = 0.8F + random.nextFloat() * 0.45F;
                poseStack.pushPose();
                poseStack.translate((random.nextFloat() - 0.5F) * 0.25F, (random.nextFloat() - 0.5F) * 0.25F, (random.nextFloat() - 0.5F) * 0.25F);
                poseStack.scale(scale, scale, scale);
                this.brain.render(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0x99000000 | GLITCH_COLORS[random.nextInt(colors)]);
                poseStack.popPose();
            }
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ManiaBrain entity) {
        return TEXTURE;
    }
}
