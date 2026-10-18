package com.barl_inc.opposing_force.item.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record BulletDefinition(ItemStack renderItem, float damage, float inaccuracy, float cooldown, float velocity) {

    public static final Codec<BulletDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("renderItem").forGetter(BulletDefinition::renderItem),
            Codec.FLOAT.optionalFieldOf("damage", 1.0F).forGetter(BulletDefinition::damage),
            Codec.FLOAT.optionalFieldOf("inaccuracy", 1.0F).forGetter(BulletDefinition::inaccuracy),
            Codec.FLOAT.optionalFieldOf("cooldown", 1.0F).forGetter(BulletDefinition::cooldown),
            Codec.FLOAT.optionalFieldOf("velocity", 1.0F).forGetter(BulletDefinition::velocity)
    ).apply(instance, BulletDefinition::new));
}
