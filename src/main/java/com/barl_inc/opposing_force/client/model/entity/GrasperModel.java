package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.GrasperAnimations;
import com.barl_inc.opposing_force.entity.Grasper;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GrasperModel extends SinewEntityModel<Grasper> {

    private final ModelPart root;

    public GrasperModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Grasper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateIdleSmooth(entity.idleAnimationState, GrasperAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateWalkSmooth(entity.walkAnimationState, GrasperAnimations.MOVING, limbSwing, limbSwingAmount, 3.0F, 2.5F, partialTicks);
        this.animateSmooth(entity.leapStartAnimationState, GrasperAnimations.LEAP_START, ageInTicks, partialTicks);
        this.animateSmooth(entity.leapLoopAnimationState, GrasperAnimations.LEAP_LOOP, ageInTicks, partialTicks);
        this.animateSmooth(entity.punchAnimationState, GrasperAnimations.PUNCH, ageInTicks, partialTicks);
        this.animateSmooth(entity.slapAnimationState, GrasperAnimations.SLAP, ageInTicks, partialTicks);
        this.animateSmooth(entity.flingedAnimationState, GrasperAnimations.FLINGED, ageInTicks, partialTicks);
        this.animateSmooth(entity.stunAnimationState, GrasperAnimations.STUN, ageInTicks, partialTicks);
        this.animateSmooth(entity.flipBackAnimationState, GrasperAnimations.FLIP_BACK, ageInTicks, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 0.0F));

        PartDefinition body = bodyMain.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition handMain = body.addOrReplaceChild("hand_main", CubeListBuilder.create(), PartPose.offset(4.5F, 0.5F, 2.5F));

        handMain.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -1.5F, -5.25F, 9.0F, 3.0F, 9.0F)
                .texOffs(18, 19).addBox(-2.5F, -1.5F, 3.75F, 5.0F, 3.0F, 2.0F), PartPose.offset(-4.5F, -0.5F, -1.75F));

        CubeListBuilder finger = CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -7.0F, 2.0F, 2.0F, 7.0F)
                .texOffs(0, 21).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 2.0F, 2.0F);

        handMain.addOrReplaceChild("index_finger", finger, PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -0.2618F, 0.0F));
        handMain.addOrReplaceChild("middle_finger", finger, PartPose.offsetAndRotation(-3.0F, 0.0F, -7.0F, 0.0F, -0.0873F, 0.0F));
        handMain.addOrReplaceChild("ring_finger", finger, PartPose.offsetAndRotation(-6.0F, 0.0F, -7.0F, 0.0F, 0.0873F, 0.0F));
        handMain.addOrReplaceChild("pinky_finger", finger, PartPose.offsetAndRotation(-9.0F, 0.0F, -6.0F, 0.0F, 0.2618F, 0.0F));

        body.addOrReplaceChild("thumb_left", CubeListBuilder.create().texOffs(18, 12).addBox(0.0F, -1.0F, -4.0F, 2.0F, 2.0F, 5.0F), PartPose.offset(4.5F, 0.5F, 3.5F));
        body.addOrReplaceChild("thumb_right", CubeListBuilder.create().texOffs(32, 12).mirror().addBox(-2.0F, -1.0F, -4.0F, 2.0F, 2.0F, 5.0F), PartPose.offset(-4.5F, 0.5F, 3.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
