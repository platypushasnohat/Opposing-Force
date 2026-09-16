package com.barl_inc.opposing_force.client.model.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class MoonShoesModel extends HumanoidModel<LivingEntity> {

    public static final MoonShoesModel INSTANCE = new MoonShoesModel(createBodyLayer().bakeRoot());

    private final ModelPart right_wing;
    private final ModelPart left_wing;

    public MoonShoesModel(ModelPart root) {
        super(root);
        ModelPart rightLeg = root.getChild("right_leg");
        ModelPart leftLeg = root.getChild("left_leg");
        ModelPart rightBoot = rightLeg.getChild("right_boot");
        ModelPart leftBoot = leftLeg.getChild("left_boot");
        this.right_wing = rightBoot.getChild("right_wing");
        this.left_wing = leftBoot.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0), 0);
        PartDefinition root = meshdefinition.getRoot();
        PartDefinition leftLeg = root.getChild("left_leg");
        PartDefinition rightLeg = root.getChild("right_leg");

        PartDefinition right_boot = rightLeg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 9).addBox(-2.25F, 9.8F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(0, 0).addBox(-3.25F, 7.8F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.ZERO);

        right_boot.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(17, 4).addBox(0.0F, -5.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 10.8F, 2.25F));

        PartDefinition left_boot = leftLeg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.75F, 7.8F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).mirror().addBox(-1.75F, 9.8F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.ZERO);

        left_boot.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(17, 4).mirror().addBox(0.0F, -5.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5F, 10.8F, 2.25F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public MoonShoesModel withAnimations(LivingEntity entity) {
        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        float ageInTicks = entity.tickCount + partialTicks;
        float fly = Mth.sin(ageInTicks * 0.2F) * 0.3F;
        this.right_wing.yRot = -fly;
        this.left_wing.yRot = fly;
        return this;
    }
}
