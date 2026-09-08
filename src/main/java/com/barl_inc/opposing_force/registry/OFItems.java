package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.BlasterItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OFItems {

    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(OpposingForce.MOD_ID);

    public static List<DeferredItem<? extends Item>> ITEM_TRANSLATIONS = new ArrayList<>();

    public static final DeferredItem<Item> DICER_SPAWN_EGG = registerSpawnEggItem("dicer", OFEntities.DICER, 0x0a020a, 0x8943ff);
    public static final DeferredItem<Item> BLASTER = registerItem("blaster", () -> new BlasterItem(new Item.Properties()));
    public static final DeferredItem<Item> LASER_FOCUS = registerItem("laser_focus", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MUSIC_DISC_SLAYSER = registerItemNoLang("music_disc_slayser", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(OFJukeboxSongs.SLAYSER)));

    private static <I extends Item> DeferredItem<I> registerItem(String name, Supplier<? extends I> supplier) {
        DeferredItem<I> item = ITEM.register(name, supplier);
        ITEM_TRANSLATIONS.add(item);
        return item;
    }

    private static <I extends Item> DeferredItem<I> registerItemNoLang(String name, Supplier<? extends I> supplier) {
        return ITEM.register(name, supplier);
    }

    private static DeferredItem<Item> registerSpawnEggItem(String name, Supplier<? extends EntityType<? extends Mob>> type, int baseColor, int spotColor) {
        return registerItem(name + "_spawn_egg", () -> new DeferredSpawnEggItem(type, baseColor, spotColor, new Item.Properties()));
    }
}