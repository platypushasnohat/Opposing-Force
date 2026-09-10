package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.platypushasnohat.sinew.utils.RandomMessageDamageSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class OFDamageTypes {

    public static final ResourceKey<DamageType> LASER = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("laser"));
    public static final ResourceKey<DamageType> LASER_BOLT = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("laser_bolt"));
    public static final ResourceKey<DamageType> LASER_BLADE = ResourceKey.create(Registries.DAMAGE_TYPE, OpposingForce.location("laser_blade"));

    public static DamageSource causeLaserDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(LASER), source, 1);
    }

    public static DamageSource causeLaserBoltDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(LASER_BOLT), source, 1);
    }

    public static DamageSource causeLaserBladeDamage(RegistryAccess registryAccess, Entity source) {
        return new RandomMessageDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(LASER_BLADE), source, 1);
    }
}
