package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.item.base.SpearAnimatedItem;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}
