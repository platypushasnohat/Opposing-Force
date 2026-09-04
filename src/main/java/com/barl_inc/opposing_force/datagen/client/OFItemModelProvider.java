package com.barl_inc.opposing_force.datagen.client;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.datagen.client.SinewItemModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class OFItemModelProvider extends SinewItemModelProvider {

    public OFItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, OpposingForce.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof DeferredSpawnEggItem && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(OpposingForce.MOD_ID)) {
                this.withExistingParent(name(item), "item/template_spawn_egg");
            }
        }
    }
}
