package com.barl_inc.opposing_force.registry;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class OFClientCompat {

    public static void registerCompat() {
        registerItemProperties();
    }

    public static void registerItemProperties() {
        ItemProperties.register(OFItems.CHITIN_LANCE.get(), ResourceLocation.withDefaultNamespace("charging"), (stack, level, living, j) ->  {
            if (living == null) {
                return 0.0F;
            } else {
                return living.getUseItem() == stack ? 1.0F : 0.0F;
            }
        });
    }
}
