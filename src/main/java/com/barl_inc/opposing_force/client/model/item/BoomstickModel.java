package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.client.model.item.animation.BoomstickAnimations;
import com.barl_inc.opposing_force.item.ranged.BoomstickItem;
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

public class BoomstickModel extends AnimatedItemModel<BoomstickItem> {

    private final ModelPart root;

    public BoomstickModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 3.0F, 1.0F, 4.0F, 7.0F)
                .texOffs(16, 6).addBox(0.5F, -2.0F, -1.0F, 2.0F, 3.0F, 4.0F)
                .texOffs(0, 19).addBox(1.5F, 1.0F, 1.0F, 0.0F, 2.0F, 2.0F), PartPose.offset(-1.5F, 20.0F, 3.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 11).addBox(1.3929F, -2.15F, -5.8857F, 2.0F, 2.0F, 6.0F)
                .texOffs(16, 18).addBox(0.3929F, -3.125F, 0.0893F, 4.0F, 4.0F, 1.0F)
                .texOffs(16, 0).addBox(-2.1071F, -1.15F, -3.8857F, 4.0F, 2.0F, 4.0F)
                .texOffs(0, 11).mirror().addBox(-3.3571F, -2.15F, -5.8857F, 2.0F, 2.0F, 6.0F).mirror(false)
                .texOffs(16, 18).mirror().addBox(-4.3571F, -3.125F, 0.0893F, 4.0F, 4.0F, 1.0F).mirror(false), PartPose.offset(1.6071F, 0.15F, -1.1143F));

        head.addOrReplaceChild("barrel_left", CubeListBuilder.create().texOffs(16, 13).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F), PartPose.offset(2.3929F, -1.15F, -6.3857F));

        head.addOrReplaceChild("barrel_right", CubeListBuilder.create().texOffs(16, 13).mirror().addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F).mirror(false), PartPose.offset(-2.3571F, -1.15F, -6.3857F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, BoomstickItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.shootAnimationState, BoomstickAnimations.SHOOT, ageInTicks);
        this.animate(entity, item.reloadAnimationState, BoomstickAnimations.RELOAD, ageInTicks);
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
