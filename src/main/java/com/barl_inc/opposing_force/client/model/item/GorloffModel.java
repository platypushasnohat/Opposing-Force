package com.barl_inc.opposing_force.client.model.item;

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
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GorloffModel extends AnimatedItemModel<GorloffItem> {

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

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(20, 29).addBox(-1.0F, -2.025F, -3.0F, 2.0F, 2.0F, 6.0F)
                .texOffs(0, 24).addBox(-2.0F, -6.0F, 2.5F, 4.0F, 6.0F, 6.0F)
                .texOffs(24, 0).addBox(-2.5F, -5.0F, -3.0F, 5.0F, 3.0F, 6.0F)
                .texOffs(12, 42).addBox(-1.5F, -8.0F, -3.0F, 3.0F, 3.0F, 2.0F)
                .texOffs(0, 36).addBox(-2.5F, -9.0F, 3.0F, 5.0F, 8.0F, 1.0F), PartPose.offset(0.5F, 24.0F, 3.5F));

        root.addOrReplaceChild("barrel_spin", CubeListBuilder.create().texOffs(24, 9).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F)
                .texOffs(36, 29).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 3.0F, 3.0F)
                .texOffs(0, 0).addBox(-3.5F, -3.0F, -11.0F, 2.0F, 2.0F, 10.0F)
                .texOffs(0, 12).addBox(1.5F, -3.0F, -11.0F, 2.0F, 2.0F, 10.0F)
                .texOffs(24, 19).addBox(-1.0F, 1.0F, -9.0F, 2.0F, 2.0F, 8.0F)
                .texOffs(12, 37).addBox(-4.5F, -4.0F, -12.0F, 4.0F, 4.0F, 1.0F)
                .texOffs(32, 41).addBox(-2.0F, 0.0F, -10.0F, 4.0F, 4.0F, 1.0F)
                .texOffs(22, 37).addBox(0.5F, -4.0F, -12.0F, 4.0F, 4.0F, 1.0F), PartPose.offset(0.0F, -4.0F, -4.0F));

        PartDefinition trigger = root.addOrReplaceChild("trigger", CubeListBuilder.create(), PartPose.offset(2.0F, -4.5F, 7.0F));

        trigger.addOrReplaceChild("crank", CubeListBuilder.create().texOffs(36, 35).addBox(1.0F, -2.5F, 0.0F, 2.0F, 3.0F, 3.0F), PartPose.ZERO);

        trigger.addOrReplaceChild("spinner", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static final AnimationDefinition SHOOT = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.2658F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("trigger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-720.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("barrel_spin", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -1440.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
