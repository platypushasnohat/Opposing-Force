package com.barl_inc.opposing_force.client.model.entity;// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.barl_inc.opposing_force.client.model.entity.animation.BileGlobAnimations;
import com.barl_inc.opposing_force.entity.projectile.AcidCharge;
import com.barl_inc.opposing_force.entity.projectile.BileGlob;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BileGlobModel extends HierarchicalModel<BileGlob> {
	private final ModelPart root;
	private final ModelPart bile_projectile;

	public BileGlobModel(ModelPart root) {
		this.root = root.getChild("root");
		this.bile_projectile = this.root.getChild("bile_projectile");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition bile_projectile = root.addOrReplaceChild("bile_projectile", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -5.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(-3.0F, -6.0F, 1.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(3.0F, -6.0F, 1.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).mirror().addBox(-3.0F, -6.0F, 1.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 12).mirror().addBox(-3.0F, 0.0F, 1.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(BileGlob entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float partialTicks = ageInTicks - entity.tickCount;
        this.animate(entity.projectileAnimationState, BileGlobAnimations.BILE_GLOB, ageInTicks);
        this.bile_projectile.xRot += (-Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())) * Mth.DEG_TO_RAD;
        this.bile_projectile.yRot += (180.0F - Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())) * Mth.DEG_TO_RAD;
    }
}