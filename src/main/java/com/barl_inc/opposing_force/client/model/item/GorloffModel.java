package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.ranged.GorloffItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.model.item.AnimatedItemModel;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
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

public class GorloffModel extends AnimatedItemModel<GorloffItem> {

    public static final GorloffModel INSTANCE = new GorloffModel(createBodyLayer().bakeRoot());
    public static final ResourceLocation TEXTURE = OpposingForce.location("textures/item/gorloff.png");

    private final ModelPart root;

    public GorloffModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    @Override
    public void setupAnim(Entity entity, GorloffItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.shootAnimationState, SHOOT, ageInTicks);
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
        GorloffModel.INSTANCE.setupAnim(player, (GorloffItem) stack.getItem(), stack, displayContext, ageInTicks);
        GorloffModel.INSTANCE.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
        if (stack.hasFoil()) {
            GorloffModel.INSTANCE.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
        }
        poseStack.popPose();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition gun = root.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(20, 29).addBox(-1.0F, -2.025F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-2.0F, -6.0F, 3.0F, 4.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-2.5F, -5.0F, -3.0F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 42).addBox(-1.5F, -8.0F, -3.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-2.5F, -9.0F, 3.25F, 5.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 3.5F));

        gun.addOrReplaceChild("barrel_spin", CubeListBuilder.create().texOffs(24, 9).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 29).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.5F, -3.0F, -11.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(1.5F, -3.0F, -11.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(24, 19).addBox(-1.0F, 1.0F, -9.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(12, 37).addBox(-4.5F, -4.0F, -12.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 41).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 37).addBox(0.5F, -4.0F, -12.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -4.0F));

        PartDefinition trigger = gun.addOrReplaceChild("trigger", CubeListBuilder.create(), PartPose.offset(2.0F, -4.5F, 7.0F));

        trigger.addOrReplaceChild("crank", CubeListBuilder.create().texOffs(36, 35).addBox(1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        trigger.addOrReplaceChild("spinner", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, -2.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static final AnimationDefinition SHOOT = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("gun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("barrel_spin", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -1440.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("trigger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-720.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
