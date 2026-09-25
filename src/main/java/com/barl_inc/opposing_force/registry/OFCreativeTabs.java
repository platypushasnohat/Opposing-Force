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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(OFItems.DICER_SPAWN_EGG.get()))
                    .title(Component.translatable("creative_tab.opposing_force"))
                    .displayItems((parameters, output) -> {
                        output.accept(OFItems.DICER_SPAWN_EGG);
                        output.accept(OFItems.LASER_FOCUS);
                        output.accept(OFItems.BLASTER);
                        output.accept(OFItems.TRI_BLASTER);
                        output.accept(OFItems.SCATTER_BLASTER);
                        output.accept(OFItems.LASER_BLADE);
                        output.accept(OFItems.MUSIC_DISC_SLAYSER);
                        output.accept(OFItems.EMERALD_SWORD);
                        output.accept(OFItems.EMERALD_SHOVEL);
                        output.accept(OFItems.EMERALD_PICKAXE);
                        output.accept(OFItems.EMERALD_AXE);
                        output.accept(OFItems.EMERALD_HOE);
                        output.accept(OFItems.EMERALD_MASK);
                        output.accept(OFItems.EMERALD_CHESTPLATE);
                        output.accept(OFItems.EMERALD_LEGGINGS);
                        output.accept(OFItems.EMERALD_BOOTS);
                        output.accept(OFItems.MOON_SHOES);
                    })
                    .build());
}
