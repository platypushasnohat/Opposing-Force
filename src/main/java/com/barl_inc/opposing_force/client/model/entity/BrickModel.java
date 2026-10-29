package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.BrickAnimations;
import com.barl_inc.opposing_force.entity.Brick;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BrickModel extends SinewEntityModel<Brick> {
    private final ModelPart root;

    public BrickModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Brick entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateWalkSmooth(entity.walkAnimationState, BrickAnimations.MOVE, limbSwing, limbSwingAmount, 3.0F, 2.5F, partialTicks);
        this.animateWalkSmooth(entity.sprintAnimationState, BrickAnimations.RUN, limbSwing, limbSwingAmount, partialTicks);
        this.animateIdleSmooth(entity.idleAnimationState, BrickAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateSmooth(entity.danceAnimationState, BrickAnimations.DANCE, ageInTicks, partialTicks);
        this.animateSmooth(entity.defendStartAnimationState, BrickAnimations.DEFEND_START, ageInTicks, partialTicks);
        this.animateSmooth(entity.defendAnimationState, BrickAnimations.DEFEND, ageInTicks, partialTicks);
        this.animateSmooth(entity.meltAnimationState, BrickAnimations.MELT, ageInTicks, partialTicks);
        this.animateSmooth(entity.squishAnimationState, BrickAnimations.SQUISH_BLEND, ageInTicks, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        bodyMain.addOrReplaceChild("body", CubeListBuilder.create().texOffs(156, 0).mirror().addBox(-18.0F, -13.0F, 9.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(72, 127).addBox(-10.0F, -12.0F, -10.0F, 20.0F, 12.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-17.0F, -30.0F, -15.0F, 34.0F, 16.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(124, 130).addBox(7.0F, -14.0F, -15.0F, 10.0F, 9.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(0, 91).addBox(-7.0F, -14.0F, -17.0F, 14.0F, 2.0F, 34.0F, new CubeDeformation(0.0F))
                .texOffs(96, 91).addBox(7.0F, -5.0F, -17.0F, 12.0F, 2.0F, 34.0F, new CubeDeformation(0.0F))
                .texOffs(0, 127).addBox(5.0F, -12.0F, -17.0F, 2.0F, 9.0F, 34.0F, new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(-9.0F, -31.0F, -16.0F, 18.0F, 13.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(128, 14).addBox(11.0F, -29.0F, -16.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(128, 14).addBox(11.0F, -21.0F, -16.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(128, 14).addBox(11.0F, -13.0F, -16.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(156, 14).addBox(11.0F, -29.0F, 9.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(156, 14).addBox(11.0F, -21.0F, 9.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(156, 14).addBox(11.0F, -13.0F, 9.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(100, 46).mirror().addBox(-17.0F, -14.0F, -15.0F, 10.0F, 9.0F, 30.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(146, 52).mirror().addBox(-19.0F, -5.0F, -17.0F, 12.0F, 2.0F, 34.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(150, 0).mirror().addBox(-7.0F, -12.0F, -17.0F, 2.0F, 9.0F, 34.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(156, 0).mirror().addBox(-18.0F, -29.0F, 9.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(156, 0).mirror().addBox(-18.0F, -21.0F, 9.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(128, 0).mirror().addBox(-18.0F, -13.0F, -16.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(128, 0).mirror().addBox(-18.0F, -21.0F, -16.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(128, 0).mirror().addBox(-18.0F, -29.0F, -16.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(99, 0).addBox(16.0F, -25.0F, -6.0F, 2.0F, 15.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(193, 0).mirror().addBox(-18.0F, -25.0F, -6.0F, 2.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(31, 172).addBox(-17.0F, -17.0F, -15.0F, 34.0F, 11.0F, 30.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        bodyMain.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(128, 35).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(100, 85).addBox(-1.5F, 3.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(128, 28).addBox(-1.5F, 6.0F, -3.5F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(112, 85).addBox(-1.5F, 5.0F, -3.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -8.0F, 0.0F));

        bodyMain.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(128, 35).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(100, 85).mirror().addBox(-1.5F, 3.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(128, 28).mirror().addBox(-1.5F, 6.0F, -3.5F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(112, 85).mirror().addBox(-1.5F, 5.0F, -3.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -8.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }
}