package com.barl_inc.opposing_force.client.particle;

import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class SporeCloudParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected SporeCloudParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0F, 0.0F, 0.0F);
        this.friction = 0.96F;
        this.spriteSet = spriteSet;
        this.xd *= 0.125F;
        this.yd *= 0.125F;
        this.zd *= 0.125F;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;

        float random = this.random.nextFloat();
        int color = 0xe35c22;
        float shade = 0.9F + this.random.nextFloat() * 0.1F;
        if (random < 0.5F) {
            color = 0xc46b27;
        } else if (random < 0.75F) {
            color = 0xd19c1f;
        }

        this.rCol = SinewColorUtils.unpackRed(color) * shade;
        this.gCol = SinewColorUtils.unpackGreen(color) * shade;
        this.bCol = SinewColorUtils.unpackBlue(color) * shade;

        this.quadSize *= 0.75F + this.random.nextFloat() * 0.5F;
        this.lifetime = 15 + this.random.nextInt(15);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp(((float) this.age + scaleFactor) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.spriteSet);
        }
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
