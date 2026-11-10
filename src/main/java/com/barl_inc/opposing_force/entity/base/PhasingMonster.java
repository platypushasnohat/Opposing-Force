package com.barl_inc.opposing_force.entity.base;

import com.barl_inc.opposing_force.entity.ai.navigation.PhasingFlyingNavigation;
import com.barl_inc.opposing_force.entity.ai.navigation.PhasingMoveControl;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralFade;
import com.platypushasnohat.sinew.registry.SinewDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class PhasingMonster extends BasicFlyingMonster implements SpectralEntity {

    public static final int IDLE_DESPAWN_TICKS = 2400;

    private final SpectralFade fade = new SpectralFade(this);
    private float tilt;
    private float prevTilt;
    private int idleTicks;

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

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getTarget() != null || !this.canVanish()) {
            this.idleTicks = 0;
        } else {
            this.idleTicks++;
        }
        if (this.idleTicks >= IDLE_DESPAWN_TICKS || this.tickCount % 20 == 0 && this.shouldVanishInDaylight() && this.random.nextInt(4) == 0) {
            this.vanish();
        }
    }

    protected boolean despawnsNaturally() {
        return false;
    }

    private boolean canVanish() {
        return this.despawnsNaturally() && !this.hasCustomName() && !this.isLeashed();
    }

    private boolean shouldVanishInDaylight() {
        return this.canVanish() && this.level().isDay() && this.isAboveground();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        this.idleTicks = 0;
        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("IdleTicks", this.idleTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.idleTicks = tag.getInt("IdleTicks");
    }

    public boolean isAboveground() {
        BlockPos pos = this.blockPosition();
        return pos.getY() >= this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
    }

    protected void vanish() {
        this.level().broadcastEntityEvent(this, EntityEvent.POOF);
        this.discard();
    }

    public double getMaxPathY() {
        return Double.MAX_VALUE;
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
        this.getMoveControl().setWantedPosition(approach.x, Math.min(approach.y - this.getBbHeight() / 2.0F, this.getMaxPathY()), approach.z, 1.2D + Mth.clamp(this.getGap(target) - 1.25D, 0.0D, this.getChaseBoost()) * 0.6D);
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
        float scale = 1.0F;
        if (source.is(OFDamageTypes.LASER_BOLT)) {
            scale = 0.5F;
        } else if (source.is(SinewDamageTypes.MAGIC_DAMAGE)) {
            scale = 1.25F;
        }
        boolean hurt = !(source.getEntity() instanceof PhasingMonster) && super.hurt(source, amount * scale);
        if (hurt) {
            this.idleTicks = 0;
        }
        return hurt;
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