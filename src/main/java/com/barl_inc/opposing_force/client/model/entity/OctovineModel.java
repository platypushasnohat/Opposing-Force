package com.barl_inc.opposing_force.client.model.entity;// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.barl_inc.opposing_force.client.model.entity.animation.FurballAnimations;
import com.barl_inc.opposing_force.client.model.entity.animation.OctovineAnimations;
import com.barl_inc.opposing_force.entity.Furball;
import com.barl_inc.opposing_force.entity.Octovine;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class OctovineModel extends SinewEntityModel<Octovine> {

	private final ModelPart root;
	private final ModelPart body_main;
	private final ModelPart upper_body;
	private final ModelPart tentacle_top_left;
	private final ModelPart tentacle_top_middle;
	private final ModelPart tentacle_top_right;
	private final ModelPart tentacle_bottom_left;
	private final ModelPart tentacle_bottom_right;
	private final ModelPart tail;
	private final ModelPart leg_left;
	private final ModelPart knee_left;
	private final ModelPart foot_left;
	private final ModelPart leg_right;
	private final ModelPart knee_right;
	private final ModelPart foot_right;

	public OctovineModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body_main = this.root.getChild("body_main");
		this.upper_body = this.body_main.getChild("upper_body");
		this.tentacle_top_left = this.upper_body.getChild("tentacle_top_left");
		this.tentacle_top_middle = this.upper_body.getChild("tentacle_top_middle");
		this.tentacle_top_right = this.upper_body.getChild("tentacle_top_right");
		this.tentacle_bottom_left = this.upper_body.getChild("tentacle_bottom_left");
		this.tentacle_bottom_right = this.upper_body.getChild("tentacle_bottom_right");
		this.tail = this.upper_body.getChild("tail");
		this.leg_left = this.body_main.getChild("leg_left");
		this.knee_left = this.leg_left.getChild("knee_left");
		this.foot_left = this.knee_left.getChild("foot_left");
		this.leg_right = this.body_main.getChild("leg_right");
		this.knee_right = this.leg_right.getChild("knee_right");
		this.foot_right = this.knee_right.getChild("foot_right");
	}

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Octovine entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, OctovineAnimations.MOVING, limbSwing, limbSwingAmount, 3.0F, 2.5F, partialTicks);
        this.animateWalkSmooth(entity.sprintAnimationState, OctovineAnimations.RUN_AGGRO, limbSwing, limbSwingAmount, 2.0F, 2.5F, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, OctovineAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.swimAnimationState, OctovineAnimations.SWIM, ageInTicks, partialTicks);
        this.animateSmooth(entity.biteAnimationState, OctovineAnimations.BITE_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.swingAnimationState, OctovineAnimations.SWING_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.spitAnimationState, OctovineAnimations.SPIT_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.eatAnimationState, OctovineAnimations.EATING_BLEND, ageInTicks, partialTicks);
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition upper_body = body_main.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -17.0F, -14.0F, 15.0F, 17.0F, 21.0F, new CubeDeformation(0.0F))
		.texOffs(38, 88).addBox(-7.5F, -8.0F, -10.0F, 15.0F, 6.0F, 10.0F, new CubeDeformation(0.2F))
		.texOffs(1, 38).addBox(-4.5F, -21.0F, -13.0F, 9.0F, 12.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 4.0F));

		PartDefinition tentacle_top_left = upper_body.addOrReplaceChild("tentacle_top_left", CubeListBuilder.create().texOffs(72, 26).addBox(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(28, 84).addBox(-1.5F, -3.5F, -11.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(70, 76).addBox(-1.5F, -5.5F, -11.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(66, 55).addBox(-1.0F, -6.5F, -12.0F, 2.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -6.5F, -14.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition tentacle_top_middle = upper_body.addOrReplaceChild("tentacle_top_middle", CubeListBuilder.create().texOffs(72, 26).addBox(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(28, 84).addBox(-1.5F, -3.5F, -11.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(70, 76).addBox(-1.5F, -5.5F, -11.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(66, 55).addBox(-1.0F, -6.5F, -12.0F, 2.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, -14.0F));

		PartDefinition tentacle_top_right = upper_body.addOrReplaceChild("tentacle_top_right", CubeListBuilder.create().texOffs(72, 26).mirror().addBox(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(28, 84).mirror().addBox(-1.5F, -3.5F, -11.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(70, 76).mirror().addBox(-1.5F, -5.5F, -11.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(66, 55).mirror().addBox(-1.0F, -6.5F, -12.0F, 2.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -6.5F, -14.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition tentacle_bottom_left = upper_body.addOrReplaceChild("tentacle_bottom_left", CubeListBuilder.create().texOffs(72, 26).addBox(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(38, 84).addBox(-1.5F, -1.5F, -11.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(70, 82).addBox(-1.5F, 3.5F, -11.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 72).addBox(-1.0F, -2.5F, -12.0F, 2.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.5F, -14.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition tentacle_bottom_right = upper_body.addOrReplaceChild("tentacle_bottom_right", CubeListBuilder.create().texOffs(72, 26).mirror().addBox(-1.5F, -1.5F, -9.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 84).mirror().addBox(-1.5F, -1.5F, -11.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(70, 82).mirror().addBox(-1.5F, 3.5F, -11.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 72).mirror().addBox(-1.0F, -2.5F, -12.0F, 2.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -1.5F, -14.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition tail = upper_body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(66, 38).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 7.0F));

		PartDefinition leg_left = body_main.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(72, 0).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 8.0F, 4.0F));

		PartDefinition knee_left = leg_left.addOrReplaceChild("knee_left", CubeListBuilder.create().texOffs(52, 76).addBox(-1.0F, -2.0F, -2.5F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 3.0F, 3.5F));

		PartDefinition foot_left = knee_left.addOrReplaceChild("foot_left", CubeListBuilder.create().texOffs(72, 16).addBox(-2.0F, 0.0F, -5.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.5F));

		PartDefinition leg_right = body_main.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(72, 0).mirror().addBox(-3.0F, -3.0F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.5F, 8.0F, 4.0F));

		PartDefinition knee_right = leg_right.addOrReplaceChild("knee_right", CubeListBuilder.create().texOffs(52, 76).mirror().addBox(-3.0F, -2.0F, -2.5F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 3.0F, 3.5F));

		PartDefinition foot_right = knee_right.addOrReplaceChild("foot_right", CubeListBuilder.create().texOffs(72, 16).mirror().addBox(-4.0F, 0.0F, -5.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 5.0F, 0.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
}