package com.barl_inc.opposing_force.tags;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class OFItemTags {

    public static final TagKey<Item> CONSUMES_BASIC_MANA = modItemTag("consumes_basic_mana");
    public static final TagKey<Item> BASIC_MANA = modItemTag("basic_mana");

    private static TagKey<Item> modItemTag(String name) {
        return itemTag(OpposingForce.MOD_ID, name);
    }

    public static TagKey<Item> itemTag(String modid, String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
