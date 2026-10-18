package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.item.base.BulletDefinition;
import com.barl_inc.opposing_force.registry.OFDataMaps;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class OFDataMapProvider extends DataMapProvider {

    public OFDataMapProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void gather(Provider provider) {
        this.builder(OFDataMaps.BULLET_DEFINITION)
                .add(OFItems.STONE_BULLET, new BulletDefinition(OFItems.SHOT_STONE_BULLET.toStack(),
                        0.75F, 1.5F, 1.25F, 0.8F), false)

                .add(OFItems.IRON_BULLET, new BulletDefinition(OFItems.SHOT_IRON_BULLET.toStack(),
                        1.0F, 1.0F, 1.0F, 1.0F), false)

                .add(OFItems.PRECISION_BULLET, new BulletDefinition(OFItems.SHOT_PRECISION_BULLET.toStack(),
                        1.25F, 0.4F, 0.8F, 1.25F), false);
    }
}
