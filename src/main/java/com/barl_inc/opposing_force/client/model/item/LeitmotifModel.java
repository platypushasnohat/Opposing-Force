package com.barl_inc.opposing_force.client.model.item;// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.barl_inc.opposing_force.client.model.item.animation.BlasterAnimations;
import com.barl_inc.opposing_force.client.model.item.animation.LeitmotifAnimations;
import com.barl_inc.opposing_force.item.BlasterItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.item.AnimatedItemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LeitmotifModel<I extends BlasterItem> extends AnimatedItemModel<I> {
	private final ModelPart root;
	private final ModelPart body;

	public LeitmotifModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(20, 64).addBox(-1.5F, 0.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 48).addBox(-1.5F, 0.0F, 14.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 57).addBox(-0.5F, 0.0F, 3.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(20, 57).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, -7.0F));

		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -15.0F, -9.125F, 15.0F, 15.0F, 19.0F, new CubeDeformation(0.0F))
		.texOffs(34, 48).addBox(-5.5F, -13.0F, -11.125F, 11.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-7.5F, -15.0F, -13.125F, 15.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(34, 34).addBox(-1.0F, -18.0F, -7.125F, 2.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(60, 55).addBox(-1.0F, -20.0F, -9.125F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 61).addBox(-1.0F, -18.0F, 5.875F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 51).addBox(-4.0F, -11.0F, 11.875F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 51).addBox(-2.0F, -9.0F, 9.875F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 8.125F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

    @Override
    public void setupAnim(Entity entity, I item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.blastAnimationState, LeitmotifAnimations.SHOOT, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root().render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}