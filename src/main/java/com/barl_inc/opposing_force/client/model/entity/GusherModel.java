package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.GusherAnimations;
import com.barl_inc.opposing_force.entity.Gusher;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class GusherModel extends SinewEntityModel<Gusher> {

    private final ModelPart root;
    private final ModelPart head;

    public GusherModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        ModelPart body_main = this.root.getChild("body_main");
        ModelPart body = body_main.getChild("body");
        ModelPart neck = body.getChild("neck");
        this.head = neck.getChild("head");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Gusher entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, GusherAnimations.WALK, limbSwing, limbSwingAmount, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, GusherAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.attackAnimationState, GusherAnimations.ATTACK_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.gushAnimationState, GusherAnimations.GUSH, ageInTicks, partialTicks);
        this.head.yRot += Math.clamp(netHeadYaw * Mth.DEG_TO_RAD, Mth.PI / -4.0F, Mth.PI / 4.0F);
        this.head.xRot += Math.clamp(headPitch * Mth.DEG_TO_RAD, Mth.PI / -4.0F, 4.0F * Mth.DEG_TO_RAD);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -6.4F, 1.5F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(54, 45).addBox(-4.5F, -5.6F, -8.5F, 9.0F, 10.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 87).addBox(3.0F, -7.6F, -7.5F, 0.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 87).mirror().addBox(-3.0F, -7.6F, -7.5F, 0.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(1, 41).addBox(-5.0F, -25.0F, -4.0F, 10.0F, 28.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.6F, -8.5F));

        neck.addOrReplaceChild("neck_legs_left", CubeListBuilder.create().texOffs(27, 105).addBox(-3.0F, -7.0F, -3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(27, 105).addBox(-3.0F, 2.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -10.0F, -4.0F, 0.0F, -0.7418F, 0.0F));

        neck.addOrReplaceChild("neck_legs_right", CubeListBuilder.create().texOffs(27, 105).mirror().addBox(0.0F, -7.0F, -3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(27, 105).mirror().addBox(0.0F, 2.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -10.0F, -4.0F, 0.0F, 0.7418F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -36.0F, 10.0F, 4.0F, 36.0F, new CubeDeformation(0.01F))
                .texOffs(0, 49).addBox(3.0F, -6.0F, -28.0F, 0.0F, 2.0F, 29.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).mirror().addBox(-3.0F, -6.0F, -28.0F, 0.0F, 2.0F, 29.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -25.0F, 0.0F));

        head.addOrReplaceChild("head_legs_left", CubeListBuilder.create().texOffs(0, 81).addBox(-3.0F, 0.0F, -10.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 81).addBox(-3.0F, 0.0F, 2.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, -20.0F, 0.0F, 0.0F, -0.7418F));

        head.addOrReplaceChild("head_legs_right", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(0.0F, 0.0F, -10.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 81).mirror().addBox(0.0F, 0.0F, 2.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, -20.0F, 0.0F, 0.0F, 0.7418F));

        head.addOrReplaceChild("jaw_left", CubeListBuilder.create().texOffs(0, 105).addBox(-1.0F, -1.0F, -18.0F, 6.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(48, 78).addBox(5.0F, -1.0F, -18.0F, 2.0F, 1.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(0, 96).addBox(0.0F, -2.0F, -1.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 1.0F, -36.0F));

        head.addOrReplaceChild("jaw_right", CubeListBuilder.create().texOffs(0, 105).mirror().addBox(-5.0F, -1.0F, -18.0F, 6.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 78).mirror().addBox(-7.0F, -1.0F, -18.0F, 2.0F, 1.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 96).mirror().addBox(-5.0F, -2.0F, -1.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 1.0F, -36.0F));

        head.addOrReplaceChild("antenna_left", CubeListBuilder.create().texOffs(26, 81).addBox(0.0F, -15.0F, -5.0F, 1.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -3.0F, -36.0F, 0.0F, 0.0F, 1.0472F));

        head.addOrReplaceChild("antenna_right", CubeListBuilder.create().texOffs(26, 81).mirror().addBox(-1.0F, -15.0F, -5.0F, 1.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -3.0F, -36.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(2, 4).addBox(-5.0F, -25.0F, 0.0F, 10.0F, 28.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.6F, 8.5F));

        tail1.addOrReplaceChild("tail1_legs_left", CubeListBuilder.create().texOffs(40, 102).addBox(-3.0F, -11.0F, 0.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(40, 102).addBox(-3.0F, 1.0F, 0.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -9.0F, 4.0F, 0.0F, 0.7418F, 0.0F));

        tail1.addOrReplaceChild("tail1_legs_right", CubeListBuilder.create().texOffs(40, 102).mirror().addBox(0.0F, -11.0F, 0.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 102).mirror().addBox(0.0F, 1.0F, 0.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -9.0F, 4.0F, 0.0F, -0.7418F, 0.0F));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 40).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 33.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(3.0F, -6.0F, 0.0F, 0.0F, 2.0F, 29.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).mirror().addBox(-3.0F, -6.0F, 0.0F, 0.0F, 2.0F, 29.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -25.0F, 0.0F));

        tail2.addOrReplaceChild("tail2_legs_left", CubeListBuilder.create().texOffs(0, 81).addBox(-3.0F, 0.0F, -12.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 81).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 21.0F, 0.0F, 0.0F, -0.7418F));

        tail2.addOrReplaceChild("tail2_legs_right", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(0.0F, 0.0F, -12.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 81).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 21.0F, 0.0F, 0.0F, 0.7418F));

        tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(57, 17).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 33.0F));

        PartDefinition leg_control = body_main.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(3.5F, -0.6F, -0.5F));

        PartDefinition legs_left = leg_control.addOrReplaceChild("legs_left", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, -2.0F));

        legs_left.addOrReplaceChild("leg_left1", CubeListBuilder.create().texOffs(39, 92).addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 81).addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -5.0F, -0.6545F, -0.6545F, 0.0F));

        legs_left.addOrReplaceChild("leg_left2", CubeListBuilder.create().texOffs(39, 92).addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 81).addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, -0.6545F, -1.6581F, 0.0F));

        legs_left.addOrReplaceChild("leg_left3", CubeListBuilder.create().texOffs(39, 92).addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 81).addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, -0.6545F, -1.9635F, 0.0F));

        legs_left.addOrReplaceChild("leg_left4", CubeListBuilder.create().texOffs(39, 92).addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 81).addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 9.0F, -0.6545F, -2.138F, 0.0F));

        PartDefinition legs_right = leg_control.addOrReplaceChild("legs_right", CubeListBuilder.create(), PartPose.offset(-7.0F, 5.0F, -2.0F));

        legs_right.addOrReplaceChild("leg_right1", CubeListBuilder.create().texOffs(39, 92).mirror().addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 81).mirror().addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -5.0F, -0.6545F, 0.6545F, 0.0F));

        legs_right.addOrReplaceChild("leg_right2", CubeListBuilder.create().texOffs(39, 92).mirror().addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 81).mirror().addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, -0.6545F, 1.6581F, 0.0F));

        legs_right.addOrReplaceChild("leg_right3", CubeListBuilder.create().texOffs(39, 92).mirror().addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 81).mirror().addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, -0.6545F, 1.9635F, 0.0F));

        legs_right.addOrReplaceChild("leg_right4", CubeListBuilder.create().texOffs(39, 92).mirror().addBox(-1.0F, 0.0F, -8.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 81).mirror().addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 9.0F, -0.6545F, 2.138F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
