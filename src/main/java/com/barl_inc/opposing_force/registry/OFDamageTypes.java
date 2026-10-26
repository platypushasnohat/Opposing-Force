package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.utils.RandomMessageDamageSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class OFDamageTypes {

    public static final ResourceKey<DamageType> LASER = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("laser"));
    public static final ResourceKey<DamageType> LASER_BOLT = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("laser_bolt"));
    public static final ResourceKey<DamageType> LASER_BLADE = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("laser_blade"));
    public static final ResourceKey<DamageType> ACID = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("acid"));
    public static final ResourceKey<DamageType> SCORCH = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("scorch"));
    public static final ResourceKey<DamageType> SAW = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("saw"));
    public static final ResourceKey<DamageType> BULLET = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("bullet"));
    public static final ResourceKey<DamageType> CURSED_RING = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("cursed_ring"));
    public static final ResourceKey<DamageType> SOUL = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("soul"));

    public static DamageSource causeLaserDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(LASER), source, 1);
    }

    public static DamageSource causeLaserBoltDamage(RegistryAccess registryAccess, Entity laserBolt, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(LASER_BOLT), laserBolt, source, 1);
    }

    public static DamageSource causeLaserBladeDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(LASER_BLADE), source, 1);
    }

    public static DamageSource causeAcidDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(ACID), source, 1);
    }

    public static DamageSource causeScorchDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(SCORCH), source, 1);
    }

    public static DamageSource causeSawDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(SAW), source, 1);
    }

    public static DamageSource causeBulletDamage(RegistryAccess registryAccess, Entity bullet, Entity source) {
        return new DamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(BULLET), bullet, source);
    }

    public static DamageSource causeCursedRingDamage(RegistryAccess registryAccess, Entity ring, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(CURSED_RING), ring, source, 2);
    }

    public static DamageSource causeSoulDamage(RegistryAccess registryAccess, Entity soul, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(SOUL), soul, source, 2);
    }
}
