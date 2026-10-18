package com.barl_inc.opposing_force.item.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BulletStats(float damage, float inaccuracy, float cooldown, float velocity) {

    public static final Codec<BulletStats> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("damage", 1.0F).forGetter(BulletStats::damage),
            Codec.FLOAT.optionalFieldOf("inaccuracy", 1.0F).forGetter(BulletStats::inaccuracy),
            Codec.FLOAT.optionalFieldOf("cooldown", 1.0F).forGetter(BulletStats::cooldown),
            Codec.FLOAT.optionalFieldOf("velocity", 1.0F).forGetter(BulletStats::velocity)
    ).apply(instance, BulletStats::new));
}
