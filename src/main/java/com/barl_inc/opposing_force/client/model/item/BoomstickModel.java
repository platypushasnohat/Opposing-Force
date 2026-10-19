package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.item.animation.BoomstickAnimations;
import com.barl_inc.opposing_force.item.ranged.BoomstickItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.item.AnimatedItemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BoomstickModel extends AnimatedItemModel<BoomstickItem> {

    public static final BoomstickModel INSTANCE = new BoomstickModel(createBodyLayer().bakeRoot());
    public static final ResourceLocation TEXTURE = OpposingForce.location("textures/item/boomstick.png");

    private final ModelPart root;

    public BoomstickModel(ModelPart root) {
        this.root = root.getChild("root");
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

    public static void renderItem(Player player, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float ageInTicks) {
        poseStack.pushPose();
        BoomstickModel.INSTANCE.setupAnim(player, (BoomstickItem) stack.getItem(), stack, displayContext, ageInTicks);
        BoomstickModel.INSTANCE.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
        if (stack.hasFoil()) {
            BoomstickModel.INSTANCE.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
        }
        poseStack.popPose();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition gun = root.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 3.0F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(1.5F, 1.0F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 6).addBox(0.5F, -2.0F, -1.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -4.0F, 3.0F));

        PartDefinition head = gun.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 11).addBox(1.5F, -2.025F, -6.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(16, 18).addBox(0.5F, -3.025F, -0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(-2.0F, -1.0F, -4.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).mirror().addBox(-3.5F, -2.025F, -6.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 18).mirror().addBox(-4.5F, -3.025F, -0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, 0.0F, -1.0F));

        head.addOrReplaceChild("barrel_left", CubeListBuilder.create().texOffs(16, 13).addBox(-2.3929F, -1.85F, -0.6143F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.8929F, -1.15F, -6.3857F));

        head.addOrReplaceChild("barrel_right", CubeListBuilder.create().texOffs(16, 13).mirror().addBox(-1.3929F, -1.85F, -0.6143F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.1071F, -1.15F, -6.3857F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
