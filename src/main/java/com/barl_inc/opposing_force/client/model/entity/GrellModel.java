package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.GrellAnimations;
import com.barl_inc.opposing_force.entity.Grell;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

import java.util.List;

public class GrellModel extends SinewEntityModel<Grell> {

    private final ModelPart root;
    private final ModelPart brain;
    private final ModelPart brainLines;
    private final List<ModelPart> brainPath;
    public float alpha = 1.0F;

    public GrellModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        ModelPart grell = root.getChild("root");
        ModelPart flightControl = grell.getChild("flight_control");
        ModelPart bodyMain = flightControl.getChild("body_main");
        ModelPart body = bodyMain.getChild("body");
        this.brain = body.getChild("brain");
        this.brain.skipDraw = true;
        this.brainLines = this.brain.getChild("brain_lines");
        this.brainLines.visible = false;
        this.brainPath = List.of(root, grell, flightControl, bodyMain, body);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Grell entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateIdleSmooth(entity.idleAnimationState, GrellAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateWalkSmooth(entity.walkAnimationState, GrellAnimations.MOVING, limbSwing, limbSwingAmount, partialTicks);
        this.animateWalkSmooth(entity.chargeAnimationState, GrellAnimations.CHARGING, limbSwing, limbSwingAmount, partialTicks);
        this.animateSmooth(entity.brainAnimationState, GrellAnimations.BRAIN_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.brainAggroAnimationState, GrellAnimations.BRAIN_AGGRO_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.biteAnimationState, GrellAnimations.BITE_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.conjureAnimationState, GrellAnimations.CONJURE_BLEND, ageInTicks, partialTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, this.applyAlpha(color));
    }

    public void renderBrain(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        this.brainPath.forEach(part -> part.translateAndRotate(poseStack));
        this.brain.skipDraw = false;
        this.brain.render(poseStack, buffer, packedLight, packedOverlay, this.applyAlpha(color));
        this.brain.skipDraw = true;
        poseStack.popPose();
    }

    public void renderBrainLines(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        this.brainPath.forEach(part -> part.translateAndRotate(poseStack));
        this.brain.translateAndRotate(poseStack);
        this.brainLines.visible = true;
        this.brainLines.render(poseStack, buffer, packedLight, packedOverlay, this.applyAlpha(color));
        this.brainLines.visible = false;
        poseStack.popPose();
    }

    private int applyAlpha(int color) {
        int alpha = (int) (((color >>> 24) & 0xFF) * this.alpha);
        return (alpha << 24) | (color & 0xFFFFFF);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition flightControl = root.addOrReplaceChild("flight_control", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition bodyMain = flightControl.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -15.5714F, -1.2857F));
        PartDefinition body = bodyMain.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition brain = body.addOrReplaceChild("brain", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -18.0F, -10.0F, 24.0F, 18.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5714F, 1.2857F));
        brain.addOrReplaceChild("brain_lines", CubeListBuilder.create().texOffs(0, 61).addBox(-12.0F, -18.0F, -10.0F, 24.0F, 18.0F, 20.0F, new CubeDeformation(0.3F)), PartPose.ZERO);

        body.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(40, 48).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 47).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.4286F, -8.7143F));
        body.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(16, 38).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.4286F, -6.7143F));

        CubeListBuilder tentacle = CubeListBuilder.create().texOffs(32, 38).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 38).addBox(0.0F, 9.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0025F));
        CubeListBuilder tentacleMirrored = CubeListBuilder.create().texOffs(32, 38).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 38).mirror().addBox(0.0F, 9.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0025F)).mirror(false);
        CubeListBuilder thinTentacle = CubeListBuilder.create().texOffs(16, 46).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 47).addBox(0.0F, 9.0F, -1.5F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0025F));
        CubeListBuilder thinTentacleMirrored = CubeListBuilder.create().texOffs(16, 46).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(10, 47).mirror().addBox(0.0F, 9.0F, -1.5F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0025F)).mirror(false);

        body.addOrReplaceChild("tentacle_front_left", tentacle, PartPose.offsetAndRotation(4.0F, 1.5714F, -1.7143F, 0.0F, -0.7854F, 0.0F));
        body.addOrReplaceChild("tentacle_front_right", tentacleMirrored, PartPose.offsetAndRotation(-4.0F, 1.5714F, -1.7143F, 0.0F, 0.7854F, 0.0F));
        body.addOrReplaceChild("thin_tentacle_front_left", thinTentacle, PartPose.offset(1.5F, 1.5714F, -3.2143F));
        body.addOrReplaceChild("thin_tentacle_front_right", thinTentacleMirrored, PartPose.offset(-1.5F, 1.5714F, -3.2143F));
        body.addOrReplaceChild("tentacle_back_left", tentacle, PartPose.offsetAndRotation(4.0F, 1.5714F, 4.2857F, 0.0F, 0.7854F, 0.0F));
        body.addOrReplaceChild("tentacle_back_right", tentacleMirrored, PartPose.offsetAndRotation(-4.0F, 1.5714F, 4.2857F, 0.0F, -0.7854F, 0.0F));
        body.addOrReplaceChild("thin_tentacle_back_left", thinTentacle, PartPose.offset(1.5F, 1.5714F, 5.7857F));
        body.addOrReplaceChild("thin_tentacle_back_right", thinTentacleMirrored, PartPose.offset(-1.5F, 1.5714F, 5.7857F));

        PartDefinition armLeft = body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(0, 38).addBox(-0.5F, 0.0F, 0.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0025F)), PartPose.offset(6.5F, 1.5714F, 1.2857F));
        PartDefinition elbowLeft = armLeft.addOrReplaceChild("elbow_left", CubeListBuilder.create().texOffs(20, 46).addBox(0.0F, -7.5F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0025F)), PartPose.offset(7.5F, 8.5F, 0.0F));
        elbowLeft.addOrReplaceChild("arm_tip_left", CubeListBuilder.create().texOffs(22, 54).addBox(-2.5F, 0.0F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0025F)), PartPose.offset(4.5F, -6.5F, 0.0F));

        PartDefinition armRight = body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(0, 38).mirror().addBox(-7.5F, 0.0F, 0.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0025F)).mirror(false), PartPose.offset(-6.5F, 1.5714F, 1.2857F));
        PartDefinition elbowRight = armRight.addOrReplaceChild("elbow_right", CubeListBuilder.create().texOffs(20, 46).mirror().addBox(-5.0F, -7.5F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0025F)).mirror(false), PartPose.offset(-7.5F, 8.5F, 0.0F));
        elbowRight.addOrReplaceChild("arm_tip_right", CubeListBuilder.create().texOffs(22, 54).mirror().addBox(-0.5F, 0.0F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0025F)).mirror(false), PartPose.offset(-4.5F, -6.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
