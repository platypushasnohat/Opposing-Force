package com.barl_inc.opposing_force.client.particle;

import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SporeCloudParticle extends SmokeParticle {
    protected SporeCloudParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, 1.5F, spriteSet);
        float roll = this.random.nextFloat();
        int color;
        if (roll < 0.50F) {
            color = 0xe35c22;
        } else if (roll < 0.75F) {
            color = 0xc46b27;
        } else if (roll < 0.90F) {
            color = 0xd19c1f;
        } else {
            color = 0xffffff;
        }
        float shade = 0.9F + this.random.nextFloat() * 0.1F;
        this.rCol = SinewColorUtils.unpackRed(color) * shade;
        this.gCol = SinewColorUtils.unpackGreen(color) * shade;
        this.bCol = SinewColorUtils.unpackBlue(color) * shade;
        this.lifetime *= 2;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SporeCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
