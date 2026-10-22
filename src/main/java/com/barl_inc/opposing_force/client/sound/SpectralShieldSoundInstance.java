package com.barl_inc.opposing_force.client.sound;

import com.barl_inc.opposing_force.item.SpectralShieldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.WeakHashMap;

public class SpectralShieldSoundInstance extends AbstractTickableSoundInstance {

    private static final Map<LivingEntity, SpectralShieldSoundInstance> PLAYING = new WeakHashMap<>();

    private final LivingEntity entity;

    public SpectralShieldSoundInstance(LivingEntity entity) {
        super(SoundEvents.BEACON_AMBIENT, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.looping = true;
        this.volume = 1.0F;
        this.pitch = 1.6F;
        this.relative = entity == Minecraft.getInstance().player;
        this.attenuation = this.relative ? Attenuation.NONE : Attenuation.LINEAR;
        this.tick();
    }

    public static void play(LivingEntity entity) {
        SoundManager manager = Minecraft.getInstance().getSoundManager();
        SpectralShieldSoundInstance sound = PLAYING.get(entity);
        if (sound == null || !manager.isActive(sound)) {
            sound = new SpectralShieldSoundInstance(entity);
            PLAYING.put(entity, sound);
            manager.play(sound);
        }
    }

    @Override
    public void tick() {
        if (this.entity.isRemoved() || !this.entity.isBlocking() || !(this.entity.getUseItem().getItem() instanceof SpectralShieldItem)) {
            this.stop();
        } else if (!this.relative) {
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
        }
    }
}
