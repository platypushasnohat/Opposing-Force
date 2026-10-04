package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.entity.Terror;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class TerrorSoundInstance extends AbstractTickableSoundInstance {

    private final Terror terror;

    public TerrorSoundInstance(Terror terror) {
        super(OFSoundEvents.TERROR_SAW_LOOP.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.terror = terror;
        this.x = (float) terror.getX();
        this.y = (float) terror.getY();
        this.z = (float) terror.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public void tick() {
        if (this.terror.isAlive()) {
            this.x = (float) this.terror.getX();
            this.y = (float) this.terror.getY();
            this.z = (float) this.terror.getZ();
            this.pitch = this.terror.getVoicePitch();
            float horizontalDistance = (float) this.terror.getDeltaMovement().horizontalDistance();
            if (horizontalDistance >= 0.01F) {
                this.volume = 1.0F;
            } else {
                this.volume = 0.0F;
            }
        } else {
            this.stop();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.terror.isSilent() && this.terror.getAnimationState() == Terror.ATTACK_ANIMATION;
    }

    public boolean isSameEntity(Terror terror) {
        return this.terror.isAlive() && this.terror.getId() == terror.getId();
    }
}
