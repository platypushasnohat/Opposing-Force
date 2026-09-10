package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OFSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(Registries.SOUND_EVENT, OpposingForce.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_HURT = registerSoundEvent("dicer_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_DEATH = registerSoundEvent("dicer_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_IDLE = registerSoundEvent("dicer_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_ATTACK = registerSoundEvent("dicer_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_LASER = registerSoundEvent("dicer_laser");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_LASER_START = registerSoundEvent("dicer_laser_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_LASER_END = registerSoundEvent("dicer_laser_end");

    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_BOLT_IMPACT = registerSoundEvent("laser_bolt_impact");

    public static final DeferredHolder<SoundEvent, SoundEvent> BLASTER_SHOOT = registerSoundEvent("blaster_shoot");

    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_BLADE_SWING = registerSoundEvent("laser_blade_swing");

    public static final DeferredHolder<SoundEvent, SoundEvent> SLAYSER_DISC = registerSoundEvent("slayser_disc");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String soundName) {
        return SOUND_EVENT.register(soundName, () -> SoundEvent.createVariableRangeEvent(OpposingForce.location(soundName)));
    }
}
