package com.barl_inc.opposing_force.tags;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class OFEntityTypeTags {
    public static final TagKey<EntityType<?>> PROJECTILE_BYPASSES_BRICK = modEntityTag("projectile_bypasses_brick");

    public static final TagKey<EntityType<?>> SLIMY = modEntityTypeTag("slimy");
    public static final TagKey<EntityType<?>> PIPSQUEAK_CANNOT_SWALLOW = modEntityTypeTag("pipsqueak_cannot_swallow");
    public static final TagKey<EntityType<?>> BUGS = modEntityTypeTag("bugs");

    private static TagKey<EntityType<?>> modEntityTypeTag(String name) {
        return entityTypeTag(OpposingForce.MOD_ID, name);
    }

    public static TagKey<EntityType<?>> entityTypeTag(String modid, String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
