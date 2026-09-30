package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.AcidChargeAnimations;
import com.barl_inc.opposing_force.entity.projectile.AcidCharge;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class AcidChargeModel extends HierarchicalModel<AcidCharge> {

    private final ModelPart root;
    private final ModelPart acid_charge;

    public AcidChargeModel(ModelPart root) {
        this.root = root.getChild("root");
        this.acid_charge = this.root.getChild("acid_charge");
    }

    @Override
    public void setupAnim(AcidCharge entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float partialTicks = ageInTicks - entity.tickCount;
        this.animate(entity.projectileAnimationState, AcidChargeAnimations.ACID_CHARGE, ageInTicks);
        this.acid_charge.xRot += (-Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())) * Mth.DEG_TO_RAD;
        this.acid_charge.yRot += (180.0F - Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())) * Mth.DEG_TO_RAD;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("acid_charge", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
