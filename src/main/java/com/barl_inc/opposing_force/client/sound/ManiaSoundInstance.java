package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.client.render.shader.ManiaTrip;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ManiaSoundInstance implements TickableSoundInstance {

    private final SoundInstance sound;
    private final float phase;
    private int age;

    public ManiaSoundInstance(SoundInstance sound, float phase) {
        this.sound = sound;
        this.phase = phase;
    }

    private float getWarbledPitch() {
        float depth = 0.12F;
        if (ManiaTrip.isLocalQuadruple()) {
            depth = 0.22F;
        }
        float warble = Mth.sin(this.age * 0.35F + this.phase) + Mth.sin(this.age * 1.3F + this.phase * 2.0F) * 0.4F;
        return this.sound.getPitch() * (1.0F + warble * depth * ManiaTrip.getLocalStrength());
    }

    @Override
    public void tick() {
        this.age++;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    public ResourceLocation getLocation() {
        return this.sound.getLocation();
    }

    @Nullable
    @Override
    public WeighedSoundEvents resolve(SoundManager manager) {
        return this.sound.resolve(manager);
    }

    @Override
    public Sound getSound() {
        return this.sound.getSound();
    }

    @Override
    public SoundSource getSource() {
        return this.sound.getSource();
    }

    @Override
    public boolean isLooping() {
        return this.sound.isLooping();
    }

    @Override
    public boolean isRelative() {
        return this.sound.isRelative();
    }

    @Override
    public int getDelay() {
        return this.sound.getDelay();
    }

    @Override
    public float getVolume() {
        return this.sound.getVolume();
    }

    @Override
    public float getPitch() {
        return this.getWarbledPitch();
    }

    @Override
    public double getX() {
        return this.sound.getX();
    }

    @Override
    public double getY() {
        return this.sound.getY();
    }

    @Override
    public double getZ() {
        return this.sound.getZ();
    }

    @Override
    public Attenuation getAttenuation() {
        return this.sound.getAttenuation();
    }

    @Override
    public boolean canStartSilent() {
        return this.sound.canStartSilent();
    }

    @Override
    public boolean canPlaySound() {
        return this.sound.canPlaySound();
    }

    @Override
    public CompletableFuture<AudioStream> getStream(SoundBufferLibrary soundBuffers, Sound sound, boolean looping) {
        return this.sound.getStream(soundBuffers, sound, looping);
    }
}
