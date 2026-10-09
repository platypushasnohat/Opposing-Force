package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.MushyAnimations;
import com.barl_inc.opposing_force.entity.Mushy;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MushyModel extends SinewEntityModel<Mushy> {

	private final ModelPart root;
	private final ModelPart body_main;

	public MushyModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body_main = this.root.getChild("body_main");
	}

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Mushy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, MushyAnimations.MOVE, limbSwing, limbSwingAmount, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, MushyAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.attackAnimationState, MushyAnimations.HIT, ageInTicks, partialTicks);
        this.animateSmooth(entity.danceAnimationState, MushyAnimations.DANCE, ageInTicks, partialTicks);
        this.animateSmooth(entity.launchAnimationState, MushyAnimations.LAUNCH_START, ageInTicks, partialTicks);
        this.animateSmooth(entity.spinAnimationState, MushyAnimations.LAUNCH_LOOP, ageInTicks, partialTicks);
        this.body_main.yRot += entity.getSpinAngle(partialTicks) * Mth.DEG_TO_RAD;
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

        body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(4.5F, -7.0F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.5F, -13.0F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(16, 16).addBox(1.5F, -4.0F, -3.5F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).mirror().addBox(-5.5F, -7.0F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 16).mirror().addBox(-5.5F, -4.0F, -3.5F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        leg_control.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(4, 1).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-0.5F, 2.0F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(-0.5F, 1.0F, -2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        leg_control.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(4, 1).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 4).mirror().addBox(-0.5F, 2.0F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 1).mirror().addBox(-0.5F, 1.0F, -2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
	}
}