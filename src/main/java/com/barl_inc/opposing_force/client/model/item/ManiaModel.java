package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.item.animation.ManiaAnimations;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.barl_inc.opposing_force.item.magic.ManiaItem;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ManiaModel extends AnimatedItemModel<ManiaItem> {

    public static final ManiaModel INSTANCE = new ManiaModel(createBodyLayer().bakeRoot());
    public static final ResourceLocation TEXTURE = OpposingForce.location("textures/item/mania.png");
    public static final ResourceLocation ELITE_TEXTURE = OpposingForce.location("textures/item/mania_elite.png");

    private final ModelPart root;
    private final ModelPart brain_part;

    public ManiaModel(ModelPart root) {
        this.root = root;
        this.brain_part = root.getChild("body").getChild("brain_part");
    }

    @Override
    public void setupAnim(Entity entity, ManiaItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(stack, GunItem.SHOOT_ANIMATION, ManiaAnimations.SHOOT);
        this.animate(stack, ManiaItem.RELOAD_ANIMATION, ManiaAnimations.RELOAD);
        if (entity instanceof LivingEntity living && living.isUsingItem() && living.getUseItem().getItem() == item) {
            float charge = Math.min(living.getTicksUsingItem() / 40.0F, 1.0F);
            float pulse = 1.0F + (0.04F + charge * 0.08F) * Mth.sin(ageInTicks * (0.6F + charge * 0.6F));
            this.brain_part.xScale *= pulse;
            this.brain_part.yScale *= pulse;
            this.brain_part.zScale *= pulse;
        }
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
        ManiaItem item = (ManiaItem) stack.getItem();
        ResourceLocation texture = TEXTURE;
        if (item.isElite(stack)) {
            texture = ELITE_TEXTURE;
        }
        poseStack.pushPose();
        ManiaModel.INSTANCE.setupAnim(player, item, stack, displayContext, ageInTicks);
        ManiaModel.INSTANCE.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(buffer, RenderType.entityCutoutNoCull(texture), false, stack.hasFoil()), packedLight, packedOverlay, 0xFFFFFF);
        poseStack.popPose();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(42, 28).addBox(-1.0F, -0.125F, -16.575F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -2.125F, -14.575F, 2.0F, 2.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(12, 49).addBox(-1.0F, -0.125F, 9.425F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 49).addBox(-1.0F, -0.125F, 3.425F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-1.0F, 1.875F, 5.425F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 49).addBox(0.0F, -0.125F, -0.575F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 25.125F, 9.575F));

        body.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(0, 28).addBox(-1.5F, -1.5F, -15.0F, 3.0F, 3.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.625F, -3.575F));

        body.addOrReplaceChild("brain_part", CubeListBuilder.create().texOffs(42, 46).addBox(-3.5F, -4.0F, -8.5F, 7.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.125F, -0.975F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
