package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.client.model.item.animation.BlasterAnimations;
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

public class BlasterModel<I extends BlasterItem> extends AnimatedItemModel<I> {

    private final ModelPart root;

    public BlasterModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    @Override
    public void setupAnim(Entity entity, I item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.blastAnimationState, BlasterAnimations.BLASTER_SHOOT, ageInTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 5).addBox(0.0F, 2.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(8, 15).addBox(-0.5F, 1.0F, 2.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 10).addBox(-0.5F, 0.0F, -7.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(14, 15).addBox(-1.0F, -0.5F, -9.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(18, 10).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 2.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
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