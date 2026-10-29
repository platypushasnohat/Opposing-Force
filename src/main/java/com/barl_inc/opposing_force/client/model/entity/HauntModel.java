package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.HauntAnimations;
import com.barl_inc.opposing_force.entity.Haunt;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.EnumSet;
import java.util.Set;

public class HauntModel extends PhasingMonsterModel<Haunt> {

    private static final Set<Direction> PLANE_Z = EnumSet.of(Direction.NORTH);
    private static final Set<Direction> PLANE_Y = EnumSet.of(Direction.UP);

    private final ModelPart root;
    private final ModelPart head;

    public HauntModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("root").getChild("body_main").getChild("body").getChild("head");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    protected void setupAnimations(Haunt entity, float limbSwing, float limbSwingAmount, float ageInTicks, float partialTicks, float netHeadYaw, float headPitch) {
        this.animateIdleSmooth(entity.idleAnimationState, HauntAnimations.IDLE, ageInTicks, partialTicks, limbSwingAmount);
        this.animateWalkSmooth(entity.walkAnimationState, HauntAnimations.FLY, limbSwing, limbSwingAmount, partialTicks);
        this.animateSmooth(entity.attackAnimationState, HauntAnimations.ATTACK, ageInTicks, partialTicks);
        this.animateSmooth(entity.spinStartAnimationState, HauntAnimations.SPINATTACK_START, ageInTicks, partialTicks);
        this.animateSmooth(entity.spinAnimationState, HauntAnimations.SPINATTACK_LOOP, ageInTicks, partialTicks);
        this.animateSmooth(entity.headSpinAnimationState, HauntAnimations.HEAD_SPIN_BLEND, ageInTicks, partialTicks);
        if (!entity.isPassenger()) {
            this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bodyMain = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, 0.0F));

        PartDefinition body = bodyMain.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 47).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 3.0F, 6.0F)
                .texOffs(48, 25).addBox(-3.0F, 0.0F, 2.975F, 6.0F, 3.0F, 0.0F, PLANE_Z), PartPose.ZERO);

        body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(48, 16).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 5.0F, 0.0F, PLANE_Z), PartPose.offset(0.0F, 3.0F, 2.975F));

        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 38).addBox(-0.5F, -9.0F, -6.5F, 2.0F, 9.0F, 13.0F, new CubeDeformation(0.1F))
                .texOffs(0, 16).addBox(-5.5F, -9.0F, -6.0F, 12.0F, 9.0F, 12.0F)
                .texOffs(3, 2).addBox(-6.5F, -7.0F, -6.0F, 14.0F, 2.0F, 12.0F, new CubeDeformation(0.2F))
                .texOffs(32, 37).addBox(-2.5F, -13.0F, -3.0F, 6.0F, 4.0F, 6.0F), PartPose.offset(-0.5F, -0.15F, 0.0F));

        addArm(body, "arm_left", "chain_left", "chain", false);
        addArm(body, "arm_right", "chain_right", "chain2", true);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private static void addArm(PartDefinition body, String name, String cuffName, String chainName, boolean mirror) {
        float side = mirror ? -1.0F : 1.0F;
        PartDefinition arm = body.addOrReplaceChild(name, CubeListBuilder.create().mirror(mirror)
                .texOffs(56, 28).addBox(mirror ? -1.0F : 0.0F, 2.0F, -0.5F, 1.0F, 10.0F, 1.0F)
                .texOffs(32, 56).addBox(mirror ? -3.0F : 0.0F, 0.0F, -1.0F, 3.0F, 2.0F, 2.0F), PartPose.offset(side * 3.0F, 1.0F, 0.0F));

        PartDefinition cuff = arm.addOrReplaceChild(cuffName, CubeListBuilder.create().mirror(mirror).texOffs(48, 21).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 1.0F, 3.0F), PartPose.offset(side * 0.5F, 9.0F, 0.0F));

        cuff.addOrReplaceChild(chainName, CubeListBuilder.create().mirror(mirror)
                .texOffs(48, 28).addBox(mirror ? -4.75F : 0.75F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, PLANE_Z)
                .texOffs(48, 31).addBox(mirror ? -1.25F : -0.75F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, PLANE_Y), PartPose.offset(side * 1.5F, 0.75F, 0.0F));
    }
}
