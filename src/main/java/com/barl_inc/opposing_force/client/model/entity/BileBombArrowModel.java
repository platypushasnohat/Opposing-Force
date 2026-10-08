package com.barl_inc.opposing_force.client.model.entity;// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.barl_inc.opposing_force.entity.projectile.AcidCharge;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.HierarchicalModel;

public class BileBombArrowModel extends HierarchicalModel<AcidCharge> {
	private final ModelPart root;
	private final ModelPart back;
	private final ModelPart cross_1;
	private final ModelPart cross_2;

	public BileBombArrowModel(ModelPart root) {
		this.root = root.getChild("root");
		this.back = this.root.getChild("back");
		this.cross_1 = this.root.getChild("cross_1");
		this.cross_2 = this.root.getChild("cross_2");
	}

    @Override
    public ModelPart root() {
        return this.root;
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(-3, 9).addBox(-1.5F, -5.0F, -8.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(10, 11).addBox(-1.5F, -5.0F, -5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition back = root.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, -3.5F, 7.0F, -2.3562F, 1.5708F, 0.0F));

        PartDefinition cross_1 = root.addOrReplaceChild("cross_1", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -2.5F, 0.0F, 16.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, -0.5F, 0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, -4.0F, -2.3562F, 1.5708F, 0.0F));

        PartDefinition cross_2 = root.addOrReplaceChild("cross_2", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -2.5F, 0.0F, 16.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, -0.5F, 0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, -4.0F, -0.7854F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(AcidCharge entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

}