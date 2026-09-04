package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OFItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OpposingForce.MOD_ID);

    public static List<DeferredItem<? extends Item>> ITEM_TRANSLATIONS = new ArrayList<>();

    public static final DeferredItem<Item> DICER_SPAWN_EGG = registerSpawnEggItem("dicer", OFEntities.DICER, 0x0a020a, 0x8943ff);

    private static <I extends Item> DeferredItem<I> registerItem(String name, Supplier<? extends I> supplier) {
        DeferredItem<I> item = ITEMS.register(name, supplier);
        ITEM_TRANSLATIONS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSpawnEggItem(String name, Supplier<? extends EntityType<? extends Mob>> type, int baseColor, int spotColor) {
        return registerItem(name + "_spawn_egg", () -> new DeferredSpawnEggItem(type, baseColor, spotColor, new Item.Properties()));
    }
}