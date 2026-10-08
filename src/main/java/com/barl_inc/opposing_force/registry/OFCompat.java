package com.barl_inc.opposing_force.registry;

import net.minecraft.world.level.block.DispenserBlock;

public class OFCompat {

    public static void registerCompat() {
        registerDispenserBehaviors();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerProjectileBehavior(OFItems.ACID_CHARGE.get());
        DispenserBlock.registerProjectileBehavior(OFItems.BILE_GLOB.get());
        DispenserBlock.registerProjectileBehavior(OFItems.BILE_BOMB_ARROW.get());
    }
}
