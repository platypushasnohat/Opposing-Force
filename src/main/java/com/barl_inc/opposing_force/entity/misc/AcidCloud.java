package com.barl_inc.opposing_force.entity.misc;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AcidCloud extends AreaDamageCloud {

    private int soundCooldown = 20;

    public AcidCloud(EntityType<? extends AcidCloud> entityType, Level level) {
        super(entityType, level);
    }

    public AcidCloud(Level level, double x, double y, double z) {
        super(OFEntities.ACID_CLOUD.get(), level, x, y, z);
    }

    @Override
    public DamageSource getDamageSource() {
        return OFDamageTypes.causeAcidDamage(this.level().registryAccess(), this.getOwner());
    }

    @Override
    public ParticleOptions getParticle() {
        return OFParticleTypes.ACID.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isAlive()) {
            if (this.soundCooldown > 0) {
                this.soundCooldown--;
            }
            if (this.soundCooldown <= 0) {
                this.level().playSound(null, this.blockPosition(), OFSoundEvents.ACID_BURNING.get(), SoundSource.NEUTRAL, 0.15F, SinewSoundUtils.randomizePitch(this.level()));
                this.soundCooldown = 70 + this.getRandom().nextInt(20);
            }
        }
    }
}
