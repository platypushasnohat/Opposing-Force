package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFArmorDefinitions;
import com.platypushasnohat.sinew.item.SkinLayerHidingArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class MoonShoesItem extends SkinLayerHidingArmorItem {

    public MoonShoesItem(Properties properties) {
        super(Type.BOOTS, properties, OFArmorDefinitions.MOON_SHOES);
    }

    @Override
    @Nullable
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return OpposingForce.location("textures/models/armor/moon_shoes.png");
    }
}
