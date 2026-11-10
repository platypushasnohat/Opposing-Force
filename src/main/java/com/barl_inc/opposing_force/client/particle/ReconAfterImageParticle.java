package com.barl_inc.opposing_force.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.platypushasnohat.sinew.client.particle.AfterImageParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ClientHooks;

import javax.annotation.Nullable;

public class ReconAfterImageParticle extends AfterImageParticle {

    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    @Nullable
    private static HumanoidModel<LivingEntity> innerModel;
    @Nullable
    private static HumanoidModel<LivingEntity> outerModel;

    public ReconAfterImageParticle(LivingEntity entity, ClientLevel level, double x, double y, double z, int lifetime, int color, float alpha) {
        super(entity, level, x, y, z, lifetime, color, alpha);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        super.render(consumer, camera, partialTicks);
        if (!this.entity.isInvisible() && Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(this.entity) instanceof LivingEntityRenderer<?, ?> renderer && renderer.getModel() instanceof HumanoidModel<?> humanoid) {
            this.renderArmor(humanoid, camera, partialTicks);
        }
    }

    @SuppressWarnings("unchecked")
    private void renderArmor(HumanoidModel<?> humanoid, Camera camera, float partialTicks) {
        HumanoidModel<LivingEntity> parent = (HumanoidModel<LivingEntity>) humanoid;
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cameraPos = camera.getPosition();
        float bodyYaw = Mth.rotLerp(partialTicks, this.entity.yBodyRotO, this.entity.yBodyRot);

        PoseStack poseStack = new PoseStack();
        poseStack.translate(this.x - cameraPos.x, this.y - cameraPos.y + 1.4D, this.z - cameraPos.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        RenderSystem.setShaderColor((this.color >> 16 & 255) / 255.0F, (this.color >> 8 & 255) / 255.0F, (this.color & 255) / 255.0F, this.alpha);
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = this.entity.getItemBySlot(slot);
            if (stack.getItem() instanceof ArmorItem armor && armor.getEquipmentSlot() == slot) {
                boolean inner = slot == EquipmentSlot.LEGS;
                HumanoidModel<LivingEntity> base = inner ? getInnerModel() : getOuterModel();
                parent.copyPropertiesTo(base);
                setPartVisibility(base, slot);
                Model model = ClientHooks.getArmorModel(this.entity, stack, slot, base);
                for (ArmorMaterial.Layer layer : armor.getMaterial().value().layers()) {
                    ResourceLocation texture = ClientHooks.getArmorTexture(this.entity, stack, layer, inner, slot);
                    model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(texture)), 15728880, OverlayTexture.NO_OVERLAY, -1);
                }
            }
        }
        bufferSource.endBatch();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void setPartVisibility(HumanoidModel<LivingEntity> model, EquipmentSlot slot) {
        model.setAllVisible(false);
        if (slot == EquipmentSlot.HEAD) {
            model.head.visible = true;
            model.hat.visible = true;
        } else if (slot == EquipmentSlot.CHEST) {
            model.body.visible = true;
            model.rightArm.visible = true;
            model.leftArm.visible = true;
        } else if (slot == EquipmentSlot.LEGS) {
            model.body.visible = true;
            model.rightLeg.visible = true;
            model.leftLeg.visible = true;
        } else {
            model.rightLeg.visible = true;
            model.leftLeg.visible = true;
        }
    }

    private static HumanoidModel<LivingEntity> getInnerModel() {
        if (innerModel == null) {
            innerModel = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        }
        return innerModel;
    }

    private static HumanoidModel<LivingEntity> getOuterModel() {
        if (outerModel == null) {
            outerModel = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        }
        return outerModel;
    }
}
