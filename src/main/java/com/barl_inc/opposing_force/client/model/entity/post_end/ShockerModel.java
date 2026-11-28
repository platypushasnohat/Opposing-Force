package com.barl_inc.opposing_force.client.model.entity.post_end;

import com.barl_inc.opposing_force.client.model.entity.animation.post_end.ShockerAnimations;
import com.barl_inc.opposing_force.entity.post_end.Shocker;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.EnumSet;

public class ShockerModel extends SinewEntityModel<Shocker> {

    private final ModelPart root;
    private final ModelPart head;

    public ShockerModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.head = this.root.getChild("body_main").getChild("body").getChild("head");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Shocker entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, ShockerAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F, partialTicks);
        this.animateWalkSmooth(entity.aggroWalkAnimationState, ShockerAnimations.AGGRO_WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, ShockerAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateIdleSmooth(entity.aggroIdleAnimationState, ShockerAnimations.AGGRO_IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.aggroStartAnimationState, ShockerAnimations.AGGRO_START, ageInTicks, partialTicks);
        this.animateSmooth(entity.tweakAnimationState, ShockerAnimations.TWEAKING, ageInTicks, partialTicks);
        this.animateSmooth(entity.chargeAnimationState, ShockerAnimations.CHARGE_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.shootAnimationState, ShockerAnimations.SHOOT_BLEND, ageInTicks, partialTicks);
        float yaw = (entity.getHeadSpin(partialTicks) + netHeadYaw) * Mth.DEG_TO_RAD;
        float offsetX = this.head.x - this.head.getInitialPose().x;
        float offsetZ = this.head.z - this.head.getInitialPose().z;
        this.head.yRot += yaw;
        this.head.x = this.head.getInitialPose().x + offsetX * Mth.cos(yaw) + offsetZ * Mth.sin(yaw);
        this.head.z = this.head.getInitialPose().z - offsetX * Mth.sin(yaw) + offsetZ * Mth.cos(yaw);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(27, 54).addBox(-8.5F, -2.0F, 4.0F, 17.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(33, 10).addBox(-8.5F, -2.0F, -5.0F, 17.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(10, 76).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.002F)), PartPose.offset(0.0F, -2.0F, -3.0F));

        head.addOrReplaceChild("left_head", CubeListBuilder.create().texOffs(77, 60).addBox(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(50, 50).addBox(2.0F, -0.5F, -24.0F, 2.0F, 1.0F, 23.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(0.0F, -0.5F, -24.5F, 5.0F, 1.0F, 23.0F, new CubeDeformation(0.5F)), PartPose.offset(1.5F, -0.5F, 0.0F));

        head.addOrReplaceChild("right_head", CubeListBuilder.create().texOffs(27, 60).addBox(-5.0F, -0.5F, -1.0F, 4.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 50).addBox(-5.0F, -0.5F, -24.0F, 2.0F, 1.0F, 23.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -0.5F, -24.5F, 5.0F, 1.0F, 23.0F, new CubeDeformation(0.5F)), PartPose.offset(-0.5F, -0.5F, 0.0F));

        head.addOrReplaceChild("eye_right", CubeListBuilder.create().texOffs(-5, 74).addBox(-5.0F, 0.0F, -0.5F, 5.0F, 0.0F, 5.0F, EnumSet.of(Direction.DOWN)), PartPose.offsetAndRotation(-5.5F, -1.0F, 0.5F, 0.9599F, 0.0F, 0.0F));

        head.addOrReplaceChild("eye_left", CubeListBuilder.create().texOffs(-5, 79).addBox(0.0F, 0.0F, -0.5F, 5.0F, 0.0F, 5.0F, EnumSet.of(Direction.DOWN)), PartPose.offsetAndRotation(5.5F, -1.0F, 0.5F, 0.9599F, 0.0F, 0.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition legs_left = leg_control.addOrReplaceChild("legs_left", CubeListBuilder.create(), PartPose.offset(7.5F, -0.5F, -2.5F));

        legs_left.addOrReplaceChild("leg_left1", CubeListBuilder.create().texOffs(10, 79).mirror().addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, 0.6109F, 0.6109F));

        legs_left.addOrReplaceChild("leg_left2", CubeListBuilder.create().texOffs(10, 79).mirror().addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        legs_left.addOrReplaceChild("leg_left3", CubeListBuilder.create().texOffs(10, 79).mirror().addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, -0.6109F, 0.6109F));

        PartDefinition legs_right = leg_control.addOrReplaceChild("legs_right", CubeListBuilder.create(), PartPose.offset(-8.0F, -0.5F, -2.5F));

        legs_right.addOrReplaceChild("leg_right1", CubeListBuilder.create().texOffs(10, 74).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.6109F, -0.6109F));

        legs_right.addOrReplaceChild("leg_right2", CubeListBuilder.create().texOffs(10, 74).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        legs_right.addOrReplaceChild("leg_right3", CubeListBuilder.create().texOffs(10, 74).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.6109F, -0.6109F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}