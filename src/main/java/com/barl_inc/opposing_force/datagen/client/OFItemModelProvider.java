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
                OFItems.SHARPSHOOTER_COIN,
                OFItems.FILLET_BUCKET,
                OFItems.TEAR_OF_AQUARIUS,
                OFItems.BLACK_BELT,
                OFItems.BLOOD_AMULET,
                OFItems.FIERY_CARAPACE,
                OFItems.URANIUM_KNUCKLES,
                OFItems.FIRING_MECHANISM,
                OFItems.CRYSTALIZED_HEART,
                OFItems.MAGNETISED_ENHANCEMENTS,
                OFItems.LUCKY_TACKLEBOX,
                OFItems.SLIMY_SHIELD,
                OFItems.STALKERS_GLOVES,
                OFItems.STONE_BULLET,
                OFItems.IRON_BULLET,
                OFItems.PRECISION_BULLET
        );

        this.handheldItem(
                OFItems.EMERALD_SWORD,
                OFItems.EMERALD_PICKAXE,
                OFItems.EMERALD_AXE,
                OFItems.EMERALD_SHOVEL,
                OFItems.EMERALD_HOE,
                OFItems.ENCHANTED_CHISEL,
                OFItems.LAPIS_LAZULI_SWORD,
                OFItems.LAPIS_LAZULI_PICKAXE,
                OFItems.LAPIS_LAZULI_AXE,
                OFItems.LAPIS_LAZULI_SHOVEL,
                OFItems.LAPIS_LAZULI_HOE,
                OFItems.TERROR_LEG,
                OFItems.FRIED_TERROR_LEG
        );

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof DeferredSpawnEggItem && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(OpposingForce.MOD_ID)) {
                this.withExistingParent(name(item), "item/template_spawn_egg");
            }
        }
    }
}
