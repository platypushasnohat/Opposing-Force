package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.GhostAnimations;
import com.barl_inc.opposing_force.entity.Ghost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
    public float alpha = 1.0F;

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
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        int alpha = (int) (((color >>> 24) & 0xFF) * this.alpha);
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, (alpha << 24) | (color & 0xFFFFFF));
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
                .texOffs(36, 16).addBox(mirror ? -5.0F : 1.0F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0025F))
                .texOffs(-1, 27).addBox(mirror ? -1.75F : -0.25F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0025F));

        PartDefinition chain = arm.addOrReplaceChild("Chain" + firstChain, link, PartPose.offset(side * 3.25F, 10.75F, 0.0F));
        for (int i = 1; i < 3; i++) {
            chain = chain.addOrReplaceChild("Chain" + (firstChain + i), link, PartPose.offset(side * 4.5F, 0.0F, 0.0F));
        }
    }
}
