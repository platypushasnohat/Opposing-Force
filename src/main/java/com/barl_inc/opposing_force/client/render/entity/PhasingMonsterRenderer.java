package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.PhasingMonsterModel;
import com.barl_inc.opposing_force.client.render.entity.layer.PhasingGlowLayer;
import com.barl_inc.opposing_force.entity.base.PhasingMonster;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class PhasingMonsterRenderer<T extends PhasingMonster, M extends PhasingMonsterModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation texture;
    private final float tiltPivot;

    public PhasingMonsterRenderer(EntityRendererProvider.Context context, M model, String name, float tiltPivot) {
        super(context, model, 0.0F);
        this.texture = OpposingForce.location("textures/entity/" + name + "/" + name + ".png");
        this.tiltPivot = tiltPivot;
        this.addLayer(new PhasingGlowLayer<>(this, OpposingForce.location("textures/entity/" + name + "/" + name + "_glow.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.texture;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model.alpha = entity.getFade(partialTicks);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void setupRotations(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTicks, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTicks, scale);
        poseStack.translate(0.0F, this.tiltPivot, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getTilt(partialTicks)));
        poseStack.translate(0.0F, -this.tiltPivot, 0.0F);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(T entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return this.model.alpha > 0.0F ? super.getRenderType(entity, bodyVisible, translucent, glowing) : null;
    }
}
