package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.entity.projectile.ElectricCharge;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class ElectricChargeSoundInstance extends AbstractTickableSoundInstance {

    private static final float MIN_PITCH = 0.75F;
    private static final float MAX_PITCH = 1.25F;

    private final ElectricCharge charge;

    public ElectricChargeSoundInstance(ElectricCharge charge) {
        super(OFSoundEvents.ELECTRIC_CHARGE.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.charge = charge;
        this.x = (float) charge.getX();
        this.y = (float) charge.getY();
        this.z = (float) charge.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public boolean canPlaySound() {
        return this.charge.isAlive() && !this.charge.isSilent() && !this.charge.isDischarging();
    }

    @Override
    public void tick() {
        if (!this.charge.isAlive() || this.charge.isDischarging()) {
            this.stop();
            return;
        }
        this.x = (float) this.charge.getX();
        this.y = (float) this.charge.getY();
        this.z = (float) this.charge.getZ();
        float speed = (float) this.charge.getDeltaMovement().horizontalDistance();
        this.pitch = Mth.lerp(Mth.clamp(speed, MIN_PITCH, MAX_PITCH), MIN_PITCH, MAX_PITCH);
        this.volume = 2.0F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(ElectricCharge charge) {
        return this.charge.isAlive() && this.charge.getId() == charge.getId();
    }
}
