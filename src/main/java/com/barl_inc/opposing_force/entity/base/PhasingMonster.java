package com.barl_inc.opposing_force.entity.base;

import com.barl_inc.opposing_force.entity.ai.navigation.PhasingFlyingNavigation;
import com.barl_inc.opposing_force.entity.ai.navigation.PhasingMoveControl;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralFade;
import com.platypushasnohat.sinew.registry.SinewDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class PhasingMonster extends BasicFlyingMonster implements SpectralEntity {

    private final SpectralFade fade = new SpectralFade(this);
    private float tilt;
    private float prevTilt;

    protected PhasingMonster(EntityType<? extends PhasingMonster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.moveControl = new PhasingMoveControl(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new PhasingFlyingNavigation(this, level);
    }

    @Override
    public void tick() {
        this.noPhysics = this.isPhasing();
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        this.fade.tick();
        if (this.level().isClientSide) {
            this.prevTilt = this.tilt;
            this.tilt = Mth.approachDegrees(this.tilt, this.getXRot(), this.getTiltSpeed());
        }
    }

    protected boolean isPhasing() {
        return true;
    }

    protected float getTiltSpeed() {
        return 6.0F;
    }

    protected double getChaseBoost() {
        return 12.0D;
    }

    public float getTilt(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevTilt, this.tilt);
    }

    public float getFade(float partialTicks) {
        return this.fade.get(partialTicks);
    }

    public void approach(LivingEntity target) {
        Vec3 center = target.getBoundingBox().getCenter();
        Vec3 approach = center.add(this.getBoundingBox().getCenter().subtract(center).normalize().scale(1.2D));
        this.getMoveControl().setWantedPosition(approach.x, approach.y - this.getBbHeight() / 2.0F, approach.z, 1.2D + Mth.clamp(this.getGap(target) - 1.25D, 0.0D, this.getChaseBoost()) * 0.6D);
    }

    public double getGap(LivingEntity target) {
        AABB box = this.getBoundingBox();
        AABB targetBox = target.getBoundingBox();
        double x = Math.max(0.0D, Math.max(box.minX - targetBox.maxX, targetBox.minX - box.maxX));
        double y = Math.max(0.0D, Math.max(box.minY - targetBox.maxY, targetBox.minY - box.maxY));
        double z = Math.max(0.0D, Math.max(box.minZ - targetBox.maxZ, targetBox.minZ - box.maxZ));
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float scale = source.is(OFDamageTypes.LASER_BOLT) ? 0.5F : source.is(SinewDamageTypes.MAGIC_DAMAGE) ? 1.25F : 1.0F;
        return !(source.getEntity() instanceof PhasingMonster) && super.hurt(source, amount * scale);
    }

    @Override
    public boolean isVisibleWithoutSight(Player viewer) {
        return this.fade.isVisibleTo(viewer);
    }

    @Override
    public int getFadeInTicks() {
        return 0;
    }

    @Override
    public int getStareTicksToTarget() {
        return 60;
    }

    @Override
    public boolean canBeHeardBy(Player listener) {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }
}