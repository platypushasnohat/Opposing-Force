package com.barl_inc.opposing_force.entity.misc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

public class SuperLightningBolt extends LightningBolt {

    public SuperLightningBolt(EntityType<? extends SuperLightningBolt> entityType, Level level) {
        super(entityType, level);
    }
}