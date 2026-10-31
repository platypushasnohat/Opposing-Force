package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.item.animation.BlasterAnimations;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.barl_inc.opposing_force.item.ranged.TriBlasterItem;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.sinew.client.utils.ClientItemData;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class TriBlasterModel extends GunModel<TriBlasterItem> {

    public static final TriBlasterModel INSTANCE = new TriBlasterModel(createBodyLayer().bakeRoot());
    public static final ResourceLocation TEXTURE = OpposingForce.location("textures/item/tri_blaster.png");
    public static final ResourceLocation GLOW_TEXTURE = OpposingForce.location("textures/item/tri_blaster_glow.png");

    private final ModelPart root;
    private final ModelPart particle;

    public TriBlasterModel(ModelPart root) {
        this.root = root.getChild("root");
        this.particle = this.root.getChild("particle");
    }

    @Override
    public void setupAnim(Entity entity, TriBlasterItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(stack, GunItem.SHOOT_ANIMATION, BlasterAnimations.BLASTER_SHOOT);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static void renderItem(Player player, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float ageInTicks) {
        poseStack.pushPose();
        INSTANCE.setupAnim(player, (TriBlasterItem) stack.getItem(), stack, displayContext, ageInTicks);
        INSTANCE.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(TEXTURE)), packedLight, packedOverlay, 0xFFFFFF);
        INSTANCE.renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(GLOW_TEXTURE)), LightTexture.FULL_BRIGHT, packedOverlay, 0xFFFFFF);
        if (stack.hasFoil()) {
            INSTANCE.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutout(TEXTURE), false, true), packedLight, packedOverlay, 0xFFFFFF);
        }
        if (player != null && ClientItemData.hasClientData(stack, GunItem.GUN_FLASH)) {
            INSTANCE.renderGunFlashParticle(stack,
                    player,
                    poseStack,
                    displayContext,
                    OFParticleTypes.LASER_FLASH.get(),
                    INSTANCE.particle,
                    new Vector3f(0.0F, 1.0F, -0.75F)
            );
        }
        poseStack.popPose();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -13.0F, 4.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(28, 36).addBox(-1.5F, 1.0F, -5.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 20).addBox(-1.5F, -1.5F, -18.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(36, 26).addBox(-2.5F, -2.5F, -21.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 44).addBox(-2.5F, -2.5F, -16.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(42, 42).addBox(-2.5F, -2.5F, -17.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 42).addBox(-1.5F, 1.0F, 3.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 47).addBox(-2.5F, -2.5F, -15.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(37, 11).addBox(-1.5F, -2.0F, -2.0F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(50, 37).addBox(-1.0F, 2.0F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 32).addBox(-1.5F, -2.0F, 3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-1.0F, -5.0F, -6.0F, 0.0F, 3.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(-1.5F, -2.0F, 6.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(51, 8).addBox(-1.5F, -1.0F, 8.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(51, 8).addBox(-1.5F, -1.0F, 10.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(49, 9).addBox(-1.5F, 2.0F, 8.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 9).addBox(-1.5F, -1.0F, 8.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 18.0F, 4.0F));

        root.addOrReplaceChild("particle", CubeListBuilder.create(), PartPose.offset(-1.0F, -1.0F, -24.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}