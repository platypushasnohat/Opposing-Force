package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.entity.Gnat;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class GnatSoundInstance extends AbstractTickableSoundInstance {

    private final Gnat gnat;

    public GnatSoundInstance(Gnat gnat) {
        super(OFSoundEvents.GNAT_BUZZING.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.gnat = gnat;
        this.x = (float) gnat.getX();
        this.y = (float) gnat.getY();
        this.z = (float) gnat.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public void tick() {
        if (this.gnat.isAlive()) {
            this.x = (float) this.gnat.getX();
            this.y = (float) this.gnat.getY();
            this.z = (float) this.gnat.getZ();
            float horizontalDistance = (float) this.gnat.getDeltaMovement().horizontalDistance();
            if (horizontalDistance >= 0.01F) {
                this.pitch = Mth.lerp(Mth.clamp(horizontalDistance, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
                this.volume = Mth.lerp(Mth.clamp(horizontalDistance, 0.0F, 0.5F), 0.0F, 1.2F);
            } else {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }
        } else {
            this.stop();
        }
    }

    private float getMinPitch() {
        return this.gnat.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.gnat.isBaby() ? 1.5F : 1.1F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.gnat.isSilent();
    }

    public boolean isSameEntity(Gnat gnat) {
        return this.gnat.isAlive() && this.gnat.getId() == gnat.getId();
    }
}
