package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.client.render.entity.layer.SpectralShieldLayer;
import com.barl_inc.opposing_force.client.render.item.SpearAnimations;
import com.barl_inc.opposing_force.client.render.item.TachiAnimations;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    private void opposingForce$renderArmWithItem(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (SpectralShieldLayer.isShielding(entity)) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", shift = At.Shift.AFTER), method = "renderArmWithItem")
    private void opposingForce$animateHeldItem(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (((ItemInHandLayer<?, ?>) (Object) this).getParentModel() instanceof HumanoidModel<?> model) {
            SpearAnimations.thirdPersonItem(model, entity, arm, poseStack);
        }
        TachiAnimations.thirdPersonItem(entity, arm, poseStack);
    }
}