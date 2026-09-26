package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.BewilderAnimations;
import com.barl_inc.opposing_force.entity.Bewilder;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class BewilderModel extends SinewEntityModel<Bewilder> {

    private final ModelPart root;

    public BewilderModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Bewilder entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, BewilderAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F, partialTicks);
        this.animateWalkSmooth(entity.chargeAnimationState, BewilderAnimations.CHARGE, limbSwing, limbSwingAmount, 1.5F, 2.0F, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, BewilderAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.warnAnimationState, BewilderAnimations.SCREECH, ageInTicks, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition charge_control = root.addOrReplaceChild("charge_control", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));

        PartDefinition body_main = charge_control.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 9.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(80, 92).addBox(-2.5F, -28.6786F, -17.1786F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.5F, -18.6786F, -11.1786F, 17.0F, 22.0F, 23.0F, new CubeDeformation(0.0F))
                .texOffs(0, 45).addBox(-2.5F, -18.6786F, -37.1786F, 5.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(62, 92).addBox(-2.5F, -21.6786F, -41.1786F, 5.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(80, 34).addBox(-2.5F, -23.6786F, -41.1786F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(80, 21).addBox(-2.5F, -25.6786F, -9.1786F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(28, 79).addBox(-2.5F, -28.6786F, -15.1786F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.3214F, -9.8214F));

        body.addOrReplaceChild("mandible_left", CubeListBuilder.create().texOffs(28, 94).addBox(-3.0F, -1.0F, -2.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(80, 42).addBox(-3.0F, 3.0F, -2.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 0.3214F, -11.1786F));

        body.addOrReplaceChild("mandible_right", CubeListBuilder.create().texOffs(28, 94).mirror().addBox(-1.0F, -1.0F, -2.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(80, 42).mirror().addBox(-1.0F, 3.0F, -2.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 0.3214F, -11.1786F));

        body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(40, 94).addBox(-3.5F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 3.3214F, -11.1786F));

        body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(40, 94).mirror().addBox(-0.5F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 3.3214F, -11.1786F));

        body.addOrReplaceChild("elytra_left", CubeListBuilder.create().texOffs(62, 45).addBox(-8.5F, -1.0F, 0.0F, 9.0F, 13.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, -18.6786F, -1.1786F));

        body.addOrReplaceChild("elytra_right", CubeListBuilder.create().texOffs(62, 45).mirror().addBox(-0.5F, -1.0F, 0.0F, 9.0F, 13.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.5F, -18.6786F, -1.1786F));

        body.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(62, 73).addBox(-4.5F, -4.5F, 0.0F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.1786F, 11.8214F));

        body_main.addOrReplaceChild("leg_left1", CubeListBuilder.create().texOffs(0, 79).addBox(0.0F, -2.0F, -6.0F, 7.0F, 14.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, 0.0F, -19.0F));

        body_main.addOrReplaceChild("leg_right1", CubeListBuilder.create().texOffs(0, 79).mirror().addBox(-7.0F, -2.0F, -6.0F, 7.0F, 14.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.5F, 0.0F, -19.0F));

        charge_control.addOrReplaceChild("leg_left2", CubeListBuilder.create().texOffs(80, 0).addBox(0.0F, -2.0F, -2.0F, 7.0F, 14.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, -12.0F, 9.0F));

        charge_control.addOrReplaceChild("leg_right2", CubeListBuilder.create().texOffs(80, 0).mirror().addBox(-7.0F, -2.0F, -2.0F, 7.0F, 14.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.5F, -12.0F, 9.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
