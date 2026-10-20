package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.RoamerAnimations;
import com.barl_inc.opposing_force.entity.Roamer;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class RoamerModel extends SinewEntityModel<Roamer> {

    private final ModelPart root;

    public RoamerModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Roamer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateIdleSmooth(entity.idleAnimationState, RoamerAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateWalkSmooth(entity.walkAnimationState, RoamerAnimations.WALK, limbSwing, limbSwingAmount, 6.0F, 2.5F, partialTicks);
        this.animateSmooth(entity.lookAroundAnimationState, RoamerAnimations.LOOK_AROUND, ageInTicks, partialTicks);
        this.animateSmooth(entity.tweakingAnimationState, RoamerAnimations.TWEAKING, ageInTicks, partialTicks);
        this.animateSmooth(entity.disappearAnimationState, RoamerAnimations.DISSAPEARING, ageInTicks, partialTicks);
        this.animateSmooth(entity.appearAnimationState, RoamerAnimations.APPEARING, ageInTicks, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0F, 0.0F));

        bodyMain.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -11.0F, -2.0F, 11.0F, 12.0F, 4.0F), PartPose.offset(0.0F, -0.5F, 0.0F));

        bodyMain.addOrReplaceChild("leg_left", CubeListBuilder.create().mirror()
                .texOffs(0, 16).addBox(-2.5F, 0.5F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(16, 16).addBox(-2.5F, 0.5F, -2.0F, 4.0F, 15.0F, 4.0F), PartPose.offset(4.0F, 0.0F, 0.0F));

        bodyMain.addOrReplaceChild("leg_right", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.5F, 0.5F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(16, 16).addBox(-1.5F, 0.5F, -2.0F, 4.0F, 15.0F, 4.0F), PartPose.offset(-4.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
