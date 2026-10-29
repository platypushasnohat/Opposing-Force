package com.barl_inc.opposing_force.client.render.shader;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.sound.ManiaSoundInstance;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.io.IOException;
import java.util.List;

public class ManiaTrip {

    private static final RandomSource RANDOM = RandomSource.create();
    private static PostChain chain;
    private static boolean failed;
    private static int width = -1;
    private static int height = -1;
    private static int soundTicks;
    private static List<SoundEvent> sounds;

    public static float getStrength(LivingEntity entity, float partialTicks) {
        MobEffectInstance effect = entity.getEffect(OFMobEffects.MANIA);
        float strength = 0.0F;
        if (effect != null) {
            strength = 1.0F;
            if (!effect.isInfiniteDuration()) {
                strength = Mth.clamp((effect.getDuration() - partialTicks) / 40.0F, 0.0F, 1.0F);
            }
        }
        return strength;
    }

    public static boolean isQuadruple(LivingEntity entity) {
        MobEffectInstance effect = entity.getEffect(OFMobEffects.MANIA);
        return effect != null && effect.getAmplifier() >= 1;
    }

    public static float getLocalStrength() {
        LocalPlayer player = Minecraft.getInstance().player;
        float strength = 0.0F;
        if (player != null) {
            strength = getStrength(player, 0.0F);
        }
        return strength;
    }

    public static boolean isLocalQuadruple() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && isQuadruple(player);
    }

    public static double modifyFov(double fov, float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        double result = fov;
        if (player != null) {
            float strength = getStrength(player, partialTicks);
            float age = player.tickCount + partialTicks;
            float depth = isQuadruple(player) ? 0.3F : 0.15F;
            result = fov * (1.0D + strength * depth * (Mth.sin(age * 0.13F) * 0.7F + Mth.sin(age * 0.37F) * 0.3F));
        }
        return result;
    }

    public static boolean shouldDistort(SoundInstance sound) {
        return !(sound instanceof TickableSoundInstance) && sound.getSource() != SoundSource.MUSIC && sound.getSource() != SoundSource.RECORDS && getLocalStrength() > 0.0F;
    }

    public static SoundInstance distort(SoundInstance sound) {
        return new ManiaSoundInstance(sound, RANDOM.nextFloat() * Mth.TWO_PI);
    }

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null && !minecraft.isPaused() && player.hasEffect(OFMobEffects.MANIA)) {
            soundTicks--;
            if (soundTicks <= 0) {
                playRandomSound(minecraft, player);
                soundTicks = 10 + RANDOM.nextInt(30);
                if (isQuadruple(player)) {
                    soundTicks /= 2;
                }
            }
        }
    }

    private static void playRandomSound(Minecraft minecraft, LocalPlayer player) {
        if (sounds == null) {
            sounds = BuiltInRegistries.SOUND_EVENT.stream().filter(sound -> !sound.getLocation().getPath().startsWith("music")).toList();
        }
        if (!sounds.isEmpty()) {
            SoundEvent sound = sounds.get(RANDOM.nextInt(sounds.size()));
            double x = player.getX() + (RANDOM.nextDouble() - 0.5D) * 16.0D;
            double y = player.getY() + (RANDOM.nextDouble() - 0.5D) * 6.0D;
            double z = player.getZ() + (RANDOM.nextDouble() - 0.5D) * 16.0D;
            minecraft.getSoundManager().play(new SimpleSoundInstance(sound, SoundSource.AMBIENT, 0.6F, 0.5F + RANDOM.nextFloat() * 1.5F, RANDOM, x, y, z));
        }
    }

    public static void render(float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null && !failed && getStrength(player, partialTicks) > 0.0F) {
            if (chain == null) {
                load(minecraft);
            }
            if (chain != null) {
                Window window = minecraft.getWindow();
                if (window.getWidth() != width || window.getHeight() != height) {
                    width = window.getWidth();
                    height = window.getHeight();
                    chain.resize(width, height);
                }
                chain.setUniform("Strength", getStrength(player, partialTicks));
                chain.setUniform("Mode", isQuadruple(player) ? 1.0F : 0.0F);
                chain.process(partialTicks);
                minecraft.getMainRenderTarget().bindWrite(false);
            }
        }
    }

    private static void load(Minecraft minecraft) {
        try {
            chain = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getMainRenderTarget(), OpposingForce.location("shaders/post/mania.json"));
            width = -1;
            height = -1;
        } catch (IOException | JsonSyntaxException exception) {
            OpposingForce.LOGGER.warn("Failed to load mania shader", exception);
            failed = true;
        }
    }

    public static void reset() {
        if (chain != null) {
            chain.close();
        }
        chain = null;
        failed = false;
    }
}
