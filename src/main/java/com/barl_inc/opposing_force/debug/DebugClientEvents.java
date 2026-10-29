package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.render.item.OFItemExtensions;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = OpposingForce.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DebugClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(OFDebugItems.KNIGHTSOUL.get(), ResourceLocation.withDefaultNamespace("blocking"), (stack, level, living, j) -> {
            if (living == null) {
                return 0.0F;
            } else {
                return living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F;
            }
        }));
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(OFItemExtensions.itemExtensions, OFDebugItems.MINDFUCK.get());
    }
}
