package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.SoulAnimations;
import com.barl_inc.opposing_force.entity.projectile.Soul;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class SoulModel extends HierarchicalModel<Soul> {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    private final ModelPart root;
    private final ModelPart projectile;

    public SoulModel(ModelPart root) {
        this.root = root.getChild("root");
        this.projectile = this.root.getChild("projectile");
    }

    @Override
    public void setupAnim(Soul entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float partialTicks = ageInTicks - entity.tickCount;
        KeyframeAnimations.animate(this, SoulAnimations.PROJECTILE, (long) (ageInTicks * 50.0F), 1.0F, ANIMATION_VECTOR_CACHE);
        this.projectile.xRot += (-Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())) * Mth.DEG_TO_RAD;
        this.projectile.yRot += (180.0F - Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())) * Mth.DEG_TO_RAD;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition projectile = root.addOrReplaceChild("projectile", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
        projectile.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));
        return LayerDefinition.create(meshdefinition, 16, 16);
    }
}
