package com.barl_inc.opposing_force.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class BulletTrailParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected BulletTrailParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0F, 0.0F, 0.0F);
        this.spriteSet = spriteSet;
        this.friction = 0.98F;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize = 0.25F;
        this.lifetime = 8;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setAlpha(1.0F - (this.age / (float) this.lifetime));
        this.setSpriteFromAge(this.spriteSet);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BulletTrailParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}