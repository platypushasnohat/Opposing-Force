package com.barl_inc.opposing_force.item.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public abstract class AbstractCurioItem extends Item implements ICurioItem {

    public AbstractCurioItem(Properties properties) {
        super(properties);
    }

    public void addAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, SlotContext context, ItemStack stack) {
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext context, ResourceLocation location, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = LinkedHashMultimap.create();
        this.addAttributeModifiers(map, context, stack);
        return map;
    }

    @Override
    public void onEquipFromUse(SlotContext context, ItemStack stack) {
        LivingEntity entity = context.entity();
        entity.level().playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(entity));
    }

    @Override
    public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
        return true;
    }

    public void addAttributeModifier(Multimap<Holder<Attribute>, AttributeModifier> map, Holder<Attribute> attribute, AttributeModifier modifier) {
        map.put(attribute, modifier);
    }
}
