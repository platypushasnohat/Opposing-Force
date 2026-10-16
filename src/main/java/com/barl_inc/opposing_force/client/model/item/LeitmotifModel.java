package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.client.model.item.animation.LeitmotifAnimations;
import com.barl_inc.opposing_force.item.LeitmotifItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.item.AnimatedItemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LeitmotifModel extends AnimatedItemModel<LeitmotifItem> {

    private final ModelPart root;

    public LeitmotifModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(14, 27).addBox(-1.5F, 3.0F, 3.0F, 1.0F, 2.0F, 1.0F)
                .texOffs(14, 30).addBox(-1.0F, 3.0F, 1.0F, 0.0F, 2.0F, 2.0F)
                .texOffs(32, 30).addBox(-1.5F, 3.0F, 10.0F, 1.0F, 2.0F, 1.0F)
                .texOffs(14, 33).addBox(-1.0F, 3.0F, 8.0F, 0.0F, 1.0F, 2.0F), PartPose.offset(1.0F, 21.0F, -4.0F));

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -6.9375F, -6.9375F, 8.0F, 8.0F, 1.0F)
                .texOffs(18, 26).addBox(-3.0F, -5.9375F, 6.0625F, 6.0F, 6.0F, 1.0F)
                .texOffs(22, 33).addBox(-0.5F, -7.9375F, 4.0625F, 1.0F, 1.0F, 1.0F)
                .texOffs(18, 18).addBox(-0.5F, -7.9375F, -3.9375F, 1.0F, 0.0F, 8.0F)
                .texOffs(32, 26).addBox(-1.0F, -3.9375F, 4.0625F, 2.0F, 2.0F, 2.0F)
                .texOffs(18, 33).addBox(-0.5F, -8.9375F, -4.9375F, 1.0F, 2.0F, 1.0F)
                .texOffs(0, 27).addBox(-3.0F, -5.9375F, -5.9375F, 6.0F, 6.0F, 1.0F)
                .texOffs(0, 0).addBox(-4.0F, -6.9375F, -4.9375F, 8.0F, 8.0F, 10.0F), PartPose.offset(-1.0F, 1.9375F, 5.9375F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, LeitmotifItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.shootAnimationState, LeitmotifAnimations.SHOOT, ageInTicks);
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
