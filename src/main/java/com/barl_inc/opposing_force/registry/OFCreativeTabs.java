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
                        output.accept(OFItems.DICER_SPAWN_EGG.get());
                        output.accept(OFItems.DICER_LENS.get());
                        output.accept(OFItems.BLASTER.get());
                    })
                    .build());
}
