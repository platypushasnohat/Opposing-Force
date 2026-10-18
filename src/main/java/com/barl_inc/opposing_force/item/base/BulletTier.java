package com.barl_inc.opposing_force.item.base;

public enum BulletTier {
    STONE(0.75F, 1.5F, 1.25F, 0.8F),
    IRON(1.0F, 1.0F, 1.0F, 1.0F),
    PRECISION(1.25F, 0.4F, 0.8F, 1.25F);

    private final float damageMultiplier;
    private final float inaccuracyMultiplier;
    private final float cooldownMultiplier;
    private final float velocityMultiplier;

    BulletTier(float damageMultiplier, float inaccuracyMultiplier, float cooldownMultiplier, float velocityMultiplier) {
        this.damageMultiplier = damageMultiplier;
        this.inaccuracyMultiplier = inaccuracyMultiplier;
        this.cooldownMultiplier = cooldownMultiplier;
        this.velocityMultiplier = velocityMultiplier;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public float getInaccuracyMultiplier() {
        return this.inaccuracyMultiplier;
    }

    public float getCooldownMultiplier() {
        return this.cooldownMultiplier;
    }

    public float getVelocityMultiplier() {
        return this.velocityMultiplier;
    }
}
