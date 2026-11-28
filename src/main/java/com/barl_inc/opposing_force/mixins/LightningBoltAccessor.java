package com.barl_inc.opposing_force.mixins;

import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightningBolt.class)
public interface LightningBoltAccessor {

    @Accessor("visualOnly")
    boolean opposingForce$isVisualOnly();
}
