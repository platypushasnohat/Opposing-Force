package com.barl_inc.opposing_force.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class KirinSmokeParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected KirinSmokeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.spriteSet = spriteSet;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.friction = 0.96F;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.quadSize *= 0.75F + this.random.nextFloat() * 0.5F;
        this.lifetime = 16 + this.random.nextInt(12);
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
            return new KirinSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
