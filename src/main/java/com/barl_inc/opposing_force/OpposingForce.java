package com.barl_inc.opposing_force;

import com.barl_inc.opposing_force.registry.OFCreativeTabs;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.Locale;

@Mod(OpposingForce.MOD_ID)
public class OpposingForce {

    public static final String MOD_ID = "opposing_force";

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path.toLowerCase(Locale.ROOT));
    }

    public OpposingForce(IEventBus modEventBus, ModContainer modContainer) {
        OFEntities.ENTITY_TYPE.register(modEventBus);
        OFItems.ITEMS.register(modEventBus);
        OFCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
