package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.FilletAnimations;
import com.barl_inc.opposing_force.entity.Fillet;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class FilletModel extends SinewEntityModel<Fillet> {

    private final ModelPart root;
    private final ModelPart swim_control;

    public FilletModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root.getChild("root");
        this.swim_control = this.root.getChild("swim_control");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Fillet entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.swimAnimationState, FilletAnimations.SWIM, limbSwing, limbSwingAmount, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, FilletAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.flopAnimationState, FilletAnimations.FLOP, ageInTicks, partialTicks);
        this.swim_control.xRot += entity.swimPitch.getPitch(partialTicks) * Mth.DEG_TO_RAD;
        this.swim_control.zRot += -entity.swimPitch.getRoll(partialTicks) * Mth.DEG_TO_RAD;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition swim_control = root.addOrReplaceChild("swim_control", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition body_main = swim_control.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -4.75F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(0.0F, -8.0F, -4.75F, 0.0F, 16.0F, 4.0F, new CubeDeformation(0.0025F))
                .texOffs(8, 13).addBox(0.0F, -7.0F, 0.25F, 0.0F, 14.0F, 3.0F, new CubeDeformation(0.0025F)), PartPose.offset(0.0F, -2.0F, 0.75F));

        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, -2).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 3.25F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}