package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.item.base.SpearAnimatedItem;
import com.barl_inc.opposing_force.registry.OFAttributes;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(at = @At("RETURN"), method = "getCurrentSwingDuration", cancellable = true)
    private void opposingForce$getCurrentSwingDuration(CallbackInfoReturnable<Integer> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getMainHandItem().getItem() instanceof SpearAnimatedItem item) {
            cir.setReturnValue(item.getStabDuration());
        }
    }

    @ModifyVariable(at = @At("HEAD"), method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", argsOnly = true)
    private MobEffectInstance opposingForce$scaleElectrifiedDuration(MobEffectInstance instance) {
        LivingEntity entity = (LivingEntity) (Object) this;
        MobEffectInstance result = instance;
        if (instance.is(OFMobEffects.ELECTRIFIED) && !instance.isInfiniteDuration() && entity.getAttributes().hasAttribute(OFAttributes.ELECTRIFIED_TIME)) {
            int duration = Math.max(1, Mth.floor(instance.getDuration() * entity.getAttributeValue(OFAttributes.ELECTRIFIED_TIME)));
            result = new MobEffectInstance(instance.getEffect(), duration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
        }
        return result;
    }
}