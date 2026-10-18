package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.client.model.item.animation.JaegerAnimations;
import com.barl_inc.opposing_force.item.ranged.JaegerItem;
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

public class JaegerModel extends AnimatedItemModel<JaegerItem> {

    private final ModelPart root;

    public JaegerModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 10).addBox(-0.4286F, -0.9286F, 0.1071F, 1.0F, 4.0F, 6.0F)
                .texOffs(0, 0).addBox(-0.4286F, -1.9286F, -13.8929F, 1.0F, 1.0F, 9.0F)
                .texOffs(14, 10).addBox(-0.9286F, -1.9286F, -3.8929F, 2.0F, 2.0F, 4.0F)
                .texOffs(20, 0).addBox(-1.4286F, -2.9286F, -4.8929F, 3.0F, 3.0F, 1.0F)
                .texOffs(20, 4).addBox(0.0714F, 0.0714F, -1.8929F, 0.0F, 2.0F, 2.0F), PartPose.offset(-0.0714F, 20.9286F, 5.8929F));

        root.addOrReplaceChild("stock", CubeListBuilder.create().texOffs(11, 16).addBox(-1.0F, 0.0F, -3.5F, 2.0F, 1.0F, 7.0F), PartPose.offset(0.0714F, -0.9286F, -8.3929F));

        root.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 1.0F), PartPose.offset(0.0714F, -1.4286F, -14.3929F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, JaegerItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.shootAnimationState, JaegerAnimations.SHOOT, ageInTicks);
        this.animate(entity, item.cockAnimationState, JaegerAnimations.COCK, ageInTicks);
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
