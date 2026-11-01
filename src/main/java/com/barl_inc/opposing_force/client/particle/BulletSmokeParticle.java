package com.barl_inc.opposing_force.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class BulletSmokeParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected BulletSmokeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.spriteSet = spriteSet;
        this.xd *= xSpeed;
        this.yd *= ySpeed;
        this.zd *= zSpeed;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        float color = this.random.nextFloat() * 0.5F;
        this.rCol = color;
        this.gCol = color;
        this.bCol = color;
        this.quadSize *= 0.75F * 1.5F;
        this.lifetime = 8 + this.random.nextInt(8);
        this.setSpriteFromAge(spriteSet);
        this.hasPhysics = false;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp((this.age + scaleFactor) / this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteSet);
        float ageProgress = this.age / (float) this.lifetime;
        this.setAlpha(1.0F - ageProgress);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BulletSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
