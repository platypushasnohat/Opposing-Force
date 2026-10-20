package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.*;
import com.barl_inc.opposing_force.item.curios.*;
import com.barl_inc.opposing_force.item.ranged.*;
import com.barl_inc.opposing_force.item.ranged.ammo.BulletComponent;
import com.barl_inc.opposing_force.item.ranged.ammo.PowerCellItem;
import com.platypushasnohat.sinew.item.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ArmorItem.Type;
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
    public static final DeferredItem<Item> TERROR_LEG = registerItem("terror_leg", () -> new Item(new Item.Properties().food(OFFoodProperties.TERROR_LEG)));
    public static final DeferredItem<Item> FRIED_TERROR_LEG = registerItem("fried_terror_leg", () -> new Item(new Item.Properties().food(OFFoodProperties.FRIED_TERROR_LEG)));

    public static final DeferredItem<Item> TARANTULA_SPAWN_EGG = registerSpawnEggItem("tarantula", OFEntities.TARANTULA, 0x4f4235, 0xedb24c);

    public static final DeferredItem<Item> MUSHY_SPAWN_EGG = registerSpawnEggItem("mushy", OFEntities.MUSHY, 0xe3552b, 0x110701);

    public static final DeferredItem<Item> MOTIF_SPAWN_EGG = registerSpawnEggItem("motif", OFEntities.MOTIF, 0xddd6b1, 0xc15e3e);
    public static final DeferredItem<Item> MYSTIC_POT = registerItemNoLang("mystic_pot", () -> new MysticPotItem(OFBlocks.MYSTIC_POT.get(), new Item.Properties()));
    public static final DeferredItem<Item> LEITMOTIF = registerItem("leitmotif", () -> new LeitmotifItem(new Item.Properties()));

    public static final DeferredItem<Item> OCTOVINE_SPAWN_EGG = registerSpawnEggItem("octovine", OFEntities.OCTOVINE, 0x674d2f, 0xaa2110);
    public static final DeferredItem<Item> FILLET_SPAWN_EGG = registerSpawnEggItem("fillet", OFEntities.FILLET, 0x470e2f, 0xff6e1f);
    public static final DeferredItem<Item> FILLET_BUCKET = registerItemNoLang("fillet_bucket", () -> new FilletBucketItem(new Item.Properties()));

    //materials
    public static final DeferredItem<Item> FIRING_MECHANISM = registerItem("firing_mechanism", () -> new Item(new Item.Properties()));

    //misc items
    public static final DeferredItem<Item> CRACKSHOT = registerItem("crackshot", () -> new CrackshotItem(new Item.Properties()));
    public static final DeferredItem<Item> JAEGER = registerItem("jaeger", () -> new JaegerItem(new Item.Properties()));
    public static final DeferredItem<Item> BOOMSTICK = registerItem("boomstick", () -> new BoomstickItem(new Item.Properties()));
    public static final DeferredItem<Item> GORLOFF = registerItem("gorloff", () -> new GorloffItem(new Item.Properties()));

    public static final DeferredItem<Item> EMERALD_SWORD = registerItem("emerald_sword", () -> new SinewSwordItem(OFToolDefinitions.EMERALD, 3.0F, -2.4F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_PICKAXE = registerItem("emerald_pickaxe", () -> new SinewPickaxeItem(OFToolDefinitions.EMERALD, 1.0F, -2.8F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_AXE = registerItem("emerald_axe", () -> new SinewAxeItem(OFToolDefinitions.EMERALD, 5.0F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_SHOVEL = registerItem("emerald_shovel", () -> new SinewShovelItem(OFToolDefinitions.EMERALD, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_HOE = registerItem("emerald_hoe", () -> new SinewHoeItem(OFToolDefinitions.EMERALD, -3.0F, 0.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> EMERALD_MASK = registerItem("emerald_mask", () -> new SkinLayerHidingArmorItem(Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.HELMET.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));
    public static final DeferredItem<Item> EMERALD_CHESTPLATE = registerItem("emerald_chestplate", () -> new SkinLayerHidingArmorItem(Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.CHESTPLATE.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));
    public static final DeferredItem<Item> EMERALD_LEGGINGS = registerItem("emerald_leggings", () -> new SkinLayerHidingArmorItem(Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.LEGGINGS.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));
    public static final DeferredItem<Item> EMERALD_BOOTS = registerItem("emerald_boots", () -> new SkinLayerHidingArmorItem(Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.BOOTS.getDurability(33)), OFArmorDefinitions.EMERALD_ARMOR));

    public static final DeferredItem<Item> LAPIS_LAZULI_SWORD = registerItem("lapis_lazuli_sword", () -> new SinewSwordItem(OFToolDefinitions.LAPIS_LAZULI, 3.0F, -2.4F, new Item.Properties()));
    public static final DeferredItem<Item> LAPIS_LAZULI_PICKAXE = registerItem("lapis_lazuli_pickaxe", () -> new SinewPickaxeItem(OFToolDefinitions.LAPIS_LAZULI, 1.0F, -2.8F, new Item.Properties()));
    public static final DeferredItem<Item> LAPIS_LAZULI_AXE = registerItem("lapis_lazuli_axe", () -> new SinewAxeItem(OFToolDefinitions.LAPIS_LAZULI, 6.0F, -3.1F, new Item.Properties()));
    public static final DeferredItem<Item> LAPIS_LAZULI_SHOVEL = registerItem("lapis_lazuli_shovel", () -> new SinewShovelItem(OFToolDefinitions.LAPIS_LAZULI, 1.5F, -3.0F, new Item.Properties()));
    public static final DeferredItem<Item> LAPIS_LAZULI_HOE = registerItem("lapis_lazuli_hoe", () -> new SinewHoeItem(OFToolDefinitions.LAPIS_LAZULI, 0.0F, -1.0F, new Item.Properties()));

    public static final DeferredItem<Item> MOON_SHOES = registerItem("moon_shoes", () -> new MoonShoesItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(Type.BOOTS.getDurability(25))));

    //misc ammo
    public static final DeferredItem<Item> SHOT_STONE_BULLET = registerItem("shot_stone_bullet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHOT_IRON_BULLET = registerItem("shot_iron_bullet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHOT_PRECISION_BULLET = registerItem("shot_precision_bullet", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STONE_BULLET = registerItem("stone_bullet", () -> new Item(new Item.Properties().component(OFDataComponents.BULLET.get(), new BulletComponent(SHOT_STONE_BULLET.toStack(), 0.8F, 1.2F, 0.8F))));
    public static final DeferredItem<Item> IRON_BULLET = registerItem("iron_bullet", () -> new Item(new Item.Properties().component(OFDataComponents.BULLET.get(), new BulletComponent(SHOT_IRON_BULLET.toStack(), 1.0F, 1.0F, 1.0F))));
    public static final DeferredItem<Item> PRECISION_BULLET = registerItem("precision_bullet", () -> new Item(new Item.Properties().component(OFDataComponents.BULLET.get(), new BulletComponent(SHOT_PRECISION_BULLET.toStack(), 1.2F, 0.8F, 1.2F))));

    //trinket
    public static final DeferredItem<Item> RELIC_FEATHER = registerItem("relic_feather", () -> new RelicFeatherItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1), OFCurioDefinitions.RELIC_FEATHER));
    public static final DeferredItem<Item> BEETLE_HUSK = registerItem("beetle_husk", () -> new CurioItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.BEETLE_HUSK));
    public static final DeferredItem<Item> ENCHANTED_CHISEL = registerItem("enchanted_chisel", () -> new CurioItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1), OFCurioDefinitions.ENCHANTED_CHISEL));
    public static final DeferredItem<Item> POT_OF_GREEN = registerItem("pot_of_green", () -> new CurioItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1), OFCurioDefinitions.POT_OF_GREEN));
    public static final DeferredItem<Item> THORNY_BRACELET = registerItem("thorny_bracelet", () -> new CurioItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.THORNY_BRACELET));
    public static final DeferredItem<Item> RANGER_MANUAL = registerItem("ranger_manual", () -> new CurioItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.RANGER_MANUAL));
    public static final DeferredItem<Item> SHARPSHOOTER_COIN = registerItem("sharpshooter_coin", () -> new CurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), OFCurioDefinitions.SHARPSHOOTER_COIN));
    public static final DeferredItem<Item> TEAR_OF_AQUARIUS = registerItem("tear_of_aquarius", () -> new CurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), OFCurioDefinitions.TEAR_OF_AQUARIUS));
    public static final DeferredItem<Item> BLACK_BELT = registerItem("black_belt", () -> new CurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), OFCurioDefinitions.BLACK_BELT));
    public static final DeferredItem<Item> BLOOD_AMULET = registerItem("blood_amulet", () -> new CurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), OFCurioDefinitions.BLOOD_AMULET));
    public static final DeferredItem<Item> FIERY_CARAPACE = registerItem("fiery_carapace", () -> new CurioItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.FIERY_CARAPACE));
    public static final DeferredItem<Item> URANIUM_KNUCKLES = registerItem("uranium_knuckles", () -> new CurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), OFCurioDefinitions.URANIUM_KNUCKLES));
    public static final DeferredItem<Item> MAGNETISED_ENHANCEMENTS = registerItem("magnetised_enhancements", () -> new MagnetisedEnhancementsItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), OFCurioDefinitions.MAGNETISED_ENHANCEMENTS));
    public static final DeferredItem<Item> LUCKY_TACKLEBOX = registerItem("lucky_tacklebox", () -> new CurioItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.LUCKY_TACKLEBOX));
    public static final DeferredItem<Item> SLIMY_SHIELD = registerItem("slimy_shield", () -> new CurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), OFCurioDefinitions.SLIMY_SHIELD));
    public static final DeferredItem<Item> STALKERS_GLOVES = registerItemNoLang("stalkers_gloves", () -> new CurioItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.STALKERS_GLOVES));
    public static final DeferredItem<Item> MUNITIONS_BOX = registerItem("munitions_box", () -> new CurioItem(new Item.Properties().stacksTo(1), OFCurioDefinitions.MUNITIONS_BOX));

    //Permanent Upgrades
    public static final DeferredItem<Item> CRYSTALIZED_HEART = registerItem("crystalized_heart", () -> new CrystalizedHeartItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).food(OFFoodProperties.CRYSTALIZED_HEART)));

    public static final DeferredItem<Item> GREEN_MILK = registerItemNoLang("green_milk", () -> new MilkBucketItem(new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

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