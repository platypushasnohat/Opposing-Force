package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.client.model.item.animation.CrackshotAnimations;
import com.barl_inc.opposing_force.item.ranged.CrackshotItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.item.AnimatedItemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CrackshotModel extends AnimatedItemModel<CrackshotItem> {

    private final ModelPart root;

    public CrackshotModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(20, 11).addBox(0.0F, -1.0F, 0.0F, 2.0F, 4.0F, 2.0F)
                .texOffs(10, 20).addBox(-1.0F, -4.0F, -3.0F, 4.0F, 4.0F, 1.0F)
                .texOffs(20, 17).addBox(1.0F, 1.0F, -2.0F, 0.0F, 2.0F, 2.0F)
                .texOffs(0, 0).addBox(0.0F, -3.0F, -7.0F, 2.0F, 2.0F, 9.0F), PartPose.offset(-1.0F, 21.0F, 4.0F));

        root.addOrReplaceChild("stock", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -1.0F, -3.5F, 3.0F, 2.0F, 7.0F), PartPose.offset(1.0F, 0.0F, -3.5F));

        root.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F), PartPose.offset(1.0F, -2.0F, -7.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, CrackshotItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.shootAnimationState, CrackshotAnimations.SHOOT, ageInTicks);
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
