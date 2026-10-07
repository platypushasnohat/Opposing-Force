package com.barl_inc.opposing_force.entity.misc;

import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BilePuddle extends AreaDamageCloud {

    private int soundCooldown = 20;

    public BilePuddle(EntityType<? extends BilePuddle> entityType, Level level) {
        super(entityType, level);
    }

    public BilePuddle(Level level, double x, double y, double z) {
        super(OFEntities.BILE_PUDDLE.get(), level, x, y, z);
    }

    @Override
    public DamageSource getDamageSource() {
        return OFDamageTypes.causeBileDamage(this.level().registryAccess(), this.getOwner());
    }

    @Override
    public ParticleOptions getParticle() {
        return OFParticleTypes.BILE.get();
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            float radius = this.getRadius();
            int extra = Mth.ceil((float) Math.PI * 2.5F * 2.5F) - Mth.ceil((float) Math.PI * radius * radius);
            ParticleOptions particle = this.getParticle();
            for (int j = 0; j < extra; j++) {
                float angle = this.getRandom().nextFloat() * (float) (Math.PI * 2.0F);
                float dist = Mth.sqrt(this.getRandom().nextFloat()) * radius;
                this.level().addAlwaysVisibleParticle(particle, this.getX() + Mth.cos(angle) * dist, this.getY(), this.getZ() + Mth.sin(angle) * dist, (0.5D - this.getRandom().nextDouble()) * 0.15D, 0.01F, (0.5D - this.getRandom().nextDouble()) * 0.15D);
            }
        }
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
