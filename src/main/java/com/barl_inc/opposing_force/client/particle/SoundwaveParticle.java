package com.barl_inc.opposing_force.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SoundwaveParticle extends TextureSheetParticle {

    protected SoundwaveParticle(ClientLevel level, double x, double y, double z, double xMotion, double yMotion, double zMotion, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.xd = xMotion;
        this.yd = yMotion;
        this.zd = zMotion;
        this.friction = 1.0F;
        this.gravity = 0.0F;
        this.lifetime = 20;
        this.quadSize = 0.5F;
        this.hasPhysics = false;
        this.pickSprite(spriteSet);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        super.tick();
        float life = (float) this.age / this.lifetime;
        this.alpha = Mth.clamp(2.0F - life * 2.0F, 0.0F, 1.0F);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.quadSize * (1.0F + (this.age + partialTicks) / this.lifetime * 0.5F);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vector3f motion = new Vector3f((float) this.xd, (float) this.yd, (float) this.zd);
        float right = -motion.dot(camera.getLeftVector());
        float up = motion.dot(camera.getUpVector());
        Quaternionf rotation = new Quaternionf(camera.rotation()).rotateZ((float) Mth.atan2(up, right));
        this.renderRotatedQuad(buffer, camera, rotation, partialTicks);
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
            return new SoundwaveParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
