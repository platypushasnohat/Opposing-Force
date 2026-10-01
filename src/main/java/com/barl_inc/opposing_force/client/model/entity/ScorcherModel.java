package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.ScorcherAnimations;
import com.barl_inc.opposing_force.entity.Scorcher;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class ScorcherModel extends SinewEntityModel<Scorcher> {

    private final ModelPart root;

    public ScorcherModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Scorcher entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, ScorcherAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, ScorcherAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.fireAnimationState, ScorcherAnimations.FIRE, ageInTicks, partialTicks);
        this.animateSmooth(entity.attackAnimationState, ScorcherAnimations.ATTACK_BLEND, ageInTicks, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, -4.0F));

        PartDefinition body_control = root.addOrReplaceChild("body_control", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = body_control.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, -2.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(-3.0F, -14.0F, -11.0F, 6.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(-3.0F, -9.0F, -11.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 50).addBox(-7.0F, -14.0F, 12.0F, 14.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 41).addBox(-0.5F, -3.0F, -8.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(30, 28).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.0F, -2.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-3.0F, -2.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 5).addBox(-0.5F, -5.0F, -8.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 5).addBox(-0.5F, -5.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_legs = body_control.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(1.5F, -1.0F, 2.75F));

        PartDefinition left_leg_bone_1 = left_legs.addOrReplaceChild("left_leg_bone_1", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.5F, -3.0F, 0.0F, 0.4363F, 0.4363F));

        left_leg_bone_1.addOrReplaceChild("left_leg_1", CubeListBuilder.create().texOffs(0, 42).addBox(6.0F, 1.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -1.0F, 0.0F));

        PartDefinition left_leg_bone_2 = left_legs.addOrReplaceChild("left_leg_bone_2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.5F, -1.0F, 0.0F, 0.0F, 0.4363F));

        left_leg_bone_2.addOrReplaceChild("left_leg_2", CubeListBuilder.create().texOffs(0, 42).addBox(6.0F, 1.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -1.0F, 0.0F));

        PartDefinition left_leg_bone_3 = left_legs.addOrReplaceChild("left_leg_bone_3", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.5F, 1.0F, 0.0F, -0.8727F, 0.4363F));

        left_leg_bone_3.addOrReplaceChild("left_leg_3", CubeListBuilder.create().texOffs(0, 42).addBox(-0.5F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.0F, 0.0F));

        PartDefinition right_legs = body_control.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(-1.5F, -1.0F, 2.75F));

        PartDefinition right_leg_bone_1 = right_legs.addOrReplaceChild("right_leg_bone_1", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.5F, -3.0F, 0.0F, -0.4363F, -0.4363F));

        right_leg_bone_1.addOrReplaceChild("right_leg_1", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-11.0F, 1.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -1.0F, 0.0F));

        PartDefinition right_leg_bone_2 = right_legs.addOrReplaceChild("right_leg_bone_2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.5F, -1.0F, 0.0F, 0.0F, -0.4363F));

        right_leg_bone_2.addOrReplaceChild("right_leg_2", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-11.0F, 1.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -1.0F, 0.0F));

        PartDefinition right_leg_bone_3 = right_legs.addOrReplaceChild("right_leg_bone_3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.5F, 1.0F, 0.0F, 0.8727F, -0.4363F));

        right_leg_bone_3.addOrReplaceChild("right_leg_3", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-11.0F, 1.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
