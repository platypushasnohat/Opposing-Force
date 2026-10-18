package com.barl_inc.opposing_force.item.base;

import net.minecraft.world.item.Item;

public class BulletItem extends Item {

    private final BulletTier tier;

    public BulletItem(BulletTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public BulletTier getTier() {
        return this.tier;
    }
}
