package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OFCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OpposingForce.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OPPOSING_FORCE_TAB = CREATIVE_MODE_TAB.register("opposing_force_creative_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(OFItems.POT_OF_GREEN.get()))
                    .title(Component.translatable("creative_tab.opposing_force"))
                    .displayItems((parameters, output) -> {
                        // spawn eggs
                        output.accept(OFItems.BEWILDER_SPAWN_EGG);
                        output.accept(OFItems.DICER_SPAWN_EGG);
                        output.accept(OFItems.FILLET_SPAWN_EGG);
                        output.accept(OFItems.FURBALL_SPAWN_EGG);
                        output.accept(OFItems.GNAT_SPAWN_EGG);
                        output.accept(OFItems.GUSHER_SPAWN_EGG);
                        output.accept(OFItems.MOTIF_SPAWN_EGG);
                        output.accept(OFItems.MUSHY_SPAWN_EGG);
                        output.accept(OFItems.SCORCHER_SPAWN_EGG);
                        output.accept(OFItems.TARANTULA_SPAWN_EGG);
                        output.accept(OFItems.TERROR_SPAWN_EGG);

                        // mob items
                        output.accept(OFItems.CHITIN);
                        output.accept(OFItems.CHITIN_LANCE);

                        output.accept(OFItems.LASER_FOCUS);
                        output.accept(OFItems.POWER_CELL);
                        output.accept(OFItems.BLASTER);
                        output.accept(OFItems.TRI_BLASTER);
                        output.accept(OFItems.SCATTER_BLASTER);
                        output.accept(OFItems.LASER_BLADE);
                        output.accept(OFItems.MUSIC_DISC_SLAYSER);

                        output.accept(OFItems.FILLET_BUCKET);

                        output.accept(OFItems.ACID_CHARGE);

                        output.accept(OFItems.MYSTIC_POT);
                        output.accept(OFItems.LEITMOTIF);

                        output.accept(OFItems.TERROR_LEG);
                        output.accept(OFItems.FRIED_TERROR_LEG);

                        // misc tools and armor (non individual weapons)
                        output.accept(OFItems.EMERALD_SWORD);
                        output.accept(OFItems.EMERALD_SHOVEL);
                        output.accept(OFItems.EMERALD_PICKAXE);
                        output.accept(OFItems.EMERALD_AXE);
                        output.accept(OFItems.EMERALD_HOE);
                        output.accept(OFItems.EMERALD_MASK);
                        output.accept(OFItems.EMERALD_CHESTPLATE);
                        output.accept(OFItems.EMERALD_LEGGINGS);
                        output.accept(OFItems.EMERALD_BOOTS);

                        output.accept(OFItems.LAPIS_LAZULI_SWORD);
                        output.accept(OFItems.LAPIS_LAZULI_SHOVEL);
                        output.accept(OFItems.LAPIS_LAZULI_PICKAXE);
                        output.accept(OFItems.LAPIS_LAZULI_AXE);
                        output.accept(OFItems.LAPIS_LAZULI_HOE);

                        output.accept(OFItems.MOON_SHOES);

                        //misc weapons
                        output.accept(OFItems.BOOMSTICK);
                        output.accept(OFItems.CRACKSHOT);
                        output.accept(OFItems.GORLOFF);
                        output.accept(OFItems.JAEGER);

                        //misc bullets
                        output.accept(OFItems.STONE_BULLET);
                        output.accept(OFItems.IRON_BULLET);
                        output.accept(OFItems.PRECISION_BULLET);

                        //misc mana

                        // crafting materials
                        output.accept(OFItems.FIRING_MECHANISM);

                        // trinkets
                        // common
                        output.accept(OFItems.BEETLE_HUSK);
                        output.accept(OFItems.FIERY_CARAPACE);
                        output.accept(OFItems.LUCKY_TACKLEBOX);
                        output.accept(OFItems.MUNITIONS_BOX);
                        output.accept(OFItems.RANGER_MANUAL);
                        output.accept(OFItems.THORNY_BRACELET);

                        // uncommon
                        output.accept(OFItems.BLACK_BELT);
                        output.accept(OFItems.ENCHANTED_CHISEL);
                        output.accept(OFItems.MAGNETISED_ENHANCEMENTS);
                        output.accept(OFItems.RELIC_FEATHER);
                        output.accept(OFItems.SHARPSHOOTER_COIN);
                        output.accept(OFItems.SLIMY_SHIELD);
                        output.accept(OFItems.STALKERS_GLOVES);
                        output.accept(OFItems.URANIUM_KNUCKLES);

                        // rare
                        output.accept(OFItems.BLOOD_AMULET);
                        output.accept(OFItems.POT_OF_GREEN);
                        output.accept(OFItems.TEAR_OF_AQUARIUS);

                        // permanent upgrades
                        output.accept(OFItems.CRYSTALIZED_HEART);
                    })
                    .build());
}
