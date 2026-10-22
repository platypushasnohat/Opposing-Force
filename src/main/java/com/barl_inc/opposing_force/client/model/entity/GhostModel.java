package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.GhostAnimations;
import com.barl_inc.opposing_force.entity.Ghost;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.EnumSet;
import java.util.Set;

public class GhostModel extends PhasingMonsterModel<Ghost> {

    private static final Set<Direction> PLANE_Z = EnumSet.of(Direction.NORTH);
    private static final Set<Direction> PLANE_Y = EnumSet.of(Direction.UP);

    private final ModelPart root;
    private final ModelPart ghost;
    private final ModelPart head;
    private final ModelPart armLeft;
    private final ModelPart armRight;

    public GhostModel(ModelPart root) {
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
        this.head.yRot += Mth.clamp(Mth.wrapDegrees(netHeadYaw), -50.0F, 50.0F) * Mth.DEG_TO_RAD;
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

        ghost.addOrReplaceChild("Cloak", CubeListBuilder.create().texOffs(36, 0).addBox(-5.0F, 0.0F, 0.025F, 10.0F, 5.0F, 0.0F, PLANE_Z), PartPose.offset(0.0F, 8.0F, 3.975F));

        PartDefinition head = ghost.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(32, 37).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 3.0F, 0.0F, PLANE_Z)
                .texOffs(0, 33).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.5F));

        head.addOrReplaceChild("LowerJaw", CubeListBuilder.create().texOffs(36, 19).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, PLANE_Z), PartPose.offset(0.0F, 5.0F, -2.0F));

        addArm(ghost, "Arm_Left", 1, false);
        addArm(ghost, "Arm_Right", 4, true);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private static void addArm(PartDefinition ghost, String name, int firstChain, boolean mirror) {
        float side = mirror ? -1.0F : 1.0F;
        PartDefinition arm = ghost.addOrReplaceChild(name, CubeListBuilder.create().mirror(mirror)
                .texOffs(36, 5).addBox(mirror ? -5.0F : 0.0F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F)
                .texOffs(32, 21).addBox(mirror ? -3.0F : 0.0F, 0.0F, -1.5F, 3.0F, 13.0F, 3.0F)
                .texOffs(20, 34).addBox(mirror ? -3.0F : 0.0F, 8.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(side * 5.275F, 5.0F, 0.0F));

        CubeListBuilder link = CubeListBuilder.create().mirror(mirror)
                .texOffs(36, 16).addBox(mirror ? -5.0F : 1.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, PLANE_Z)
                .texOffs(-1, 27).addBox(mirror ? -1.75F : -0.25F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, PLANE_Y);

        PartDefinition chain = arm.addOrReplaceChild("Chain" + firstChain, link, PartPose.offset(side * 3.25F, 10.75F, 0.0F));
        for (int i = 1; i < 3; i++) {
            chain = chain.addOrReplaceChild("Chain" + (firstChain + i), link, PartPose.offset(side * 4.5F, 0.0F, 0.0F));
        }
    }
}
