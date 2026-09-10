package com.barl_inc.opposing_force.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class LaserSweepParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected LaserSweepParticle(ClientLevel level, double x, double y, double z, double quadSizeMultiplier, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0F, 0.0F, 0.0F);
        this.spriteSet = spriteSet;
        this.lifetime = 4;
        this.quadSize = 1.0F - (float)quadSizeMultiplier * 0.5F;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.spriteSet);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LaserSweepParticle(level, x, y, z, xSpeed, this.spriteSet);
        }
    }
}