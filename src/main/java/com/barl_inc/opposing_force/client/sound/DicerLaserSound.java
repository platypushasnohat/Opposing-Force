package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.entity.misc.DicerLaser;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class DicerLaserSound extends AbstractTickableSoundInstance {

    private final DicerLaser laser;

    public DicerLaserSound(DicerLaser laser) {
        super(OFSoundEvents.DICER_LASER.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.laser = laser;
        this.volume = 2.0F;
        this.pitch = 1.0F;
        this.looping = true;
    }

    @Override
    public boolean canPlaySound() {
        return this.laser.isAlive() && !this.laser.isSilent();
    }

    @Override
    public void tick() {
        if (!this.laser.isAlive() || this.isLaserPoweringDown()) {
            this.stop();
        }
        this.x = (float) this.laser.getX();
        this.y = (float) this.laser.getY();
        this.z = (float) this.laser.getZ();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    private boolean isLaserPoweringDown() {
        return this.laser.tickCount - 3 > this.laser.getDuration() - 22;
    }

    public boolean isSameEntity(DicerLaser laser) {
        return this.laser.isAlive() && this.laser.getId() == laser.getId();
    }
}