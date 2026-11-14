package com.barl_inc.opposing_force.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;

import javax.annotation.Nullable;

public class ReconAfterImageParticle extends StaticAfterImageParticle {

    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    @Nullable
    private static HumanoidModel<LivingEntity> innerModel;
    @Nullable
    private static HumanoidModel<LivingEntity> outerModel;

    public ReconAfterImageParticle(LivingEntity entity, ClientLevel level, double x, double y, double z, int lifetime, int color, float alpha) {
        super(entity, level, x, y, z, lifetime, color, alpha, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void captureExtra(LivingEntity entity, EntityModel<LivingEntity> model, PoseStack poseStack) {
        if (model instanceof HumanoidModel<?> humanoid) {
            HumanoidModel<LivingEntity> parent = (HumanoidModel<LivingEntity>) humanoid;
            for (EquipmentSlot slot : ARMOR_SLOTS) {
                ItemStack stack = entity.getItemBySlot(slot);
                if (stack.getItem() instanceof ArmorItem armor && armor.getEquipmentSlot() == slot) {
                    boolean inner = slot == EquipmentSlot.LEGS;
                    HumanoidModel<LivingEntity> base = inner ? getInnerModel() : getOuterModel();
                    parent.copyPropertiesTo(base);
                    setPartVisibility(base, slot);
                    Model armorModel = ClientHooks.getArmorModel(entity, stack, slot, base);
                    for (ArmorMaterial.Layer layer : armor.getMaterial().value().layers()) {
                        ResourceLocation texture = ClientHooks.getArmorTexture(entity, stack, layer, inner, slot);
                        armorModel.renderToBuffer(poseStack, this.record(RenderType.entityTranslucentEmissive(texture)), 15728880, OverlayTexture.NO_OVERLAY, -1);
                    }
                }
            }
        }
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