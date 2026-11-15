package com.barl_inc.opposing_force.item.melee;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class SacrificialSickleItem extends SwordItem {

    public SacrificialSickleItem(Properties properties) {
        super(Tiers.IRON, properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return SwordItem.createAttributes(Tiers.IRON, 3.0F, -2.4F).withModifierAdded(SinewAttributes.LIFESTEAL, new AttributeModifier(OpposingForce.location("sacrificial_sickle_lifesteal"), 0.2D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        if (!attacker.level().isClientSide && target.isDeadOrDying()) {
            attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(OFTooltips.description("item.opposing_force.sacrificial_sickle.description"));
    }
}
