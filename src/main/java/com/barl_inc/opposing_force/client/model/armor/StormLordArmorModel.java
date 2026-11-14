package com.barl_inc.opposing_force.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;

import java.util.Set;

public class StormLordArmorModel extends ReconKnightArmorModel {

    public static final StormLordArmorModel INSTANCE = new StormLordArmorModel(createBodyLayer().bakeRoot());

    public StormLordArmorModel(ModelPart root) {
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

        head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(94, 17).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(48, 38).addBox(-5.0F, -8.0F, -4.5F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.7F))
                .texOffs(86, 47).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.7F))
                .texOffs(88, 33).addBox(3.6F, -7.0F, -1.5F, 1.0F, 5.0F, 9.0F, new CubeDeformation(0.35F))
                .texOffs(108, 33).addBox(-4.4F, -7.0F, -1.5F, 1.0F, 5.0F, 9.0F, new CubeDeformation(0.35F))
                .texOffs(120, 0).addBox(2.0F, -15.0F, -4.8F, 0.0F, 7.0F, 4.0F, Set.of(Direction.EAST))
                .texOffs(120, 0).addBox(-2.0F, -15.0F, -4.8F, 0.0F, 7.0F, 4.0F, Set.of(Direction.EAST)), PartPose.ZERO);

        PartDefinition chestplate = body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(64, 22).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.51F))
                .texOffs(94, 0).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 12.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.ZERO);

        chestplate.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(64, 0).addBox(-7.0F, -1.0F, 0.15F, 14.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        rightArm.addOrReplaceChild("right_armplate", CubeListBuilder.create().texOffs(52, 57).mirror().addBox(-3.25F, 8.0F, -3.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(66, 50).mirror().addBox(-3.55F, 6.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false)
                .texOffs(0, 32).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false)
                .texOffs(52, 50).mirror().addBox(-7.75F, -5.0F, 0.0F, 7.0F, 7.0F, 0.0F, Set.of(Direction.NORTH)).mirror(false)
                .texOffs(32, 50).mirror().addBox(-4.75F, -3.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false), PartPose.ZERO);

        leftArm.addOrReplaceChild("left_armplate", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F))
                .texOffs(52, 50).addBox(0.75F, -5.0F, 0.0F, 7.0F, 7.0F, 0.0F, Set.of(Direction.NORTH))
                .texOffs(32, 50).addBox(-0.25F, -3.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.35F))
                .texOffs(66, 50).addBox(-1.45F, 6.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.35F))
                .texOffs(52, 57).addBox(-0.75F, 8.0F, -3.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        leftLeg.addOrReplaceChild("left_legging", CubeListBuilder.create().texOffs(16, 32).addBox(-1.75F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO);

        leftLeg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(48, 32).addBox(-1.75F, 10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.26F)), PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_legging", CubeListBuilder.create().texOffs(32, 32).addBox(-2.25F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(66, 56).addBox(-2.25F, 10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.26F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}