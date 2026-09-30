package com.barl_inc.opposing_force.client.particle;

import com.platypushasnohat.sinew.utils.SinewColorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class AcidParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected AcidParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0F, 0.0F, 0.0F);
        this.friction = 0.96F;
        this.spriteSet = spriteSet;
        this.xd *= 0.125F;
        this.yd *= 0.125F;
        this.zd *= 0.125F;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        float colorRand = level.getRandom().nextFloat() * 0.2F;
        this.rCol = SinewColorUtils.unpackRed(0xb5e430) - level.getRandom().nextFloat() * 0.2F;
        this.gCol = SinewColorUtils.unpackGreen(0xb5e430) - level.getRandom().nextFloat() * 0.2F;
        this.bCol = SinewColorUtils.unpackBlue(0xb5e430);
        this.quadSize *= 0.75F + this.random.nextFloat() * 0.5F;
        this.lifetime = (int) ((double) 20 / (Math.random() * 0.8D + 0.2D));
        this.lifetime = Math.max(this.lifetime, 1);
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            return new AcidParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}