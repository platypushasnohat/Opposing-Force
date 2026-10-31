package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OFParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(Registries.PARTICLE_TYPE, OpposingForce.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LASER_DUST = PARTICLE_TYPE.register("laser_dust", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LASER_IMPACT = PARTICLE_TYPE.register("laser_impact", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LASER_SWEEP = PARTICLE_TYPE.register("laser_sweep", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ACID = PARTICLE_TYPE.register("acid", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIRE_BREATH = PARTICLE_TYPE.register("fire_breath", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPORE_CLOUD = PARTICLE_TYPE.register("spore_cloud", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BULLET_TRAIL = PARTICLE_TYPE.register("bullet_trail", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BULLET_IMPACT = PARTICLE_TYPE.register("bullet_impact", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPECTRAL_SWEEP = PARTICLE_TYPE.register("spectral_sweep", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_SOUL_FIRE_FLAME = PARTICLE_TYPE.register("small_soul_fire_flame", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHOCKWAVE = PARTICLE_TYPE.register("shockwave", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_SHOCKWAVE = PARTICLE_TYPE.register("small_shockwave", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BRICK_SPORE_CLOUD = PARTICLE_TYPE.register("brick_spore_cloud", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GUN_FLASH = PARTICLE_TYPE.register("gun_flash", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LASER_FLASH = PARTICLE_TYPE.register("laser_flash", () -> new SimpleParticleType(true));

}
