package com.barl_inc.opposing_force.registry.misc;

import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.world.level.block.DispenserBlock;

public class OFCompat {

    public static void registerCompat() {
        registerDispenserBehaviors();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerProjectileBehavior(OFItems.ACID_CHARGE.get());
    }
}
