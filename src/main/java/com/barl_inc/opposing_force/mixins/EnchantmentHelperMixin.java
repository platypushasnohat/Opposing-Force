package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getDamageProtection", at = @At(value = "RETURN"), cancellable = true)
    private static void opposingForce$fieryCarapaceProtection(ServerLevel level, LivingEntity entity, DamageSource source, CallbackInfoReturnable<Float> cir) {
        if (source.is(DamageTypeTags.IS_FIRE) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && opposingForce$isEquipped(entity, OFItems.FIERY_CARAPACE.get())) {
            cir.setReturnValue(cir.getReturnValue() + 2.0F);
        }
    }

    @Inject(method = "getFishingTimeReduction", at = @At(value = "RETURN"), cancellable = true)
    private static void opposingForce$luckyTackleboxLure(ServerLevel level, ItemStack stack, Entity entity, CallbackInfoReturnable<Float> cir) {
        if (opposingForce$isEquipped(entity, OFItems.LUCKY_TACKLEBOX.get())) {
            cir.setReturnValue(cir.getReturnValue() + 5.0F);
        }
    }

    @Inject(method = "getFishingLuckBonus", at = @At(value = "RETURN"), cancellable = true)
    private static void opposingForce$luckyTackleboxLuck(ServerLevel level, ItemStack stack, Entity entity, CallbackInfoReturnable<Integer> cir) {
        if (opposingForce$isEquipped(entity, OFItems.LUCKY_TACKLEBOX.get())) {
            cir.setReturnValue(cir.getReturnValue() + 1);
        }
    }

    @Unique
    private static boolean opposingForce$isEquipped(Entity entity, Item item) {
        return entity instanceof LivingEntity living && CuriosApi.getCuriosInventory(living).map(inventory -> inventory.isEquipped(item)).orElse(false);
    }
}
