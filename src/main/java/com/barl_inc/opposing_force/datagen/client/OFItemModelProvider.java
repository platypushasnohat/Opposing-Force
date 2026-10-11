package com.barl_inc.opposing_force.datagen.client;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.datagen.client.SinewItemModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class OFItemModelProvider extends SinewItemModelProvider {

    public OFItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, OpposingForce.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        this.generatedItem(
                OFItems.LASER_FOCUS,
                OFItems.MUSIC_DISC_SLAYSER,
                OFItems.EMERALD_MASK,
                OFItems.EMERALD_CHESTPLATE,
                OFItems.EMERALD_LEGGINGS,
                OFItems.EMERALD_BOOTS,
                OFItems.MOON_SHOES,
                OFItems.CHITIN,
                OFItems.POWER_CELL,
                OFItems.ACID_CHARGE,
                OFItems.RELIC_FEATHER,
                OFItems.BEETLE_HUSK,
                OFItems.POT_OF_GREEN,
                OFItems.THORNY_BRACELET,
                OFItems.RANGER_MANUAL,
                OFItems.SHARPSHOOTER_COIN
        );

        this.handheldItem(
                OFItems.EMERALD_SWORD,
                OFItems.EMERALD_PICKAXE,
                OFItems.EMERALD_AXE,
                OFItems.EMERALD_SHOVEL,
                OFItems.EMERALD_HOE,
                OFItems.ENCHANTED_CHISEL
        );

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof DeferredSpawnEggItem && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(OpposingForce.MOD_ID)) {
                this.withExistingParent(name(item), "item/template_spawn_egg");
            }
        }
    }
}
