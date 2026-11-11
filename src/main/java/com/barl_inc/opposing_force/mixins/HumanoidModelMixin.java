package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.client.render.item.SpearAnimations;
import com.barl_inc.opposing_force.client.render.item.TachiAnimations;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "setupAttackAnimation", cancellable = true)
    private void opposingForce$setupAttackAnimation(T entity, float ageInTicks, CallbackInfo ci) {
        HumanoidModel<T> model = (HumanoidModel<T>) (Object) this;
        if (model.attackTime > 0.0F && SpearAnimations.isStabbing(entity)) {
            SpearAnimations.thirdPersonAttackArm(model, entity);
            ci.cancel();
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void opposingForce$setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        TachiAnimations.poseBody((HumanoidModel<T>) (Object) this, entity);
    }
}
