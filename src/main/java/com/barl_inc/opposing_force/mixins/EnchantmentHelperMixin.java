package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getDamageProtection", at = @At(value = "RETURN"), cancellable = true)
    private static void opposingForce$fieryCarapaceProtection(ServerLevel level, LivingEntity entity, DamageSource source, CallbackInfoReturnable<Float> cir) {
        if (source.is(DamageTypeTags.IS_FIRE) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && CuriosApi.getCuriosInventory(entity).map(inventory -> inventory.isEquipped(OFItems.FIERY_CARAPACE.get())).orElse(false)) {
            cir.setReturnValue(cir.getReturnValue() + 2.0F);
        }
    }
}
