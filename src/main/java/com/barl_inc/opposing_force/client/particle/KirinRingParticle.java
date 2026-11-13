package com.barl_inc.opposing_force.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class KirinRingParticle extends TextureSheetParticle {

    private static final int FADE_START = 4;

    private final float yaw;
    private final int delay;
    private final double launchSpeed;

    protected KirinRingParticle(ClientLevel level, double x, double y, double z, float yaw, double ySpeed, int delay, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.yaw = yaw;
        this.delay = delay;
        this.launchSpeed = ySpeed;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.friction = 0.9F;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.lifetime = 12 + delay;
        this.quadSize = 1.25F;
        this.pickSprite(spriteSet);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        if (this.age == this.delay) {
            this.yd = this.launchSpeed;
        }
        super.tick();
        if (!this.removed && this.age - this.delay > FADE_START) {
            this.alpha = 1.0F - (float) (this.age - this.delay - FADE_START) / (this.lifetime - this.delay - FADE_START);
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        if (this.age >= this.delay) {
            this.renderRotatedQuad(buffer, camera, new Quaternionf().rotationY(-this.yaw * Mth.DEG_TO_RAD).rotateX(-Mth.HALF_PI), partialTicks);
            this.renderRotatedQuad(buffer, camera, new Quaternionf().rotationY(-this.yaw * Mth.DEG_TO_RAD).rotateX(Mth.HALF_PI), partialTicks);
        }
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
            return new KirinRingParticle(level, x, y, z, (float) xSpeed, ySpeed, (int) zSpeed, this.spriteSet);
        }
    }
}