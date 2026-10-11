package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.MudballAnimations;
import com.barl_inc.opposing_force.entity.projectile.Mudball;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MudballModel extends HierarchicalModel<Mudball> {
	private final ModelPart root;
	private final ModelPart mud_glob;

	public MudballModel(ModelPart root) {
		this.root = root.getChild("root");
		this.mud_glob = this.root.getChild("mud_glob");
	}

    @Override
    public ModelPart root() {
        return this.root;
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		root.addOrReplaceChild("mud_glob", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

    @Override
    public void setupAnim(Mudball entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float partialTicks = ageInTicks - entity.tickCount;
        this.animate(entity.projectileAnimationState, MudballAnimations.SQUISH, ageInTicks);
        this.mud_glob.xRot += (-Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())) * Mth.DEG_TO_RAD;
        this.mud_glob.yRot += (180.0F - Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())) * Mth.DEG_TO_RAD;
    }
}