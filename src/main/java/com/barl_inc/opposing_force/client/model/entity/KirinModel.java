package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.KirinAnimations;
import com.barl_inc.opposing_force.entity.Kirin;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.Set;

public class KirinModel extends SinewEntityModel<Kirin> {

    private static final Set<Direction> PLANE = Set.of(Direction.EAST);

    private final ModelPart root;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tai3;

    public KirinModel(ModelPart root) {
        this.root = root;
        ModelPart body = root.getChild("root").getChild("body_main").getChild("body_upper").getChild("body");
        this.neck = body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.tail1 = body.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
        this.tai3 = this.tail2.getChild("tai3");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Kirin entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateIdleSmooth(entity.idleAnimationState, KirinAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateWalkSmooth(entity.walkAnimationState, KirinAnimations.WALK, limbSwing, limbSwingAmount, 5.0F, 2.5F, partialTicks);
        this.animateSmooth(entity.idleTwitchOneAnimationState, KirinAnimations.IDLE_TWITCH_BLEND1, ageInTicks, partialTicks);
        this.animateSmooth(entity.idleTwitchTwoAnimationState, KirinAnimations.IDLE_TWITCH_BLEND2, ageInTicks, partialTicks);
        this.animateSmooth(entity.idleTwitchThreeAnimationState, KirinAnimations.IDLE_TWITCH_BLEND3, ageInTicks, partialTicks);
        this.animateSmooth(entity.idleShakeOneAnimationState, KirinAnimations.IDLE_SHAKE_BLEND1, ageInTicks, partialTicks);
        this.animateSmooth(entity.idleShakeTwoAnimationState, KirinAnimations.IDLE_SHAKE_BLEND2, ageInTicks, partialTicks);
        this.animateSmooth(entity.deathAnimationState, KirinAnimations.DISAPPEAR, ageInTicks, partialTicks);
        float lookYaw = Mth.clamp(netHeadYaw, -60.0F, 60.0F) * Mth.DEG_TO_RAD;
        float lookPitch = Mth.clamp(headPitch, -30.0F, 30.0F) * Mth.DEG_TO_RAD;
        this.rotatePart(this.neck, lookPitch * 0.4F, lookYaw * 0.4F + entity.getSegmentYawOffset(0, partialTicks) * Mth.DEG_TO_RAD, 0.0F);
        this.rotatePart(this.head, lookPitch * 0.6F, lookYaw * 0.6F, 0.0F);
        this.rotatePart(this.tail1, 0.0F, entity.getSegmentYawOffset(1, partialTicks) * Mth.DEG_TO_RAD, 0.0F);
        this.rotatePart(this.tail2, 0.0F, 0.0F, entity.getSegmentYawOffset(2, partialTicks) * Mth.DEG_TO_RAD);
        this.rotatePart(this.tai3, 0.0F, entity.getSegmentYawOffset(3, partialTicks) * Mth.DEG_TO_RAD, 0.0F);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -29.0F, 0.0F));

        PartDefinition body_upper = body_main.addOrReplaceChild("body_upper", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 4.5F));

        PartDefinition body = body_upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -5.0F, -8.0F, 11.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 34).addBox(0.0F, -10.0F, -5.0F, 0.0F, 5.0F, 10.0F, PLANE), PartPose.offset(0.0F, -6.0F, -4.5F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(66, 69).addBox(0.0F, -14.0F, -6.0F, 0.0F, 16.0F, 4.0F, PLANE)
                .texOffs(1, 35).addBox(-3.5F, -16.0F, -4.0F, 7.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(54, 0).addBox(-4.0F, -7.0F, -3.5F, 8.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(54, 23).addBox(0.0F, -6.0F, -6.5F, 0.0F, 2.0F, 2.0F, PLANE)
                .texOffs(66, 60).addBox(-2.0F, -4.0F, -9.5F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(46, 70).addBox(0.0F, -12.0F, -10.5F, 0.0F, 9.0F, 3.0F, PLANE), PartPose.offset(0.0F, -16.0F, -0.5F));

        head.addOrReplaceChild("horn_left", CubeListBuilder.create().texOffs(16, 58).addBox(0.0F, -18.0F, -2.0F, 0.0F, 18.0F, 5.0F, PLANE), PartPose.offsetAndRotation(4.0F, -6.0F, -0.5F, 0.0F, 0.0F, 0.0175F));

        head.addOrReplaceChild("horn_right", CubeListBuilder.create().texOffs(16, 58).mirror().addBox(0.0F, -18.0F, -2.0F, 0.0F, 18.0F, 5.0F, PLANE).mirror(false), PartPose.offsetAndRotation(-4.0F, -6.0F, -0.5F, 0.0F, 0.0F, -0.0175F));

        head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(74, 27).addBox(-1.0F, -5.0F, -0.5F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(4.0F, -6.9F, 3.0F));

        head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(74, 27).mirror().addBox(-2.0F, -5.0F, -0.5F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(-4.0F, -6.9F, 3.0F));

        head.addOrReplaceChild("whisker_left", CubeListBuilder.create().texOffs(0, -6).addBox(0.0F, 0.0F, -0.5F, 0.0F, 8.0F, 8.0F, PLANE), PartPose.offsetAndRotation(2.0F, -2.0F, -7.0F, 0.0F, 0.0F, -0.1745F));

        head.addOrReplaceChild("whisker_right", CubeListBuilder.create().texOffs(0, -6).mirror().addBox(0.0F, 0.0F, -0.5F, 0.0F, 8.0F, 8.0F, PLANE).mirror(false), PartPose.offsetAndRotation(-2.0F, -2.0F, -7.0F, 0.0F, 0.0F, 0.1745F));

        head.addOrReplaceChild("mane_left", CubeListBuilder.create().texOffs(30, 27).addBox(0.0F, -4.5F, -6.5F, 0.0F, 13.0F, 14.0F, PLANE), PartPose.offsetAndRotation(4.0F, -2.5F, 3.5F, 0.0F, 0.0F, -0.3491F));

        head.addOrReplaceChild("mane_right", CubeListBuilder.create().texOffs(30, 27).mirror().addBox(0.0F, -4.5F, -6.5F, 0.0F, 13.0F, 14.0F, PLANE).mirror(false), PartPose.offsetAndRotation(-4.0F, -2.5F, 3.5F, 0.0F, 0.0F, 0.3491F));

        head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(26, 70).addBox(-2.0F, 0.0F, -6.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -3.5F));

        PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(54, 14).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 7.5F));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(74, 20).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, 0.5F));

        tail2.addOrReplaceChild("tai3", CubeListBuilder.create().texOffs(30, 54).addBox(-3.0F, -0.5F, 0.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 58).addBox(2.975F, 1.5F, -2.0F, 0.0F, 12.0F, 8.0F, PLANE)
                .texOffs(0, 58).mirror().addBox(-2.975F, 1.5F, -2.0F, 0.0F, 12.0F, 8.0F, PLANE).mirror(false)
                .texOffs(0, 58).addBox(0.0F, 3.5F, -2.0F, 0.0F, 12.0F, 8.0F, PLANE), PartPose.offset(0.0F, 0.0F, 6.0F));

        PartDefinition arm_control = body_upper.addOrReplaceChild("arm_control", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -9.0F));

        PartDefinition arm_left = arm_control.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(74, 14).addBox(-2.5F, 20.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(54, 54).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 23.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.0F, 0.0F));

        arm_left.addOrReplaceChild("arm_fur_left", CubeListBuilder.create().texOffs(66, 49).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition arm_right = arm_control.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(74, 14).mirror().addBox(-2.5F, 20.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(54, 54).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 23.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 0.0F));

        arm_right.addOrReplaceChild("arm_fur_right", CubeListBuilder.create().texOffs(66, 49).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 4.5F));

        PartDefinition leg_left = leg_control.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(74, 14).addBox(-2.5F, 20.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(54, 54).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 23.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.0F, 0.0F));

        leg_left.addOrReplaceChild("leg_fur_left", CubeListBuilder.create().texOffs(66, 49).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition leg_right = leg_control.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(74, 14).mirror().addBox(-2.5F, 20.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(54, 54).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 23.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 0.0F));

        leg_right.addOrReplaceChild("leg_fur_right", CubeListBuilder.create().texOffs(66, 49).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));


        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}