package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.FurballAnimations;
import com.barl_inc.opposing_force.entity.Furball;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class FurballModel extends SinewEntityModel<Furball> {

    private final ModelPart root;
    private final ModelPart head;

    public FurballModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root.getChild("root");
        ModelPart body_main = this.root.getChild("body_main");
        ModelPart body = body_main.getChild("body");
        this.head = body.getChild("head");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Furball entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, FurballAnimations.WALK, limbSwing, limbSwingAmount, partialTicks);
        this.animateWalkSmooth(entity.sprintAnimationState, FurballAnimations.RUN, limbSwing, limbSwingAmount, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, FurballAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.swimAnimationState, FurballAnimations.SWIM, ageInTicks, partialTicks);
        this.animateSmooth(entity.attackAnimationState, FurballAnimations.ATTACK_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.jumpAnimationState, FurballAnimations.JUMP, ageInTicks, partialTicks);
        this.head.yRot += Math.clamp(netHeadYaw * Mth.DEG_TO_RAD, Mth.PI / -4.0F, Mth.PI / 4.0F);
        this.head.xRot += Math.clamp(headPitch * Mth.DEG_TO_RAD, Mth.PI / -4.0F, Mth.PI / 4.0F);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -11.0F, -6.0F, 11.0F, 11.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(24, 11).addBox(0.0F, -15.0F, -8.0F, 0.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 42).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(2.0F, -6.0F, -2.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).mirror().addBox(-4.0F, -6.0F, -2.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -4.0F, -6.0F));

        head.addOrReplaceChild("top_jaw", CubeListBuilder.create().texOffs(32, 48).addBox(2.01F, -1.0F, -9.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(32, 48).addBox(-2.01F, -1.0F, -9.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 42).addBox(-2.0F, -5.0F, -9.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(15, 47).addBox(-2.0F, -2.0F, -9.0F, 4.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -4.0F));

        head.addOrReplaceChild("lower_jaw", CubeListBuilder.create().texOffs(32, 46).addBox(1.975F, -1.0F, -5.9F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(32, 46).addBox(-1.975F, -1.0F, -5.9F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(-2.0F, 0.0F, -5.95F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -4.0F));

        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 26).addBox(3.0F, -3.0F, 12.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-3.0F, -3.0F, 12.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-2, 26).addBox(-3.0F, -3.0F, 12.0F, 6.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-2, 24).addBox(-3.0F, 3.0F, 12.0F, 6.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 7.0F));

        PartDefinition arm_control = body_main.addOrReplaceChild("arm_control", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -4.0F));

        arm_control.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

        arm_control.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 0.0F, 0.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 5.0F));

        leg_control.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

        leg_control.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
