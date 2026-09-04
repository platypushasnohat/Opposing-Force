package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animations.DicerAnimations;
import com.barl_inc.opposing_force.entity.Dicer;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class DicerModel extends SinewEntityModel<Dicer> {

    private final ModelPart root;
    private final ModelPart head;

    public DicerModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root.getChild("root");
        ModelPart spin_control = this.root.getChild("spin_control");
        ModelPart body_main = spin_control.getChild("body_main");
        ModelPart hips = body_main.getChild("hips");
        ModelPart waist = hips.getChild("waist");
        this.head = waist.getChild("head");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Dicer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, DicerAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F, partialTicks);
        this.animateWalkSmooth(entity.runAnimationState, DicerAnimations.RUN, limbSwing, limbSwingAmount, 1.0F, 2.0F, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, DicerAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.slash1AnimationState, DicerAnimations.SLASH1, ageInTicks, partialTicks);
        this.animateSmooth(entity.slash2AnimationState, DicerAnimations.SLASH2, ageInTicks, partialTicks);
        this.animateSmooth(entity.crossSlashAnimationState, DicerAnimations.CROSSSLASH, ageInTicks, partialTicks);
        this.animateSmooth(entity.laserAnimationState, DicerAnimations.LASER, ageInTicks, partialTicks);
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition spin_control = root.addOrReplaceChild("spin_control", CubeListBuilder.create(), PartPose.offset(0.0F, -21.0F, 0.0F));

        PartDefinition body_main = spin_control.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hips = body_main.addOrReplaceChild("hips", CubeListBuilder.create().texOffs(20, 52).addBox(-4.0F, -2.0F, -2.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition waist = hips.addOrReplaceChild("waist", CubeListBuilder.create().texOffs(46, 52).addBox(-2.0F, -12.0F, -1.5F, 4.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 1).addBox(0.0F, -10.0F, 1.5F, 0.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        waist.addOrReplaceChild("sail_r1", CubeListBuilder.create().texOffs(40, -7).mirror().addBox(0.0F, -2.0F, 0.0F, 0.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -8.0F, 1.5F, 0.0F, -0.4363F, 0.0F));

        waist.addOrReplaceChild("sail_r2", CubeListBuilder.create().texOffs(40, -7).addBox(0.0F, -2.0F, 0.0F, 0.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.0F, 1.5F, 0.0F, 0.4363F, 0.0F));

        PartDefinition head = waist.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 18).addBox(-5.0F, -9.0F, -3.5F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-2.0F, -13.0F, -4.5F, 2.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(28, 14).addBox(-2.0F, 1.0F, -5.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 8).addBox(-2.0F, -2.0F, -7.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(34, 10).addBox(-2.0F, -5.0F, -7.5F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -11.0F, -0.5F));

        head.addOrReplaceChild("visor", CubeListBuilder.create().texOffs(21, 43).addBox(-5.0F, -1.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -6.0F, -1.0F));

        PartDefinition chest = waist.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(28, 34).addBox(-4.0F, -4.0F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition left_arm_joint = chest.addOrReplaceChild("left_arm_joint", CubeListBuilder.create(), PartPose.offset(4.0F, -3.0F, 0.0F));

        PartDefinition left_arm = left_arm_joint.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(12, 47).addBox(0.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 18).addBox(-1.0F, -1.0F, 0.0F, 9.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        left_arm.addOrReplaceChild("left_finger1", CubeListBuilder.create().texOffs(14, -10).addBox(0.0F, 0.0F, -2.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 23.0F, 0.0F));

        left_arm.addOrReplaceChild("left_finger2", CubeListBuilder.create().texOffs(14, -10).addBox(0.0F, 0.0F, -2.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 0.0F));

        PartDefinition right_arm_joint = chest.addOrReplaceChild("right_arm_joint", CubeListBuilder.create(), PartPose.offset(-4.0F, -3.0F, 0.0F));

        PartDefinition right_arm = right_arm_joint.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(12, 47).mirror().addBox(-2.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 18).mirror().addBox(-8.0F, -1.0F, 0.0F, 9.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        right_arm.addOrReplaceChild("right_finger1", CubeListBuilder.create().texOffs(14, -10).mirror().addBox(0.0F, 0.0F, -2.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 23.0F, 0.0F));

        right_arm.addOrReplaceChild("right_finger2", CubeListBuilder.create().texOffs(14, -10).mirror().addBox(0.0F, 0.0F, -2.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 23.0F, 0.0F));

        PartDefinition tail1 = waist.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(-7, 30).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.5F));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(-9, 21).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(-9, 12).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));

        tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(-12, 0).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        leg_control.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 47).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 21.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 0).addBox(1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, 0.0F));

        leg_control.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 47).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 0).mirror().addBox(-4.5F, 0.0F, -1.5F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
