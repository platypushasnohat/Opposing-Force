package com.barl_inc.opposing_force.item.base;

public interface SpearAnimatedItem {

    int getChargeTime();

    int getStabDuration();

    default float getChargeTurn() {
        return 90.0F;
    }

    default float getThrustDistance() {
        return 0.38F;
    }
}
