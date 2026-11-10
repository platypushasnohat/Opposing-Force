package com.barl_inc.opposing_force.effect;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class ElectrifiedEffect extends MobEffect {

    public ElectrifiedEffect() {
        super(MobEffectCategory.HARMFUL, 0x0080C3);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return true;
        }

        boolean wet = entity.isInWaterRainOrBubble();
        this.spawnSparks(level, entity, wet);

        MobEffectInstance instance = entity.getEffect(OFMobEffects.ELECTRIFIED);
        int interval = 20 >> amplifier;
        boolean damageTick = instance == null || interval <= 0 || instance.getDuration() % interval == 0;

        if (wet && damageTick) {
            if (entity.hurt(OFDamageTypes.causeElectrifiedDamage(level.registryAccess()), 2.0F)) {
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), OFSoundEvents.ELECTRIC_CHARGE_ZAP.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
                level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), entity.getX(), entity.getY(0.5D), entity.getZ(), 5, entity.getBbWidth() * 0.4D, entity.getBbHeight() * 0.3D, entity.getBbWidth() * 0.4D, 0.0D);
            }
        }
        return true;
    }

    private void spawnSparks(ServerLevel level, LivingEntity entity, boolean wet) {
        int count;
        if (wet) {
            count = 1 + entity.getRandom().nextInt(2);
        } else {
            if (entity.getRandom().nextFloat() > 0.25F) return;
            count = 1;
        }
        double spreadX = entity.getBbWidth() * 0.4D;
        double spreadY = entity.getBbHeight() * 0.3D;
        level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), entity.getX(), entity.getY(0.5D), entity.getZ(), count, spreadX, spreadY, spreadX, 0.0D);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
