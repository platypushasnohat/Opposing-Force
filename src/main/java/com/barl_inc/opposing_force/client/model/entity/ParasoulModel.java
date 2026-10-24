package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.ParasoulAnimations;
import com.barl_inc.opposing_force.entity.Parasoul;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class ParasoulModel extends SinewEntityModel<Parasoul> {

    private final ModelPart root;
    public float alpha = 1.0F;

    public ParasoulModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Parasoul entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        float walkSpeed = entity.getWalkSpeed(partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, ParasoulAnimations.IDLE, ageInTicks, partialTicks, walkSpeed);
        this.animateSmooth(entity.mockAnimationState, ParasoulAnimations.MOCKING, ageInTicks, partialTicks);
        this.animateWalkSmooth(entity.walkAnimationState, ParasoulAnimations.MOVING, entity.getWalkPhase(partialTicks), walkSpeed, 1.5F, 2.5F, partialTicks);
        this.animateSmooth(entity.leapAnimationState, ParasoulAnimations.LEAP, ageInTicks, partialTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        int alpha = (int) (((color >>> 24) & 0xFF) * this.alpha);
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, (alpha << 24) | (color & 0xFFFFFF));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition body = bodyMain.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -22.0F, 0.0F));

        PartDefinition upperBody = body.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(46, 45).addBox(-2.5F, -26.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 51).addBox(-1.5F, -29.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(54, 21).addBox(-0.5F, -28.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.5F, -23.0F, -5.5F, 11.0F, 28.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(-5.5F, 0.0F, -5.5F, 11.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        upperBody.addOrReplaceChild("pupil", CubeListBuilder.create().texOffs(38, 41).addBox(-1.5F, -1.5F, -0.01F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.5F, -5.5F));

        PartDefinition armLeft = upperBody.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(0, 51).addBox(-0.5F, 0.0F, -5.0F, 1.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, -2.5F));

        armLeft.addOrReplaceChild("hand_left", CubeListBuilder.create().texOffs(12, 51).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -5.0F));

        PartDefinition armRight = upperBody.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(0, 51).mirror().addBox(-0.5F, 0.0F, -5.0F, 1.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 0.0F, -2.5F));

        armRight.addOrReplaceChild("hand_right", CubeListBuilder.create().texOffs(12, 51).mirror().addBox(-1.5F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 10.0F, -5.0F));

        PartDefinition tongueBase = upperBody.addOrReplaceChild("tongue_base", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -5.5F));

        PartDefinition tongueMid = tongueBase.addOrReplaceChild("tongue_mid", CubeListBuilder.create().texOffs(45, 27).addBox(-9.0F, 0.0F, -9.0F, 11.0F, 0.0F, 9.0F, new CubeDeformation(0.0025F)), PartPose.ZERO);

        tongueMid.addOrReplaceChild("tongue_tip", CubeListBuilder.create().texOffs(45, 36).addBox(-9.0F, 0.0F, -9.0F, 11.0F, 0.0F, 9.0F, new CubeDeformation(0.0025F)), PartPose.offset(0.0F, 0.0F, -9.0F));

        PartDefinition leg = body.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(46, 21).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        leg.addOrReplaceChild("foot", CubeListBuilder.create().texOffs(44, 11).addBox(-3.0F, 0.0F, -5.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(36, 53).addBox(-2.0F, 2.0F, -4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-2.0F, 2.0F, 0.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
