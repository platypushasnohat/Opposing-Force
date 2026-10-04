package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.TarantulaAnimations;
import com.barl_inc.opposing_force.entity.Tarantula;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class TarantulaModel extends SinewEntityModel<Tarantula> {

    private final ModelPart root;

    public TarantulaModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Tarantula entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, TarantulaAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F, partialTicks);
        this.animateWalkSmooth(entity.sprintAnimationState, TarantulaAnimations.RUN, limbSwing, limbSwingAmount, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, TarantulaAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.sitAnimationState, TarantulaAnimations.SIT, ageInTicks, partialTicks);
        this.animateSmooth(entity.jumpAnimationState, TarantulaAnimations.JUMP, ageInTicks, partialTicks);
        this.animateSmooth(entity.attack1AnimationState, TarantulaAnimations.SWIPE1, ageInTicks, partialTicks);
        this.animateSmooth(entity.attack2AnimationState, TarantulaAnimations.SWIPE2, ageInTicks, partialTicks);
        this.animateSmooth(entity.slamAnimationState, TarantulaAnimations.SLAM, ageInTicks, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, 7.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(30, 79).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 52).addBox(-9.0F, -6.5F, -16.0F, 18.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(4.0F, -7.5F, -15.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(1.0F, -7.5F, -15.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-3.0F, -7.5F, -15.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-6.0F, -7.5F, -15.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 52).addBox(-9.0F, -6.5F, -16.0F, 18.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -4.0F));

        head.addOrReplaceChild("brows_r1", CubeListBuilder.create().texOffs(40, 104).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, -12.5F, 0.3927F, 0.0F, 0.0F));

        head.addOrReplaceChild("mandible_left", CubeListBuilder.create().texOffs(30, 92).addBox(0.0F, 6.0F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(67, 95).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 0.5F, -16.0F, -0.5236F, 0.0F, 0.0F));

        head.addOrReplaceChild("mandible_right", CubeListBuilder.create().texOffs(30, 92).mirror().addBox(0.0F, 6.0F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(67, 95).mirror().addBox(-2.5F, -4.0F, -5.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.5F, -16.0F, -0.5236F, 0.0F, 0.0F));

        body.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -11.0F, 0.0F, 32.0F, 18.0F, 18.0F, new CubeDeformation(0.01F))
                .texOffs(38, 110).addBox(-18.0F, -11.0F, 2.0F, 36.0F, 0.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(38, 110).addBox(-18.0F, 7.0F, 2.0F, 36.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, -13.5F));

        PartDefinition leg_cluster_left = leg_control.addOrReplaceChild("leg_cluster_left", CubeListBuilder.create(), PartPose.offset(9.0F, 0.0F, 0.0F));

        PartDefinition leg_left1_joint = leg_cluster_left.addOrReplaceChild("leg_left1_joint", CubeListBuilder.create(), PartPose.offset(-1.0F, 1.0F, -4.0F));

        PartDefinition leg_left1 = leg_left1_joint.addOrReplaceChild("leg_left1", CubeListBuilder.create().texOffs(72, 86).addBox(0.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.7854F, -0.6981F));

        leg_left1.addOrReplaceChild("foot_left1", CubeListBuilder.create().texOffs(68, 52).addBox(0.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F))
                .texOffs(0, 79).addBox(5.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(40, 97).addBox(4.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -1.5F, 0.0F));

        PartDefinition leg_left2_joint = leg_cluster_left.addOrReplaceChild("leg_left2_joint", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -1.0F));

        PartDefinition leg_left2 = leg_left2_joint.addOrReplaceChild("leg_left2", CubeListBuilder.create().texOffs(72, 86).addBox(0.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.3491F, -0.5236F));

        leg_left2.addOrReplaceChild("foot_left2", CubeListBuilder.create().texOffs(68, 52).addBox(0.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F))
                .texOffs(0, 79).addBox(5.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(40, 97).addBox(4.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -1.5F, 0.0F));

        PartDefinition leg_left3_joint = leg_cluster_left.addOrReplaceChild("leg_left3_joint", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition leg_left3 = leg_left3_joint.addOrReplaceChild("leg_left3", CubeListBuilder.create().texOffs(72, 86).addBox(0.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, -0.1745F, -0.5236F));

        leg_left3.addOrReplaceChild("foot_left3", CubeListBuilder.create().texOffs(68, 52).addBox(0.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F))
                .texOffs(0, 79).addBox(5.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(40, 97).addBox(4.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -1.5F, 0.0F));

        PartDefinition leg_left4_joint = leg_cluster_left.addOrReplaceChild("leg_left4_joint", CubeListBuilder.create(), PartPose.offset(-2.0F, 1.0F, 6.0F));

        PartDefinition leg_left4 = leg_left4_joint.addOrReplaceChild("leg_left4", CubeListBuilder.create().texOffs(72, 86).addBox(0.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, -0.5236F, -0.6109F));

        leg_left4.addOrReplaceChild("foot_left4", CubeListBuilder.create().texOffs(68, 52).addBox(0.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F))
                .texOffs(0, 79).addBox(5.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(40, 97).addBox(4.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -1.5F, 0.0F));

        PartDefinition leg_cluster_right = leg_control.addOrReplaceChild("leg_cluster_right", CubeListBuilder.create(), PartPose.offset(-9.0F, 0.0F, 0.0F));

        PartDefinition leg_right1_joint = leg_cluster_right.addOrReplaceChild("leg_right1_joint", CubeListBuilder.create(), PartPose.offset(1.0F, 1.0F, -4.0F));

        PartDefinition leg_right1 = leg_right1_joint.addOrReplaceChild("leg_right1", CubeListBuilder.create().texOffs(72, 86).mirror().addBox(-12.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, -0.7854F, 0.6981F));

        leg_right1.addOrReplaceChild("foot_right1", CubeListBuilder.create().texOffs(68, 52).mirror().addBox(-10.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(0, 79).mirror().addBox(-11.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 97).mirror().addBox(-7.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-12.0F, -1.5F, 0.0F));

        PartDefinition leg_right2_joint = leg_cluster_right.addOrReplaceChild("leg_right2_joint", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -1.0F));

        PartDefinition leg_right2 = leg_right2_joint.addOrReplaceChild("leg_right2", CubeListBuilder.create().texOffs(72, 86).mirror().addBox(-12.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, -0.3491F, 0.5236F));

        leg_right2.addOrReplaceChild("foot_right2", CubeListBuilder.create().texOffs(68, 52).mirror().addBox(-10.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(0, 79).mirror().addBox(-11.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 97).mirror().addBox(-7.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-12.0F, -1.5F, 0.0F));

        PartDefinition leg_right3_joint = leg_cluster_right.addOrReplaceChild("leg_right3_joint", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition leg_right3 = leg_right3_joint.addOrReplaceChild("leg_right3", CubeListBuilder.create().texOffs(72, 86).mirror().addBox(-12.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.1745F, 0.5236F));

        leg_right3.addOrReplaceChild("foot_right3", CubeListBuilder.create().texOffs(68, 52).mirror().addBox(-10.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(0, 79).mirror().addBox(-11.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 97).mirror().addBox(-7.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-12.0F, -1.5F, 0.0F));

        PartDefinition leg_right4_joint = leg_cluster_right.addOrReplaceChild("leg_right4_joint", CubeListBuilder.create(), PartPose.offset(2.0F, 1.0F, 6.0F));

        PartDefinition leg_right4 = leg_right4_joint.addOrReplaceChild("leg_right4", CubeListBuilder.create().texOffs(72, 86).mirror().addBox(-12.0F, -1.5F, -1.5F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.5236F, 0.6109F));

        leg_right4.addOrReplaceChild("foot_right4", CubeListBuilder.create().texOffs(68, 52).mirror().addBox(-10.0F, 0.0F, -4.5F, 10.0F, 25.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(0, 79).mirror().addBox(-11.0F, -3.0F, -4.5F, 6.0F, 27.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 97).mirror().addBox(-7.0F, 25.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-12.0F, -1.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
