package com.barl_inc.opposing_force.utils;

import com.barl_inc.opposing_force.client.sound.DicerLaserSound;
import com.barl_inc.opposing_force.entity.misc.DicerLaser;
import com.platypushasnohat.sinew.mixins.client.SoundEngineAccessor;
import com.platypushasnohat.sinew.mixins.client.SoundManagerAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {

    public static final Int2ObjectMap<AbstractTickableSoundInstance> ENTITY_SOUND_INSTANCE_MAP = new Int2ObjectOpenHashMap<>();

    @Override
    public void playSound(@Nullable Object soundEmitter, byte type) {
        if (soundEmitter instanceof Entity entity && !entity.level().isClientSide) {
            return;
        }
        if (type == 0) {
            if (soundEmitter instanceof DicerLaser laser) {
                DicerLaserSound sound;
                AbstractTickableSoundInstance oldSound = ENTITY_SOUND_INSTANCE_MAP.get(laser.getId());
                if (oldSound == null || !(oldSound instanceof DicerLaserSound laserSound && laserSound.isSameEntity(laser))) {
                    sound = new DicerLaserSound(laser);
                    ENTITY_SOUND_INSTANCE_MAP.put(laser.getId(), sound);
                } else {
                    sound = laserSound;
                }
                if (!this.isSoundPlaying(sound) && sound.canPlaySound()) {
                    Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
                }
            }
        }
    }

    @Override
    public void clearSoundCacheFor(Entity entity) {
        ENTITY_SOUND_INSTANCE_MAP.remove(entity.getId());
    }

    private boolean isSoundPlaying(AbstractTickableSoundInstance sound) {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        SoundEngine soundEngine = ((SoundManagerAccessor) soundManager).getSoundEngine();
        SoundEngineAccessor engineAccessor = (SoundEngineAccessor) soundEngine;
        return engineAccessor.getQueuedTickableSounds().contains(sound) || engineAccessor.getTickingSounds().contains(sound);
    }
}
