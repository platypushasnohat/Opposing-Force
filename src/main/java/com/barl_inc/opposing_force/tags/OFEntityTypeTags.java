package com.barl_inc.opposing_force.tags;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class OFEntityTypeTags {
    public static final TagKey<EntityType<?>> PROJECTILE_BYPASSES_BRICK = modEntityTag("projectile_bypasses_brick");

    private static TagKey<EntityType<?>> modEntityTag(String name) {
        return entityTag(OpposingForce.MOD_ID, name);
    }

    private static TagKey<EntityType<?>> commonEntityTag(String name) {
        return entityTag("c", name);
    }

    public static TagKey<EntityType<?>> entityTag(String modId, String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name));
    }
}
