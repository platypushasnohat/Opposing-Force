package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.registry.OFEnchantments;
import com.barl_inc.opposing_force.tags.OFMobEffectTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    //so i made magic prot reduce 2.5% of negative pot duration per piece, so full set at lvl 4 would be 40% decreased time
    @ModifyVariable(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "HEAD"), argsOnly = true)
    private MobEffectInstance opposingForce$magicProtectionDuration(MobEffectInstance instance) {
        LivingEntity entity = (LivingEntity) (Object) this;
        MobEffectInstance result = instance;
        if (!entity.level().isClientSide() && instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL
                && instance.getDuration() > 40
                && !instance.getEffect().is(OFMobEffectTags.IMMUNE_TO_DURATION_REDUCTION)) {
            Holder<Enchantment> magicProtection = entity.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(OFEnchantments.MAGIC_PROTECTION);
            int level = 0;
            for (ItemStack stack : entity.getArmorSlots()) {
                level += EnchantmentHelper.getTagEnchantmentLevel(magicProtection, stack);
            }
            if (level > 0) {
                result = new MobEffectInstance(instance.getEffect(), Math.round(instance.getDuration() * (1.0F - 0.025F * level)), instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
            }
        }
        return result;
    }
}
