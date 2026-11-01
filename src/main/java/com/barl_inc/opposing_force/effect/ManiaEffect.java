package com.barl_inc.opposing_force.effect;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ManiaEffect extends MobEffect {

    public static final ResourceLocation MANIA_MODIFIER = OpposingForce.location("mania_modifier");

    public ManiaEffect() {
        super(MobEffectCategory.HARMFUL, 0xE040E0);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int level) {
        AttributeInstance instance = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance != null) {
            instance.removeModifier(MANIA_MODIFIER);
            instance.addTransientModifier(new AttributeModifier(MANIA_MODIFIER, -0.33F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        return true;
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        AttributeInstance instance = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (instance != null) {
            instance.removeModifier(MANIA_MODIFIER);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration > 0;
    }
}
