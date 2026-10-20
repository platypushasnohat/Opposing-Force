package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.GhostAnimations;
import com.barl_inc.opposing_force.entity.Ghost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class GhostModel extends SinewEntityModel<Ghost> {

    private final ModelPart root;
    private final ModelPart ghost;
    private final ModelPart head;
    private final ModelPart armLeft;
    private final ModelPart armRight;

    public GhostModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.ghost = root.getChild("Ghost");
        this.head = this.ghost.getChild("Head");
        this.armLeft = this.ghost.getChild("Arm_Left");
        this.armRight = this.ghost.getChild("Arm_Right");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Ghost entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateIdleSmooth(entity.idleAnimationState, GhostAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateWalkSmooth(entity.walkAnimationState, GhostAnimations.MOVE, limbSwing, limbSwingAmount, partialTicks);
        this.animateSmooth(entity.attackAnimationState, GhostAnimations.ATTACK, ageInTicks, partialTicks);
        this.animateSmooth(entity.attack2AnimationState, GhostAnimations.ATTACK2, ageInTicks, partialTicks);
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD;
    }

    public void translateToHand(PoseStack poseStack, boolean left) {
        this.ghost.translateAndRotate(poseStack);
        (left ? this.armLeft : this.armRight).translateAndRotate(poseStack);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition ghost = partdefinition.addOrReplaceChild("Ghost", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -4.0F, 10.0F, 13.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 43).addBox(-5.0F, -5.0F, -4.0F, 10.0F, 13.0F, 8.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        ghost.addOrReplaceChild("Cloak", CubeListBuilder.create().texOffs(36, 0).addBox(-5.0F, 0.0F, 0.025F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0025F)), PartPose.offset(0.0F, 8.0F, 3.975F));

        PartDefinition head = ghost.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(32, 37).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F))
                .texOffs(0, 33).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.5F));

        head.addOrReplaceChild("LowerJaw", CubeListBuilder.create().texOffs(36, 19).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0025F)), PartPose.offset(0.0F, 5.0F, -2.0F));

        PartDefinition armLeft = ghost.addOrReplaceChild("Arm_Left", CubeListBuilder.create().texOffs(36, 5).addBox(0.0F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 21).addBox(0.0F, 0.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(20, 34).addBox(0.0F, 8.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(5.275F, 5.0F, 0.0F));

        PartDefinition chain1 = armLeft.addOrReplaceChild("Chain1", CubeListBuilder.create().texOffs(36, 16).addBox(1.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F))
                .texOffs(-1, 27).addBox(-0.25F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0025F)), PartPose.offset(3.25F, 10.75F, 0.0F));

        PartDefinition chain2 = chain1.addOrReplaceChild("Chain2", CubeListBuilder.create().texOffs(36, 16).addBox(1.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F))
                .texOffs(-1, 27).addBox(-0.25F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0025F)), PartPose.offset(4.5F, 0.0F, 0.0F));

        chain2.addOrReplaceChild("Chain3", CubeListBuilder.create().texOffs(36, 16).addBox(1.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F))
                .texOffs(-1, 27).addBox(-0.25F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0025F)), PartPose.offset(4.5F, 0.0F, 0.0F));

        PartDefinition armRight = ghost.addOrReplaceChild("Arm_Right", CubeListBuilder.create().texOffs(36, 5).mirror().addBox(-5.0F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 21).mirror().addBox(-3.0F, 0.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(20, 34).mirror().addBox(-3.0F, 8.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(-5.275F, 5.0F, 0.0F));

        PartDefinition chain4 = armRight.addOrReplaceChild("Chain4", CubeListBuilder.create().texOffs(36, 16).mirror().addBox(-5.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F)).mirror(false)
                .texOffs(-1, 27).mirror().addBox(-1.75F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0025F)).mirror(false), PartPose.offset(-3.25F, 10.75F, 0.0F));

        PartDefinition chain5 = chain4.addOrReplaceChild("Chain5", CubeListBuilder.create().texOffs(36, 16).mirror().addBox(-5.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F)).mirror(false)
                .texOffs(-1, 27).mirror().addBox(-1.75F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0025F)).mirror(false), PartPose.offset(-4.5F, 0.0F, 0.0F));

        chain5.addOrReplaceChild("Chain6", CubeListBuilder.create().texOffs(36, 16).mirror().addBox(-5.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F)).mirror(false)
                .texOffs(-1, 27).mirror().addBox(-1.75F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0025F)).mirror(false), PartPose.offset(-4.5F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
