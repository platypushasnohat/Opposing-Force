package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OFCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OpposingForce.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TWEAKSLOP_TAB = CREATIVE_MODE_TABS.register("opposing_force_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(OFItems.DICER_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.opposing_force_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(OFItems.DICER_SPAWN_EGG.get());
                    })
                    .build());
}
