package com.barl_inc.opposing_force.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class KirinStepParticle extends TextureSheetParticle {

    private static final int LIFETIME = 12;
    private static final int FADE_START = 6;

    private final SpriteSet spriteSet;
    private final float yaw;

    protected KirinStepParticle(ClientLevel level, double x, double y, double z, float yaw, float scale, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.yaw = yaw;
        this.lifetime = LIFETIME;
        this.quadSize = 0.375F * scale;
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
            if (this.age > FADE_START) {
                this.alpha = 1.0F - (float) (this.age - FADE_START) / (this.lifetime - FADE_START);
            }
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        this.renderRotatedQuad(buffer, camera, new Quaternionf().rotationY(-this.yaw * Mth.DEG_TO_RAD).rotateX(-Mth.HALF_PI), partialTicks);
        this.renderRotatedQuad(buffer, camera, new Quaternionf().rotationY(-this.yaw * Mth.DEG_TO_RAD).rotateX(Mth.HALF_PI), partialTicks);
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
            return new KirinStepParticle(level, x, y, z, (float) xSpeed, ySpeed > 0.0D ? (float) ySpeed : 1.0F, this.spriteSet);
        }
    }
}