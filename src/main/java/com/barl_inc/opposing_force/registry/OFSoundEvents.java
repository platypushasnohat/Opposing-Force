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
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_STEP = registerSoundEvent("dicer_step");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_ATTACK = registerSoundEvent("dicer_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_DASH_WARN = registerSoundEvent("dicer_dash_warn");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_DASH = registerSoundEvent("dicer_dash");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_LASER = registerSoundEvent("dicer_laser");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_LASER_START = registerSoundEvent("dicer_laser_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> DICER_LASER_END = registerSoundEvent("dicer_laser_end");

    public static final DeferredHolder<SoundEvent, SoundEvent> GNAT_BUZZING = registerSoundEvent("gnat_buzzing");

    public static final DeferredHolder<SoundEvent, SoundEvent> FURBALL_HURT = registerSoundEvent("furball_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> FURBALL_DEATH = registerSoundEvent("furball_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> FURBALL_IDLE = registerSoundEvent("furball_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> FURBALL_ATTACK = registerSoundEvent("furball_attack");

    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_HURT = registerSoundEvent("terror_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_DEATH = registerSoundEvent("terror_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_IDLE = registerSoundEvent("terror_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_SAW_START = registerSoundEvent("terror_saw_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_SAW_LOOP = registerSoundEvent("terror_saw_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_SAW_END = registerSoundEvent("terror_saw_end");

    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_BOLT_IMPACT = registerSoundEvent("laser_bolt_impact");

    public static final DeferredHolder<SoundEvent, SoundEvent> BLASTER_SHOOT = registerSoundEvent("blaster_shoot");

    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_BLADE_SWING = registerSoundEvent("laser_blade_swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_BLADE_IMPACT = registerSoundEvent("laser_blade_impact");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_BLADE_SPIN = registerSoundEvent("laser_blade_spin");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_BLADE_CATCH = registerSoundEvent("laser_blade_catch");

    public static final DeferredHolder<SoundEvent, SoundEvent> ACID_CHARGE_EXPLODE = registerSoundEvent("acid_charge_explode");
    public static final DeferredHolder<SoundEvent, SoundEvent> ACID_BURNING = registerSoundEvent("acid_burning");

    public static final DeferredHolder<SoundEvent, SoundEvent> SLAYSER_DISC = registerSoundEvent("slayser_disc");

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_REINFORCEMENT = registerSoundEvent("zombie_reinforcement");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String soundName) {
        return SOUND_EVENT.register(soundName, () -> SoundEvent.createVariableRangeEvent(OpposingForce.location(soundName)));
    }
}
