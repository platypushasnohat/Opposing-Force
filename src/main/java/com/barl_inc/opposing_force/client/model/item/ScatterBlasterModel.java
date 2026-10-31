package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.item.animation.BlasterAnimations;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.barl_inc.opposing_force.item.ranged.ScatterBlasterItem;
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

public class ScatterBlasterModel extends GunModel<ScatterBlasterItem> {

    public static final ScatterBlasterModel INSTANCE = new ScatterBlasterModel(createBodyLayer().bakeRoot());
    public static final ResourceLocation TEXTURE = OpposingForce.location("textures/item/scatter_blaster.png");
    public static final ResourceLocation GLOW_TEXTURE = OpposingForce.location("textures/item/scatter_blaster_glow.png");

    private final ModelPart root;
    private final ModelPart particle;

    public ScatterBlasterModel(ModelPart root) {
        this.root = root.getChild("root");
        this.particle = this.root.getChild("particle");
    }

    @Override
    public void setupAnim(Entity entity, ScatterBlasterItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(stack, GunItem.SHOOT_ANIMATION, BlasterAnimations.SCATTER_BLASTER_SHOOT);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static void renderItem(Player player, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float ageInTicks) {
        poseStack.pushPose();
        INSTANCE.setupAnim(player, (ScatterBlasterItem) stack.getItem(), stack, displayContext, ageInTicks);
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
                    new Vector3f(0.0F, 1.0F, 0.0F)
            );
        }
        poseStack.popPose();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(22, 14).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(1.0F, -2.75F, -8.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(39, 22).addBox(4.0F, -2.75F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 22).addBox(5.0F, -2.75F, -1.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 22).addBox(-3.0F, -2.75F, -0.025F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(19, 46).addBox(2.0F, -0.75F, -0.025F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(19, 43).addBox(-2.0F, -0.75F, -0.025F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(26, 46).addBox(-2.0F, -0.75F, -0.025F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 0).addBox(-5.0F, -2.75F, -1.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(37, 0).addBox(-5.0F, -2.75F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -2.75F, -8.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(12, 35).addBox(-0.5F, 1.0F, -0.025F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-1, 45).addBox(-0.5F, -1.0F, -0.025F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 5).addBox(0.0F, 1.0F, -3.025F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-0.5F, -1.0F, 2.975F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(14, 26).addBox(-0.5F, 0.0F, 4.975F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(14, 26).addBox(-0.5F, 0.0F, 8.975F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 25).addBox(-0.5F, 3.0F, 4.975F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(10, 25).addBox(-0.5F, 0.0F, 4.975F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 29).addBox(3.0F, -4.75F, -8.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(0.0F, -7.75F, -2.0F, 0.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(18, 29).addBox(-3.0F, -4.75F, -8.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(1.5F, -2.25F, -13.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 10).addBox(2.5F, -1.0F, -10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 38).addBox(1.5F, -2.0F, -9.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(18, 38).mirror().addBox(-4.5F, -2.0F, -9.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 10).mirror().addBox(-3.5F, -1.0F, -10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 35).mirror().addBox(-4.5F, -2.25F, -13.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 20.0F, 2.0F));

        root.addOrReplaceChild("particle", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -16.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
