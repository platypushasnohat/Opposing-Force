package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.PipsqueakAnimations;
import com.barl_inc.opposing_force.entity.Pipsqueak;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class PipsqueakModel extends SinewEntityModel<Pipsqueak> {

    private final ModelPart root;
    private final ModelPart bodyMain;

    public PipsqueakModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.bodyMain = this.root.getChild("flight_control").getChild("body_main");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Pipsqueak entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateIdleSmooth(entity.idleAnimationState, PipsqueakAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateWalkSmooth(entity.flyAnimationState, PipsqueakAnimations.FLY, limbSwing, limbSwingAmount, partialTicks);
        this.animateSmooth(entity.hangingAnimationState, PipsqueakAnimations.HANGING, ageInTicks, partialTicks);
        this.animateSmooth(entity.closeMouthAnimationState, PipsqueakAnimations.CLOSE_MOUTH_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.biteAnimationState, PipsqueakAnimations.BITE_BLEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.chokeAnimationState, PipsqueakAnimations.CHOKE_BLEND, ageInTicks, partialTicks);
        this.rotatePart(this.bodyMain, entity.getTilt(partialTicks) * Mth.DEG_TO_RAD, 0.0F, entity.getBank(partialTicks) * Mth.DEG_TO_RAD);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));
        PartDefinition flightControl = root.addOrReplaceChild("flight_control", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));
        PartDefinition bodyMain = flightControl.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.0F));
        PartDefinition body = bodyMain.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition upperBody = body.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(44, 29).addBox(-7.0F, -2.0F, -5.5F, 14.0F, 2.0F, 11.0F, new CubeDeformation(0.001F))
                .texOffs(0, 6).addBox(-2.0F, -1.0F, -5.6F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.0F, 0.0F));
        upperBody.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -6.0F, 14.0F, 18.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.5F));
        upperBody.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(50, 7).addBox(0.0F, -14.0F, -0.08F, 6.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, -5.5F, -0.0873F, 0.2618F, 0.0F));
        upperBody.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(50, 7).mirror().addBox(-6.0F, -14.0F, -0.08F, 6.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, -1.0F, -5.5F, -0.0873F, -0.2618F, 0.0F));
        upperBody.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(34, 31).addBox(-4.0F, -7.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -5.7F, 0.0873F, 0.0F, 0.0F));

        PartDefinition wingControl = upperBody.addOrReplaceChild("wing_control", CubeListBuilder.create(), PartPose.offset(7.0F, -1.0F, -2.0F));
        wingControl.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(44, 0).addBox(0.0F, 0.0F, -1.5F, 9.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        wingControl.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(44, 0).mirror().addBox(-9.0F, 0.0F, -1.5F, 9.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-14.0F, 0.0F, 0.0F));

        PartDefinition lowerBody = body.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(44, 42).addBox(-7.0F, 0.0F, -5.5F, 14.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));
        lowerBody.addOrReplaceChild("teeth", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.6F));

        PartDefinition legControl = bodyMain.addOrReplaceChild("leg_control", CubeListBuilder.create(), PartPose.offset(4.5F, 11.0F, 0.0F));
        PartDefinition legLeft = legControl.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(0, 2).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        legLeft.addOrReplaceChild("foot_left", CubeListBuilder.create().texOffs(39, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition legRight = legControl.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 2).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, 0.0F, 0.0F));
        legRight.addOrReplaceChild("foot_right", CubeListBuilder.create().texOffs(39, 0).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }
}
