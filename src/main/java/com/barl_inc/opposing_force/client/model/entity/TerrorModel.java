package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.TerrorAnimations;
import com.barl_inc.opposing_force.entity.Terror;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class TerrorModel extends SinewEntityModel<Terror> {

    private final ModelPart root;
    private final ModelPart swim_control;
    private final ModelPart head;
    private final ModelPart tail1;
    private final ModelPart tail2;

    public TerrorModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.swim_control = this.root.getChild("swim_control");
        ModelPart body_main = this.swim_control.getChild("body_main");
        ModelPart body = body_main.getChild("body");
        this.head = body.getChild("head");
        this.tail1 = body.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Terror entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, TerrorAnimations.WALK, limbSwing, limbSwingAmount, partialTicks);
        this.animateWalkSmooth(entity.sprintAnimationState, TerrorAnimations.RUN, limbSwing, limbSwingAmount, partialTicks);
        this.animateWalkSmooth(entity.swimAnimationState, TerrorAnimations.SWIM, limbSwing, limbSwingAmount, partialTicks);
        this.animateWalkSmooth(entity.sprintSwimAnimationState, TerrorAnimations.SWIMFAST, limbSwing, limbSwingAmount, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, TerrorAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateIdleSmooth(entity.swimIdleAnimationState, TerrorAnimations.SWIM_IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.hideLegsAnimationState, TerrorAnimations.HIDE_LEGS_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.growLegsAnimationState, TerrorAnimations.GROW_LEGS, ageInTicks, partialTicks);
        this.animateSmooth(entity.cooldownAnimationState, TerrorAnimations.SAW_ATTACK_COOLDOWN_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.attackAnimationState, TerrorAnimations.SAW_ATTACK_BLEND, ageInTicks, partialTicks);
        this.animate(entity.attackAnimationState, TerrorAnimations.SAW_SPIN_BLEND, ageInTicks, partialTicks);
        if (entity.isInWater()) {
            this.rotatePart(this.swim_control, entity.getSwimPitch(partialTicks) * Mth.DEG_TO_RAD, 0.0F, entity.getRoll(partialTicks) * Mth.DEG_TO_RAD);
        }
        this.bendPart(this.head, entity, 0, partialTicks);
        this.bendPart(this.tail1, entity, 1, partialTicks);
        this.bendPart(this.tail2, entity, 2, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition swim_control = root.addOrReplaceChild("swim_control", CubeListBuilder.create(), PartPose.offset(0.0F, -19.0F, 0.0F));

        PartDefinition body_main = swim_control.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -1.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(65, 11).addBox(-4.5F, -12.0F, -5.5F, 9.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -4.0F, -13.0F, 11.0F, 6.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(36, 26).addBox(-5.5F, 2.0F, -13.0F, 11.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, -5.5F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(56, 0).addBox(4.0F, 0.0F, -4.0F, 2.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).mirror().addBox(-6.0F, 0.0F, -4.0F, 2.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(33, 50).addBox(-6.0F, 12.0F, -14.0F, 12.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(23, 24).addBox(-6.0F, 10.0F, -14.0F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(10, 26).addBox(3.0F, 10.0F, -14.0F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -4.0F));

        jaw.addOrReplaceChild("saw", CubeListBuilder.create().texOffs(40, 56).addBox(0.0F, -10.0F, -10.0F, 0.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(75, 44).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, -13.0F));

        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(29, 50).addBox(0.0F, 0.0F, -1.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -5.0F, -2.5F));

        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(29, 50).mirror().addBox(-8.0F, 0.0F, -1.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -5.0F, -2.5F));

        body.addOrReplaceChild("dorsal1", CubeListBuilder.create().texOffs(33, 33).addBox(0.0F, -6.0F, -1.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 1.5F));

        PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 44).addBox(-1.5F, -3.5F, -2.0F, 3.0F, 5.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.5F, 5.5F));

        tail1.addOrReplaceChild("dorsal2", CubeListBuilder.create().texOffs(0, 56).addBox(0.0F, -6.0F, -10.0F, 0.0F, 6.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 11.0F));

        tail1.addOrReplaceChild("anal", CubeListBuilder.create().texOffs(0, 62).addBox(0.0F, 0.0F, -10.0F, 0.0F, 6.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 11.0F));

        tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -5.5F, -3.0F, 0.0F, 9.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 22.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.5F));

        PartDefinition left_leg1 = leg_control.addOrReplaceChild("left_leg1", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition left_leg2 = left_leg1.addOrReplaceChild("left_leg2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 4.0F, 3.5F));

        left_leg2.addOrReplaceChild("left_leg3", CubeListBuilder.create().texOffs(0, 58).addBox(-3.0F, 0.0F, -4.5F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 30).addBox(1.0F, 0.0F, -7.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(1.0F, 0.0F, -9.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 30).addBox(-3.0F, 0.0F, -7.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-3.0F, 0.0F, -9.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.5F));

        PartDefinition right_leg1 = leg_control.addOrReplaceChild("right_leg1", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-3.0F, -2.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, 0.0F, 0.0F));

        PartDefinition right_leg2 = right_leg1.addOrReplaceChild("right_leg2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -1.0F, -1.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, 4.0F, 3.5F));

        right_leg2.addOrReplaceChild("right_leg3", CubeListBuilder.create().texOffs(0, 58).mirror().addBox(-3.0F, 0.0F, -4.5F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 30).mirror().addBox(-3.0F, 0.0F, -7.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 26).mirror().addBox(-3.0F, 0.0F, -9.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 30).mirror().addBox(1.0F, 0.0F, -7.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 26).mirror().addBox(1.0F, 0.0F, -9.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 8.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
