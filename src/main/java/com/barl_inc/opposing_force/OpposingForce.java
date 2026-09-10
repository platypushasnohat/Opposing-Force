package com.barl_inc.opposing_force;

import com.barl_inc.opposing_force.datagen.client.OFItemModelProvider;
import com.barl_inc.opposing_force.datagen.client.OFLanguageProvider;
import com.barl_inc.opposing_force.datagen.client.OFSoundDefinitionsProvider;
import com.barl_inc.opposing_force.datagen.server.OFBiomeTagsProvider;
import com.barl_inc.opposing_force.datagen.server.OFBlockTagsProvider;
import com.barl_inc.opposing_force.datagen.server.OFDatapackBuiltinEntriesProvider;
import com.barl_inc.opposing_force.datagen.server.OFItemTagsProvider;
import com.barl_inc.opposing_force.registry.*;
import com.barl_inc.opposing_force.utils.ClientProxy;
import com.barl_inc.opposing_force.utils.CommonProxy;
import com.platypushasnohat.sinew.Sinew;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Mod(OpposingForce.MOD_ID)
public class OpposingForce {

    public static final String MOD_ID = "opposing_force";
    public static final CommonProxy PROXY = Sinew.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path.toLowerCase(Locale.ROOT));
    }

    public OpposingForce(IEventBus modEventBus, ModContainer modContainer) {
        OFEntities.ENTITY_TYPE.register(modEventBus);
        OFItems.ITEM.register(modEventBus);
        OFParticleTypes.PARTICLE_TYPE.register(modEventBus);
        OFSoundEvents.SOUND_EVENT.register(modEventBus);
        OFCreativeTabs.CREATIVE_MODE_TAB.register(modEventBus);
        modEventBus.addListener(this::dataSetup);
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();
        boolean client = event.includeClient();

        OFDatapackBuiltinEntriesProvider datapackEntries = new OFDatapackBuiltinEntriesProvider(output, provider);
        generator.addProvider(server, datapackEntries);
        provider = datapackEntries.getRegistryProvider();

        OFBlockTagsProvider blockTags = new OFBlockTagsProvider(output, provider, helper);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new OFItemTagsProvider(output, provider, blockTags.contentsGetter(), helper));
        generator.addProvider(server, new OFBiomeTagsProvider(output, provider, helper));

        generator.addProvider(client, new OFItemModelProvider(output, helper));
        generator.addProvider(client, new OFSoundDefinitionsProvider(output, helper));
        generator.addProvider(client, new OFLanguageProvider(output));
    }
}
