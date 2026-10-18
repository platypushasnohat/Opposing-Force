package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.base.BulletStats;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class OFDataMaps {

    public static final DataMapType<Item, BulletStats> BULLET_STATS = DataMapType.builder(OpposingForce.location("bullet_stats"), Registries.ITEM, BulletStats.CODEC).synced(BulletStats.CODEC, false).build();

    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(BULLET_STATS);
    }
}
