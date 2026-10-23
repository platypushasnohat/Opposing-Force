package com.barl_inc.opposing_force.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class ShockwaveParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected ShockwaveParticle(ClientLevel level, double x, double y, double z, double radius, double duration, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.lifetime = (int) duration;
        this.quadSize = (float) radius;
        this.gravity = 0.0F;
        this.hasPhysics = false;
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
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        this.renderRotatedQuad(buffer, camera, new Quaternionf().rotationX(-Mth.HALF_PI), partialTicks);
        this.renderRotatedQuad(buffer, camera, new Quaternionf().rotationX(Mth.HALF_PI), partialTicks);
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

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ShockwaveParticle(level, x, y, z, xSpeed, ySpeed, this.spriteSet);
        }
    }
}
