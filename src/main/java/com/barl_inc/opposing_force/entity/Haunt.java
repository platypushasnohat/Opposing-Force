package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.entity.ai.goal.PhasingWanderGoal;
import com.barl_inc.opposing_force.entity.base.PhasingMonster;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import com.platypushasnohat.sinew.entity.ai.goal.SpectralStareGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class Haunt extends PhasingMonster {

    public static final int ATTACK_ANIMATION = 1;
    public static final int SPIN_START_ANIMATION = 2;
    public static final int SPIN_ANIMATION = 3;
    private static final double SPIN_SPEED = 1.5D;

    public final SmoothAnimationState attackAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState spinStartAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState spinAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState headSpinAnimationState = new SmoothAnimationState();

    private boolean bounced;
    private int headSpinTicks;

    public Haunt(EntityType<? extends Haunt> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.STEP_HEIGHT, 0.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new HauntAttackGoal(this));
        this.goalSelector.addGoal(2, new PhasingWanderGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new SpectralStareGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.headSpinTicks > 0) {
                this.headSpinTicks--;
            } else if (this.getAnimationState() == 0 && this.position().distanceToSqr(this.xo, this.yo, this.zo) < 1.0E-4D && this.random.nextInt(1500) == 0) {
                this.headSpinTicks = 65;
            }
        }
    }

    @Override
    protected boolean isPhasing() {
        return !this.isSpinning();
    }

    @Override
    protected float getTiltSpeed() {
        return this.isSpinning() ? 30.0F : 6.0F;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isSpinning()) {
            Vec3 velocity = this.getDeltaMovement();
            Vec3 start = this.position();
            this.move(MoverType.SELF, velocity);
            Vec3 moved = this.position().subtract(start);
            boolean x = !Mth.equal(moved.x, velocity.x);
            boolean y = !Mth.equal(moved.y, velocity.y);
            boolean z = !Mth.equal(moved.z, velocity.z);
            this.setDeltaMovement(velocity);
            if (x || y || z) {
                Vec3 reflected = new Vec3(x ? -velocity.x : velocity.x, y ? -velocity.y : velocity.y, z ? -velocity.z : velocity.z).scale(0.97D);
                Vec3 twisted = this.twist(reflected, 20.0F, 60.0F);
                this.setDeltaMovement(x ? Math.copySign(twisted.x, reflected.x) : twisted.x, y ? Math.copySign(twisted.y, reflected.y) : twisted.y, z ? Math.copySign(twisted.z, reflected.z) : twisted.z);
                this.playSound(SoundEvents.ANVIL_LAND, 0.5F, 1.4F + this.random.nextFloat() * 0.4F);
                this.bounced = true;
            }
            this.faceMovement();
        } else {
            super.travel(travelVector);
        }
    }

    public boolean isSpinning() {
        return this.getAnimationState() == SPIN_ANIMATION;
    }

    private Vec3 twist(Vec3 direction, float minAngle, float maxAngle) {
        Vec3 normal = direction.normalize();
        Vec3 side = normal.cross(new Vec3(this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian())).normalize();
        float angle = Mth.nextFloat(this.random, minAngle, maxAngle) * Mth.DEG_TO_RAD;
        return normal.scale(Mth.cos(angle)).add(side.scale(Mth.sin(angle))).scale(direction.length());
    }

    private void faceMovement() {
        Vec3 velocity = this.getDeltaMovement();
        float yaw = (float) (Mth.atan2(velocity.z, velocity.x) * Mth.RAD_TO_DEG) - 90.0F;
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
        this.setXRot((float) -(Mth.atan2(velocity.y, velocity.horizontalDistance()) * Mth.RAD_TO_DEG));
    }

    public static boolean checkHauntSpawnRules(EntityType<Haunt> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() < 0 && checkMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() == 0, this.tickCount);
        this.attackAnimationState.animateWhen(this.getAnimationState() == ATTACK_ANIMATION, this.tickCount);
        this.spinStartAnimationState.animateWhen(this.getAnimationState() == SPIN_START_ANIMATION, this.tickCount);
        this.spinAnimationState.animateWhen(this.isSpinning(), this.tickCount);
        this.headSpinAnimationState.animateWhen(this.headSpinTicks > 0, this.tickCount);
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.7F;
    }

    private static class HauntAttackGoal extends AttackGoal {

        private final Haunt haunt;
        private int attackCooldown;
        private int spinCooldown;
        private int bounces;
        private int maxBounces;

        public HauntAttackGoal(Haunt haunt) {
            super(haunt);
            this.haunt = haunt;
        }

        @Override
        public void start() {
            super.start();
            this.attackCooldown = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.haunt.getTarget();
            if (target != null) {
                if (this.attackState == 1) {
                    this.tickSlam(target);
                } else if (this.attackState == 2) {
                    this.tickSpinStart(target);
                } else if (this.attackState == 3) {
                    this.tickSpin(target);
                } else {
                    this.tickApproach(target);
                }
            }
        }

        private void tickApproach(LivingEntity target) {
            double distance = this.haunt.distanceTo(target);
            this.lookAtTarget(target, 30.0F, 30.0F);
            this.haunt.getNavigation().stop();
            this.haunt.approach(target);
            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }
            if (this.spinCooldown > 0) {
                this.spinCooldown--;
            }
            if (this.haunt.getGap(target) <= 1.5D && this.attackCooldown <= 0) {
                this.attackState = 1;
            } else if (distance >= 4.0D && distance <= 24.0D && this.spinCooldown <= 0 && this.haunt.getRandom().nextInt(5) == 0 && this.haunt.hasLineOfSight(target) && this.haunt.level().noCollision(this.haunt)) {
                this.attackState = 2;
            }
        }

        private void tickSlam(LivingEntity target) {
            this.timer++;
            this.lookAtTarget(target, 30.0F, 30.0F);
            if (this.timer == 1) {
                this.haunt.setAnimationState(ATTACK_ANIMATION);
            }
            if (this.timer == 14) {
                this.haunt.setDeltaMovement(this.aimAt(target, 0.25D));
            }
            if (this.timer == 17 && this.haunt.getGap(target) < 1.0D && this.haunt.hasLineOfSight(target)) {
                this.haunt.doHurtTarget(target);
                this.haunt.playSound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 0.6F, 1.2F);
            }
            if (this.timer >= 30) {
                this.haunt.setAnimationState(0);
                this.timer = 0;
                this.attackCooldown = 24;
                this.attackState = 0;
            }
        }

        private void tickSpinStart(LivingEntity target) {
            this.timer++;
            this.lookAtTarget(target, 30.0F, 30.0F);
            this.haunt.setDeltaMovement(this.haunt.getDeltaMovement().scale(0.8D));
            if (this.timer == 1) {
                this.haunt.setAnimationState(SPIN_START_ANIMATION);
                this.haunt.playSound(SoundEvents.VEX_CHARGE, 1.0F, 0.6F);
            }
            if (this.timer >= 30) {
                this.haunt.setAnimationState(SPIN_ANIMATION);
                this.haunt.setDeltaMovement(this.haunt.twist(this.aimAt(target, SPIN_SPEED), 20.0F, 45.0F));
                this.haunt.playSound(SoundEvents.PHANTOM_SWOOP, 1.0F, 1.2F);
                this.haunt.bounced = false;
                this.timer = 0;
                this.bounces = 0;
                this.maxBounces = 20 + this.haunt.getRandom().nextInt(11);
                this.attackState = 3;
            }
        }

        private void tickSpin(LivingEntity target) {
            this.timer++;
            Vec3 velocity = this.haunt.getDeltaMovement();
            for (LivingEntity entity : this.haunt.level().getEntitiesOfClass(LivingEntity.class, this.haunt.getBoundingBox().expandTowards(velocity), entity -> !(entity instanceof Haunt) && EntitySelector.NO_SPECTATORS.test(entity))) {
                if (entity.hurt(this.haunt.damageSources().mobAttack(this.haunt), (float) this.haunt.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                    entity.knockback(1.2D, -velocity.x, -velocity.z);
                }
            }
            if (this.haunt.bounced || this.timer > 40) {
                this.bounces++;
                if (this.bounces % 4 == 0 || !this.haunt.bounced) {
                    this.haunt.setDeltaMovement(this.aimAt(target, SPIN_SPEED));
                }
                this.haunt.bounced = false;
                this.timer = 0;
                if (this.bounces >= this.maxBounces) {
                    this.haunt.setDeltaMovement(Vec3.ZERO);
                    this.haunt.setAnimationState(0);
                    this.attackCooldown = 100;
                    this.spinCooldown = 100;
                    this.attackState = 0;
                }
            }
        }

        private Vec3 aimAt(LivingEntity target, double speed) {
            return target.getBoundingBox().getCenter().subtract(this.haunt.getBoundingBox().getCenter()).normalize().scale(speed);
        }
    }
}