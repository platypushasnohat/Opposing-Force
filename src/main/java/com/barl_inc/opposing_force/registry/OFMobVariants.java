package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.entity.variant.SinewMobVariant;
import com.platypushasnohat.sinew.entity.variant.SinewMobVariantRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class OFMobVariants {

    public static final ResourceKey<Registry<SinewMobVariant>> ROAMER = SinewMobVariantRegistry.register(OpposingForce.MOD_ID, "roamer");

    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ROAMER, SinewMobVariant.DIRECT_CODEC, SinewMobVariant.DIRECT_CODEC);
    }
}
