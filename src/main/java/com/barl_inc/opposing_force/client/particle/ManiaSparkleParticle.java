package com.barl_inc.opposing_force.client.particle;

import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class ManiaSparkleParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected ManiaSparkleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, int color) {
        super(level, x, y, z, 0.0F, 0.0F, 0.0F);
        this.spriteSet = spriteSet;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.friction = 0.9F;
        this.hasPhysics = false;
        float shade = 0.8F + this.random.nextFloat() * 0.2F;
        this.rCol = SinewColorUtils.unpackRed(color) * shade;
        this.gCol = SinewColorUtils.unpackGreen(color) * shade;
        this.bCol = SinewColorUtils.unpackBlue(color) * shade;
        this.quadSize *= 0.5F + this.random.nextFloat() * 0.3F;
        this.lifetime = 8 + this.random.nextInt(8);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.spriteSet);
            this.alpha = 1.0F - (float) this.age / this.lifetime;
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManiaSparkleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0xE88AAE);
        }
    }

    public static class EliteFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public EliteFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManiaSparkleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0x6F9BFF);
        }
    }

    public static class HeroFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public HeroFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManiaSparkleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0x6EBEFF);
        }
    }
}
