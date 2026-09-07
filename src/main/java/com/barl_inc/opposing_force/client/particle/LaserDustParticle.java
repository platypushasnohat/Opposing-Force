package com.barl_inc.opposing_force.client.particle;

import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class LaserDustParticle extends TextureSheetParticle {

    protected LaserDustParticle(ClientLevel level, double x, double y, double z, double xMotion, double yMotion, double zMotion, SpriteSet spriteSet) {
        super(level, x, y, z, xMotion, yMotion, zMotion);
        this.friction = 0.96F;
        this.gravity = -0.02F - 0.03F * level.getRandom().nextFloat();
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd *= 0.125F;
        this.yd *= 0.125F;
        this.zd *= 0.125F;
        this.rCol = SinewColorUtils.unpackRed(0xff246d);
        this.gCol = SinewColorUtils.unpackGreen(0xff246d);
        this.bCol = SinewColorUtils.unpackBlue(0xff246d);
        this.quadSize *= 0.75F + this.random.nextFloat() * 0.5F;
        this.lifetime = (int) ((double) 30 / ((double) level.getRandom().nextFloat() * 0.8D + 0.2D));
        this.lifetime = Math.max(this.lifetime, 1);
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public float getQuadSize(float f) {
        return this.quadSize * Mth.clamp(((float) this.age + f) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public int getLightColor(float partialTicks) {
        return 240;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LaserDustParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}