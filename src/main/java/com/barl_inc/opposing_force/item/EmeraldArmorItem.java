package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class EmeraldArmorItem extends ArmorItem {

    public EmeraldArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    @Nullable
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        if (slot == EquipmentSlot.LEGS) {
            return OpposingForce.location("textures/models/armor/emerald_layer_2.png");
        } else {
            return OpposingForce.location("textures/models/armor/emerald_layer_1.png");
        }
    }
}
