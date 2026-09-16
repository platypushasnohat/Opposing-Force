package com.barl_inc.opposing_force.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class EmeraldArmorModel extends HumanoidModel<LivingEntity> {

    public static final EmeraldArmorModel INSTANCE = new EmeraldArmorModel(createBodyLayer().bakeRoot());

    public EmeraldArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0), 0);
        PartDefinition root = meshdefinition.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition leftLeg = root.getChild("left_leg");
        PartDefinition rightLeg = root.getChild("right_leg");
        PartDefinition leftArm = root.getChild("left_arm");
        PartDefinition rightArm = root.getChild("right_arm");

        head.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(67, 95).addBox(-4.0F, -8.0F, -4.5F, 8.0F, 5.0F, 9.0F, new CubeDeformation(0.2F))
                .texOffs(73, 77).addBox(-5.0F, -8.0F, -4.5F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.2F))
                .texOffs(90, 110).addBox(-1.0F, -9.0F, -5.2F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.ZERO);

        body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(102, 94).addBox(-4.0F, 0.5F, -2.5F, 8.0F, 11.0F, 5.0F, new CubeDeformation(0.51F)), PartPose.ZERO);

        rightArm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create().texOffs(112, 86).mirror().addBox(-3.85F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.35F)).mirror(false)
                .texOffs(76, 120).mirror().addBox(-5.35F, 1.0F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.ZERO);

        leftArm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create().texOffs(112, 86).addBox(-0.15F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.35F))
                .texOffs(76, 120).addBox(-1.65F, 1.0F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_leggings", CubeListBuilder.create().texOffs(97, 111).mirror().addBox(-2.25F, 0.25F, -2.5F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.ZERO);

        leftLeg.addOrReplaceChild("left_leggings", CubeListBuilder.create().texOffs(97, 111).addBox(-1.75F, 0.26F, -2.5F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.ZERO);

        leftLeg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(112, 120).addBox(-1.75F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(112, 120).mirror().addBox(-2.25F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
