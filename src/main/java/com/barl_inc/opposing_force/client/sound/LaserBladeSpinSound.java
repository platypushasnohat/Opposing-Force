package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.entity.projectile.LaserBlade;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class LaserBladeSpinSound extends AbstractTickableSoundInstance {

    private final LaserBlade laserBlade;

    public LaserBladeSpinSound(LaserBlade laserBlade) {
        super(OFSoundEvents.LASER_BLADE_SPIN.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.laserBlade = laserBlade;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.looping = true;
    }

    @Override
    public boolean canPlaySound() {
        return this.laserBlade.isAlive() && !this.laserBlade.isSilent();
    }

    @Override
    public void tick() {
        if (!this.laserBlade.isAlive()) {
            this.stop();
        }
        this.x = (float) this.laserBlade.getX();
        this.y = (float) this.laserBlade.getY();
        this.z = (float) this.laserBlade.getZ();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(LaserBlade laserBlade) {
        return this.laserBlade.isAlive() && this.laserBlade.getId() == laserBlade.getId();
    }
}