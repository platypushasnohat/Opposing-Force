package com.barl_inc.opposing_force.utils;

import com.barl_inc.opposing_force.client.sound.DicerLaserSoundInstance;
import com.barl_inc.opposing_force.client.sound.GnatSoundInstance;
import com.barl_inc.opposing_force.client.sound.LaserBladeSoundInstance;
import com.barl_inc.opposing_force.entity.Gnat;
import com.barl_inc.opposing_force.entity.misc.DicerLaser;
import com.barl_inc.opposing_force.entity.projectile.LaserBlade;
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
                DicerLaserSoundInstance sound;
                AbstractTickableSoundInstance oldSound = ENTITY_SOUND_INSTANCE_MAP.get(laser.getId());
                if (oldSound == null || !(oldSound instanceof DicerLaserSoundInstance soundInstance && soundInstance.isSameEntity(laser))) {
                    sound = new DicerLaserSoundInstance(laser);
                    ENTITY_SOUND_INSTANCE_MAP.put(laser.getId(), sound);
                } else {
                    sound = soundInstance;
                }
                if (!this.isSoundPlaying(sound) && sound.canPlaySound()) {
                    Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
                }
            }
        }
        if (type == 1) {
            if (soundEmitter instanceof LaserBlade laserBlade) {
                LaserBladeSoundInstance sound;
                AbstractTickableSoundInstance oldSound = ENTITY_SOUND_INSTANCE_MAP.get(laserBlade.getId());
                if (oldSound == null || !(oldSound instanceof LaserBladeSoundInstance soundInstance && soundInstance.isSameEntity(laserBlade))) {
                    sound = new LaserBladeSoundInstance(laserBlade);
                    ENTITY_SOUND_INSTANCE_MAP.put(laserBlade.getId(), sound);
                } else {
                    sound = soundInstance;
                }
                if (!this.isSoundPlaying(sound) && sound.canPlaySound()) {
                    Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
                }
            }
        }
        if (type == 2) {
            if (soundEmitter instanceof Gnat gnat) {
                GnatSoundInstance sound;
                AbstractTickableSoundInstance oldSound = ENTITY_SOUND_INSTANCE_MAP.get(gnat.getId());
                if (oldSound == null || !(oldSound instanceof GnatSoundInstance soundInstance && soundInstance.isSameEntity(gnat))) {
                    sound = new GnatSoundInstance(gnat);
                    ENTITY_SOUND_INSTANCE_MAP.put(gnat.getId(), sound);
                } else {
                    sound = soundInstance;
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
