package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.GnatAnimations;
import com.barl_inc.opposing_force.entity.Gnat;
import com.platypushasnohat.sinew.client.model.entity.SinewEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class GnatModel extends SinewEntityModel<Gnat> {

    private final ModelPart root;

    public GnatModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root.getChild("root");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Gnat entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateSmooth(entity.flyAnimationState, GnatAnimations.FLY, ageInTicks, partialTicks);
        this.animateSmooth(entity.attackAnimationState, GnatAnimations.ATTACK_BLEND, ageInTicks, partialTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.0F, -1.5F, 10.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(1.5F, 0.0F, -2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-1.5F, -4.0F, -0.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.01F))
                .texOffs(0, 16).addBox(5.5F, -5.0F, 5.5F, 0.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-1.5F, -5.0F, 5.5F, 0.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, -3.0F));

        body.addOrReplaceChild("leg_right1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 1.0F));

        body.addOrReplaceChild("leg_right2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 3.0F));

        body.addOrReplaceChild("leg_right3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 5.0F));

        body.addOrReplaceChild("leg_left1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 1.0F));

        body.addOrReplaceChild("leg_left2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 3.0F));

        body.addOrReplaceChild("leg_left3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 5.0F));

        body.addOrReplaceChild("antenna_left", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -6.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -2.0F, -1.5F));

        body.addOrReplaceChild("antenna_right", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(0.0F, -6.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -2.0F, -1.5F));

        body.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(8, 6).addBox(0.0F, -5.0F, -1.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -5.0F, 4.5F));

        body.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(8, 6).mirror().addBox(0.0F, -5.0F, -1.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, -5.0F, 4.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
