package com.barl_inc.opposing_force.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ReconKnightArmorModel extends HumanoidModel<LivingEntity> {

    public static final ReconKnightArmorModel INSTANCE = new ReconKnightArmorModel(createBodyLayer().bakeRoot());

    private final ModelPart cape;

    public ReconKnightArmorModel(ModelPart root) {
        super(root);
        this.cape = this.body.getChild("chestplate").getChild("cape");
    }

    //the way vanilla does it or something
    public void setupCape(LivingEntity entity, float partialTicks) {
        float lift;
        float sway;
        if (entity instanceof Player player) {
            double dx = Mth.lerp(partialTicks, player.xCloakO, player.xCloak) - Mth.lerp(partialTicks, player.xo, player.getX());
            double dy = Mth.lerp(partialTicks, player.yCloakO, player.yCloak) - Mth.lerp(partialTicks, player.yo, player.getY());
            double dz = Mth.lerp(partialTicks, player.zCloakO, player.zCloak) - Mth.lerp(partialTicks, player.zo, player.getZ());
            float bodyRot = Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot) * Mth.DEG_TO_RAD;
            double sin = Mth.sin(bodyRot);
            double cos = -Mth.cos(bodyRot);

            float fall = Mth.clamp((float) dy * 10.0F, -6.0F, 32.0F);
            float forward = Mth.clamp((float) (dx * sin + dz * cos) * 100.0F, 0.0F, 150.0F);
            float sideways = Mth.clamp((float) (dx * cos - dz * sin) * 100.0F, -20.0F, 20.0F);
            float bob = Mth.lerp(partialTicks, player.oBob, player.bob);
            fall += Mth.sin(Mth.lerp(partialTicks, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * bob;

            lift = 6.0F + forward / 2.0F + fall;
            sway = sideways / 2.0F;
        } else {
            float speed = entity.walkAnimation.speed(partialTicks);
            float position = entity.walkAnimation.position(partialTicks);
            lift = 6.0F + speed * 45.0F + Mth.sin(position * 0.6662F) * 6.0F * speed;
            sway = 0.0F;
        }
        this.cape.xRot = lift * Mth.DEG_TO_RAD;
        this.cape.zRot = -sway * Mth.DEG_TO_RAD;
        this.cape.yRot = sway * Mth.DEG_TO_RAD;
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

        head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(40, 90).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(40, 78).addBox(-5.0F, -8.1F, -4.5F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.5F))
                .texOffs(72, 90).addBox(-4.5F, -2.025F, -4.5F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.5F))
                .texOffs(98, 101).addBox(3.6F, -7.0F, -1.5F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(98, 101).mirror().addBox(-4.6F, -7.0F, -1.5F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.ZERO);

        PartDefinition chestplate = body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(72, 101).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F, new CubeDeformation(0.51F))
                .texOffs(40, 106).addBox(-4.0F, 9.0F, -2.5F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.51F))
                .texOffs(78, 68).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.ZERO);

        chestplate.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(40, 56).addBox(-7.0F, -1.0F, 0.15F, 14.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        rightArm.addOrReplaceChild("right_pauldron", CubeListBuilder.create().texOffs(98, 114).addBox(-3.9F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.35F))
                .texOffs(78, 84).addBox(-3.9F, -3.6F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.35F))
                .texOffs(102, 77).addBox(-3.9F, 5.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.35F))
                .texOffs(106, 58).addBox(-3.3F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.ZERO);

        leftArm.addOrReplaceChild("left_pauldron", CubeListBuilder.create().texOffs(106, 58).mirror().addBox(-0.7F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false)
                .texOffs(102, 77).mirror().addBox(-1.1F, 5.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false)
                .texOffs(78, 84).mirror().addBox(-1.1F, -3.6F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false)
                .texOffs(98, 114).mirror().addBox(-1.1F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false), PartPose.ZERO);

        leftLeg.addOrReplaceChild("left_legging", CubeListBuilder.create().texOffs(112, 87).mirror().addBox(-1.75F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.ZERO);

        leftLeg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(61, 117).mirror().addBox(-1.75F, 10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_legging", CubeListBuilder.create().texOffs(82, 112).addBox(-2.25F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(61, 117).addBox(-2.25F, 10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}