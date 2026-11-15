package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.scroll.TornScrollVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class OFTornScrolls {

    public static final ResourceKey<Registry<TornScrollVariant>> TORN_SCROLL = ResourceKey.createRegistryKey(OpposingForce.location("torn_scroll"));

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, OpposingForce.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<List<ResourceLocation>>> RECEIVED_SCROLLS = ATTACHMENT_TYPES.register("received_torn_scrolls", () -> AttachmentType.<List<ResourceLocation>>builder(() -> List.of()).serialize(ResourceLocation.CODEC.listOf()).copyOnDeath().build());

    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(TORN_SCROLL, TornScrollVariant.DIRECT_CODEC, TornScrollVariant.NETWORK_CODEC);
    }
}