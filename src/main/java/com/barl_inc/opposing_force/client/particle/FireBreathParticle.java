package com.barl_inc.opposing_force.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class FireBreathParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final float spinIncrement;

    protected FireBreathParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = spriteSet;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.setSize(0.5F, 0.5F);
        this.quadSize = 0.5F;
        this.lifetime = 16 + this.random.nextInt(5);
        this.friction = 0.98F;
        this.roll = (float) Math.toRadians(360.0F * this.random.nextFloat());
        this.oRoll = this.roll;
        this.spinIncrement = (this.random.nextBoolean() ? -1 : 1) * this.random.nextFloat() * 0.25F;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        float ageProgress = this.age / (float) this.lifetime;
        float rollProgress = 1.0F - (ageProgress - 0.5F) * 1.3F;
        this.oRoll = this.roll;
        this.roll += rollProgress * this.spinIncrement;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FireBreathParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}