package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.*;
import com.platypushasnohat.sinew.item.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
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
    public static final DeferredItem<Item> LASER_FOCUS = registerItem("laser_focus", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POWER_CELL = registerItem("power_cell", () -> new PowerCellItem(new Item.Properties()));
    public static final DeferredItem<Item> BLASTER = registerItem("blaster", () -> new BlasterItem(new Item.Properties()));
    public static final DeferredItem<Item> TRI_BLASTER = registerItemNoLang("tri_blaster", () -> new TriBlasterItem(new Item.Properties()));
    public static final DeferredItem<Item> SCATTER_BLASTER = registerItem("scatter_blaster", () -> new ScatterBlasterItem(new Item.Properties()));
    public static final DeferredItem<Item> LASER_BLADE = registerItem("laser_blade", () -> new LaserBladeItem(OFItemTiers.LASER_BLADE, new Item.Properties().attributes(SwordItem.createAttributes(OFItemTiers.LASER_BLADE, 4.0F, -2.4F))));
    public static final DeferredItem<Item> DISABLED_LASER_BLADE = registerItemNoLang("disabled_laser_blade", () -> new DisabledItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MUSIC_DISC_SLAYSER = registerItemNoLang("music_disc_slayser", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(OFJukeboxSongs.SLAYSER)));

    public static final DeferredItem<Item> BEWILDER_SPAWN_EGG = registerSpawnEggItem("bewilder", OFEntities.BEWILDER, 0x653847, 0x1e1014);
    public static final DeferredItem<Item> CHITIN = registerItem("chitin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHITIN_LANCE = registerItem("chitin_lance", () -> new ChitinLanceItem(new Item.Properties()));

    public static final DeferredItem<Item> GUSHER_SPAWN_EGG = registerSpawnEggItem("gusher", OFEntities.GUSHER, 0x1b1d16, 0x8eeb60);
    public static final DeferredItem<Item> ACID_CHARGE = registerItem("acid_charge", () -> new AcidChargeItem(new Item.Properties()));

    public static final DeferredItem<Item> GNAT_SPAWN_EGG = registerSpawnEggItem("gnat", OFEntities.GNAT, 0x403229, 0xc6322c);

    public static final DeferredItem<Item> SCORCHER_SPAWN_EGG = registerSpawnEggItem("scorcher", OFEntities.SCORCHER, 0x99193b, 0xeeb71c);

    public static final DeferredItem<Item> FURBALL_SPAWN_EGG = registerSpawnEggItem("furball", OFEntities.FURBALL, 0x5e564c, 0x1c1714);

    public static final DeferredItem<Item> TERROR_SPAWN_EGG = registerSpawnEggItem("terror", OFEntities.TERROR, 0x0d3625, 0xff0000);

    public static final DeferredItem<Item> TARANTULA_SPAWN_EGG = registerSpawnEggItem("tarantula", OFEntities.TARANTULA, 0x4f4235, 0xedb24c);

    public static final DeferredItem<Item> OCTOVINE_SPAWN_EGG = registerSpawnEggItem("octovine", OFEntities.OCTOVINE, 0x674d2f, 0xaa2110);

    public static final DeferredItem<Item> EMERALD_SWORD = registerItem("emerald_sword", () -> new SinewSwordItem(OFToolDefinitions.EMERALD, 3.0F, -2.4F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_PICKAXE = registerItem("emerald_pickaxe", () -> new SinewPickaxeItem(OFToolDefinitions.EMERALD, 1.0F, -2.8F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_AXE = registerItem("emerald_axe", () -> new SinewAxeItem(OFToolDefinitions.EMERALD, 5.0F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_SHOVEL = registerItem("emerald_shovel", () -> new SinewShovelItem(OFToolDefinitions.EMERALD, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_HOE = registerItem("emerald_hoe", () -> new SinewHoeItem(OFToolDefinitions.EMERALD, -3.0F, 0.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> EMERALD_MASK = registerItem("emerald_mask", () -> new SkinLayerHidingArmorItem(Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.HELMET.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));
    public static final DeferredItem<Item> EMERALD_CHESTPLATE = registerItem("emerald_chestplate", () -> new SkinLayerHidingArmorItem(Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.CHESTPLATE.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));
    public static final DeferredItem<Item> EMERALD_LEGGINGS = registerItem("emerald_leggings", () -> new SkinLayerHidingArmorItem(Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.LEGGINGS.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));
    public static final DeferredItem<Item> EMERALD_BOOTS = registerItem("emerald_boots", () -> new SkinLayerHidingArmorItem(Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.BOOTS.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));

    public static final DeferredItem<Item> MOON_SHOES = registerItem("moon_shoes", () -> new MoonShoesItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.BOOTS.getDurability(25))));

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