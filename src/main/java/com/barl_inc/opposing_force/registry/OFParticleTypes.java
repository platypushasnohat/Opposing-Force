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

}
