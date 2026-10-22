package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.entity.projectile.Soul;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class SoulSoundInstance extends AbstractTickableSoundInstance {

    private final Soul soul;

    public SoulSoundInstance(Soul soul) {
        super(SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.soul = soul;
        this.volume = 1.0F;
        this.pitch = 0.5F;
        this.looping = true;
    }

    @Override
    public boolean canPlaySound() {
        return this.soul.isAlive() && !this.soul.isSilent();
    }

    @Override
    public void tick() {
        if (!this.soul.isAlive()) {
            this.stop();
        }
        this.x = (float) this.soul.getX();
        this.y = (float) this.soul.getY();
        this.z = (float) this.soul.getZ();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(Soul soul) {
        return this.soul.isAlive() && this.soul.getId() == soul.getId();
    }
}
