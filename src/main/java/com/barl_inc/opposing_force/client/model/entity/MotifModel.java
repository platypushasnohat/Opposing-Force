package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.MotifAnimations;
import com.barl_inc.opposing_force.entity.Motif;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MotifModel extends SinewEntityModel<Motif> {

	private final ModelPart root;
	private final ModelPart head;

	public MotifModel(ModelPart root) {
		this.root = root.getChild("root");
		ModelPart bodyMain = this.root.getChild("body_main");
		ModelPart body = bodyMain.getChild("body");
		this.head = body.getChild("head");
	}

    @Override
    public ModelPart root() {
        return this.root;
    }

	@Override
	protected void setupAnimations(Motif entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
		this.animateWalkSmooth(entity.walkAnimationState, MotifAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F, partialTicks);
		this.animateIdleSmooth(entity.idleAnimationState, MotifAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
		this.animateSmooth(entity.attackAnimationState, MotifAnimations.SHOOT_BLEND, ageInTicks, partialTicks);
		this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot += headPitch * Mth.DEG_TO_RAD;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 28).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 11.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(33, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 0).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create(), PartPose.offset(6.0F, -12.0F, -0.5F));

        arm_left.addOrReplaceChild("arm_left_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -1.0F, -5.0F, 11.0F, 17.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(70, 24).addBox(5.0F, 1.0F, 0.0F, 3.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 32).addBox(-3.0F, 16.0F, -2.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 13).addBox(-5.0F, 17.0F, -4.0F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, 0.0F));

        PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create(), PartPose.offset(-6.0F, -12.0F, -0.5F));

        arm_right.addOrReplaceChild("arm_right_rotation", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -1.0F, -5.0F, 11.0F, 17.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(70, 24).mirror().addBox(-8.0F, 1.0F, 0.0F, 3.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(30, 32).mirror().addBox(-2.0F, 16.0F, -2.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(44, 13).mirror().addBox(-4.0F, 17.0F, -4.0F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(40, 28).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        leg_control.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(84, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, 0.0F));

        leg_control.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(84, 0).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
	}
}