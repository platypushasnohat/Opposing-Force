package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Kirin;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KirinAttackGoal extends AttackGoal {

    private final Kirin kirin;
    private int teleportCooldown;
    private int lightningCallCooldown;
    private int dashCooldown;
    private int leapCooldown;
    private double leapStartY;
    private int airAttack;
    private int airAttackTimer;
    private boolean airAttackUsed;
    private Vec3 airGoal = Vec3.ZERO;
    private boolean dashQueued;
    private boolean leapQueued;
    private boolean slamQueued;
    private int slamCooldown;
    private double slamStartY;
    private float slamFallSpeed;
    private Vec3 slamOrigin = Vec3.ZERO;
    private Vec3 slamFirstGoal = Vec3.ZERO;
    private Vec3 slamSecondStart = Vec3.ZERO;
    private int slamSecondSide;
    private int blinks;
    private final Set<LivingEntity> dashHitPlayers = new HashSet<>();
    private final List<LivingEntity> trackedPlayers = new ArrayList<>();
    private final List<Vec3> strikes = new ArrayList<>();

    public KirinAttackGoal(Kirin kirin) {
        super(kirin);
        this.kirin = kirin;
    }

    @Override
    public void start() {
        super.start();
        this.teleportCooldown = 20;
        this.lightningCallCooldown = 40;
        this.dashCooldown = 60;
        this.leapCooldown = 120;
        this.dashQueued = false;
        this.leapQueued = false;
        this.slamQueued = false;
        this.slamCooldown = 160;
        this.setState(0);
    }

    @Override
    public void stop() {
        super.stop();
        this.trackedPlayers.clear();
        this.strikes.clear();
        this.dashHitPlayers.clear();
        this.dashQueued = false;
        this.leapQueued = false;
        this.slamQueued = false;
        this.kirin.setNoGravity(false);
        if (this.kirin.getAnimationState() >= Kirin.TELEPORT_ANIMATION) {
            this.kirin.setAnimationState(0);
        }
    }

    @Override
    public void tick() {
        LivingEntity target = this.kirin.getTarget();
        if (this.teleportCooldown > 0) {
            this.teleportCooldown--;
        }
        if (this.lightningCallCooldown > 0) {
            this.lightningCallCooldown--;
        }
        if (this.dashCooldown > 0) {
            this.dashCooldown--;
        }
        if (this.leapCooldown > 0) {
            this.leapCooldown--;
        }
        if (this.slamCooldown > 0) {
            this.slamCooldown--;
        }
        if (target != null) {
            switch (this.attackState) {
                case 1 -> this.tickTeleport(target);
                case 2 -> this.tickLightningCall(target);
                case 3 -> this.tickDash(target);
                case 4 -> this.tickLeap(target);
                case 5 -> this.tickAirWalk(target);
                case 6 -> this.tickSlam(target);
                case 7 -> this.tickSlamEnd();
                default -> this.tickChase(target);
            }
        }
    }

    private void tickChase(LivingEntity target) {
        this.lookAtTarget(target, 30.0F, 30.0F);
        double distance = this.kirin.distanceToSqr(target);
        boolean grounded = this.kirin.onGround();
        if (grounded && this.lightningCallCooldown <= 0 && !this.kirin.getLightningCallTargets().isEmpty()) {
            this.setState(2);
        } else if (grounded && this.dashCooldown <= 0) {
            this.dashQueued = true;
            this.setState(1);
        } else if (grounded && this.leapCooldown <= 0) {
            this.leapQueued = true;
            this.setState(1);
        } else if (grounded && this.slamCooldown <= 0) {
            this.slamQueued = true;
            this.setState(1);
        } else if (grounded && this.teleportCooldown <= 0 && distance < 1024.0D) {
            this.setState(1);
        } else if (distance > 12.25D) {
            if (this.kirin.getNavigation().isDone() || this.kirin.tickCount % 10 == 0) {
                this.kirin.getNavigation().moveTo(target, 0.8D);
            }
        } else {
            this.kirin.getNavigation().stop();
        }
    }

    private void tickTeleport(LivingEntity target) {
        this.timer++;
        this.kirin.getNavigation().stop();
        this.kirin.setDeltaMovement(0.0D, this.kirin.getDeltaMovement().y, 0.0D);
        if (this.timer == 1) {
            this.kirin.setAnimationState(Kirin.TELEPORT_ANIMATION);
        }
        if (this.timer == 2) {
            this.kirin.teleportBurst();
        }
        if (this.timer == 4) {
            if (this.dashQueued) {
                this.teleportFar(target, 20.0D, 16.0D);
            } else if (this.leapQueued) {
                this.teleportFar(target, 16.0D, 11.0D);
            } else if (this.slamQueued) {
                this.teleportFar(target, 25.0D, 19.0D);
            } else {
                this.teleportNear(target);
            }
        }
        if (this.timer == 7) {
            this.kirin.teleportBurst();
        }
        if (this.timer >= 10) {
            this.kirin.setAnimationState(0);
            this.teleportCooldown = 60;
            if (this.dashQueued) {
                this.dashQueued = false;
                this.setState(3);
            } else if (this.leapQueued) {
                this.leapQueued = false;
                this.setState(4);
            } else if (this.slamQueued) {
                this.slamQueued = false;
                this.setState(6);
            } else {
                this.setState(0);
            }
        }
    }

    private void tickDash(LivingEntity target) {
        this.timer++;
        this.kirin.getNavigation().stop();
        this.kirin.setDeltaMovement(0.0D, this.kirin.getDeltaMovement().y, 0.0D);
        if (this.timer == 1) {
            this.kirin.setAnimationState(Kirin.DASH_ANIMATION);
            this.blinks = 0;
            this.dashHitPlayers.clear();
        }
        if (this.timer < 16) {
            this.lookAtTarget(target, 30.0F, 30.0F);
            this.faceTowards(target.position());
            this.kirin.dashChargeParticles();
        }
        if (this.timer == 18 || this.timer == 21 || this.timer == 26 || this.timer == 31) {
            this.blink(target);
        }
        if (this.timer > 31 && this.timer <= 51) {
            this.kirin.dashSkidParticles(this.timer - 31);
        }
        if (this.timer >= 51) {
            this.kirin.setAnimationState(0);
            this.dashCooldown = 160;
            this.teleportCooldown = Math.max(this.teleportCooldown, 30);
            this.dashHitPlayers.clear();
            this.setState(0);
        }
    }

    private void blink(LivingEntity target) {
        Vec3 start = this.kirin.position();
        Vec3 toTarget = target.position().subtract(start).multiply(1.0D, 0.0D, 1.0D);
        double distance = toTarget.length();
        Vec3 direction = Vec3.directionFromRotation(0.0F, this.kirin.getYRot());
        if (distance > 1.0E-4D) {
            direction = toTarget.normalize();
        }
        double step = this.blinks == 0 ? 7.0D : 6.5D;
        boolean reaches = distance <= step + 2.0D || (this.blinks == 3 && distance <= 12.0D);
        if (reaches) {
            step = distance + 3.0D;
        }
        List<Vec3> path = this.sweep(start, direction, step);
        Vec3 end = path.get(path.size() - 1);
        boolean blocked = end.subtract(start).horizontalDistance() < step - 0.3D;
        for (Vec3 point : path) {
            AABB box = this.kirin.getBoundingBox().move(point.subtract(start)).inflate(0.5D, 0.0D, 0.5D);
            for (LivingEntity player : this.kirin.level().getEntitiesOfClass(LivingEntity.class, box, this.kirin::isValidVictim)) {
                if (this.dashHitPlayers.add(player)) {
                    this.kirin.dashHit(player, direction);
                }
            }
        }
        this.kirin.teleportTo(end.x, end.y, end.z);
        this.faceTowards(end.add(direction));
        this.kirin.dashBlinkSound(start);
        this.blinks++;
        if ((reaches || blocked) && this.timer < 31) {
            this.kirin.level().broadcastEntityEvent(this.kirin, Kirin.DASH_SKIP_EVENT);
            this.timer = 31;
        }
    }

    private List<Vec3> sweep(Vec3 start, Vec3 direction, double step) {
        List<Vec3> path = new ArrayList<>();
        path.add(start);
        Vec3 current = start;
        boolean stopped = false;
        int samples = Mth.ceil(step / 0.25D);
        for (int i = 1; i <= samples && !stopped; i++) {
            Vec3 next = start.add(direction.scale(step * i / samples));
            Vec3 fitted = this.fit(new Vec3(next.x, current.y, next.z), start);
            if (fitted == null) {
                stopped = true;
            } else {
                current = fitted;
                path.add(current);
            }
        }
        return path;
    }

    private Vec3 fit(Vec3 position, Vec3 start) {
        double[] rises = {0.0D, 0.5D, 1.0D, 1.2D};
        Vec3 fitted = null;
        for (int i = 0; i < rises.length && fitted == null; i++) {
            Vec3 raised = position.add(0.0D, rises[i], 0.0D);
            if (this.kirin.level().noCollision(this.kirin, this.kirin.getBoundingBox().move(raised.subtract(start)))) {
                fitted = raised;
            }
        }
        return fitted;
    }

    private void faceTowards(Vec3 position) {
        float yaw = (float) (Mth.atan2(position.z - this.kirin.getZ(), position.x - this.kirin.getX()) * Mth.RAD_TO_DEG) - 90.0F;
        this.kirin.setYRot(yaw);
        this.kirin.setYBodyRot(yaw);
        this.kirin.setYHeadRot(yaw);
    }

    private void teleportFar(LivingEntity target, double maxDistance, double minDistance) {
        Vec3 spot = null;
        for (int pass = 0; pass < 2 && spot == null; pass++) {
            for (int i = 0; i < 12 && spot == null; i++) {
                float angle = this.kirin.getRandom().nextFloat() * Mth.TWO_PI;
                double distance = maxDistance - (i / 4) * (maxDistance - minDistance) / 2.0D;
                Vec3 ground = this.findGround(target.position().add(Mth.cos(angle) * distance, 0.0D, Mth.sin(angle) * distance));
                if (ground != null && (pass == 1 || this.canSee(ground, target))) {
                    spot = ground;
                }
            }
        }
        if (spot != null) {
            this.kirin.teleportTo(spot.x, spot.y, spot.z);
            this.faceTowards(target.position());
        }
    }

    private boolean canSee(Vec3 spot, LivingEntity target) {
        Vec3 eye = spot.add(0.0D, this.kirin.getEyeHeight(), 0.0D);
        return this.kirin.level().clip(new ClipContext(eye, target.getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.kirin)).getType() == HitResult.Type.MISS;
    }

    private void tickLightningCall(LivingEntity target) {
        this.timer++;
        this.lookAtTarget(target, 30.0F, 30.0F);
        this.kirin.getNavigation().stop();
        if (this.timer == 1) {
            this.kirin.setAnimationState(Kirin.LIGHTNING_CALL_ANIMATION);
        }
        this.tickStrikes(this.timer);
        if (this.timer >= 30) {
            this.kirin.setAnimationState(0);
            this.lightningCallCooldown = 240;
            this.setState(0);
        }
    }

    private void tickStrikes(int time) {
        if (time == 1) {
            for (LivingEntity player : this.kirin.getLightningCallTargets()) {
                this.trackedPlayers.add(player);
                this.strikes.add(player.position());
            }
        }
        if (time > 1 && time <= 15) {
            for (int i = 0; i < this.strikes.size(); i++) {
                Vec3 strike = this.strikes.get(i);
                Vec3 toPlayer = this.trackedPlayers.get(i).position().subtract(strike);
                if (toPlayer.length() > 0.24D) {
                    toPlayer = toPlayer.normalize().scale(0.24D);
                }
                this.strikes.set(i, strike.add(toPlayer));
            }
        }
        if (time == 15) {
            for (Vec3 strike : this.strikes) {
                this.kirin.lightningCall(strike);
            }
        }
        if (time == 18) {
            for (Vec3 strike : this.strikes) {
                this.kirin.lightningCallImpact(strike);
            }
            this.trackedPlayers.clear();
            this.strikes.clear();
        }
    }

    private void tickLeap(LivingEntity target) {
        this.timer++;
        this.kirin.getNavigation().stop();
        this.lookAtTarget(target, 30.0F, 30.0F);
        if (this.timer == 1) {
            this.kirin.setAnimationState(Kirin.JUMP_START_ANIMATION);
            this.kirin.setNoGravity(true);
            this.leapStartY = this.kirin.getY();
            this.faceTowards(target.position());
        }
        if (this.timer == 11) {
            this.kirin.setAnimationState(Kirin.JUMP_FALL_ANIMATION);
        }
        double height = 0.0D;
        double forwardSpeed = 0.0D;
        if (this.timer > 5 && this.timer <= 17) {
            forwardSpeed = 0.25D;
            float rise = (this.timer - 5) / 12.0F;
            height = 9.5D * (1.0D - (1.0D - rise) * (1.0D - rise));
        } else if (this.timer > 17) {
            forwardSpeed = 0.1D;
            float fall = Math.min((this.timer - 17) / 14.0F, 1.0F);
            height = 9.5D - 3.5D * fall * fall;
        }
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.kirin.getYRot()).scale(forwardSpeed);
        this.kirin.setDeltaMovement(forward.x, this.leapStartY + height - this.kirin.getY(), forward.z);
        if (this.timer >= 31) {
            this.kirin.setAnimationState(Kirin.AIR_WALK_ANIMATION);
            this.airAttack = 0;
            this.airAttackTimer = 0;
            this.airAttackUsed = false;
            Vec3 toTarget = target.position().subtract(this.kirin.position()).multiply(1.0D, 0.0D, 1.0D);
            double sideDistance = 7.0D;
            if (this.kirin.getRandom().nextBoolean()) {
                sideDistance = -sideDistance;
            }
            this.airGoal = target.position();
            if (toTarget.lengthSqr() > 1.0E-4D) {
                this.airGoal = target.position().add(new Vec3(-toTarget.z, 0.0D, toTarget.x).normalize().scale(sideDistance));
            }
            this.setState(5);
        }
    }

    private void tickAirWalk(LivingEntity target) {
        this.timer++;
        this.kirin.getNavigation().stop();
        this.lookAtTarget(target, 30.0F, 30.0F);
        Vec3 toGoal = this.airGoal.subtract(this.kirin.position()).multiply(1.0D, 0.0D, 1.0D);
        Vec3 move = Vec3.ZERO;
        if (toGoal.length() > 1.0D) {
            move = toGoal.normalize().scale(0.3D);
        }
        this.faceTowards(target.position());
        this.kirin.setDeltaMovement(move.x, -0.15D, move.z);
        if (this.timer == 10 && !this.airAttackUsed) {
            this.airAttackUsed = true;
            this.airAttackTimer = 0;
            if (!this.kirin.getLightningCallTargets().isEmpty() && this.kirin.getRandom().nextBoolean()) {
                this.airAttack = 1;
                this.kirin.setAnimationState(Kirin.LIGHTNING_CALL_AIR_ANIMATION);
            } else {
                this.airAttack = 2;
                this.kirin.setAnimationState(this.kirin.getRandom().nextBoolean() ? Kirin.LIGHTNING_BALL_ONE_ANIMATION : Kirin.LIGHTNING_BALL_TWO_ANIMATION);
            }
        }
        if (this.airAttack != 0) {
            this.airAttackTimer++;
            if (this.airAttack == 1) {
                this.tickStrikes(this.airAttackTimer);
            } else {
                int[] fireTimes = {11, 16, 21};
                for (int i = 0; i < fireTimes.length; i++) {
                    if (this.airAttackTimer == fireTimes[i]) {
                        this.kirin.lightningBall(target, i);
                    }
                }
            }
            if (this.airAttackTimer >= 30) {
                this.airAttack = 0;
                this.trackedPlayers.clear();
                this.strikes.clear();
                this.kirin.setAnimationState(Kirin.AIR_WALK_ANIMATION);
            }
        }
        if ((this.kirin.onGround() && this.timer > 2) || this.timer >= 200) {
            this.kirin.setNoGravity(false);
            this.kirin.setAnimationState(0);
            this.airAttack = 0;
            this.trackedPlayers.clear();
            this.strikes.clear();
            this.leapCooldown = 200;
            this.teleportCooldown = Math.max(this.teleportCooldown, 20);
            this.setState(0);
        }
    }

    private void tickSlam(LivingEntity target) {
        this.timer++;
        this.kirin.getNavigation().stop();
        Vec3 toTarget = target.position().subtract(this.kirin.position()).multiply(1.0D, 0.0D, 1.0D);
        Vec3 forward = toTarget.lengthSqr() > 1.0E-4D ? toTarget.normalize() : Vec3.directionFromRotation(0.0F, this.kirin.getYRot());
        Vec3 left = new Vec3(forward.z, 0.0D, -forward.x);
        if (this.timer == 1) {
            this.kirin.setAnimationState(Kirin.JUMP_START_ANIMATION);
            this.kirin.setNoGravity(true);
            this.slamStartY = this.kirin.getY();
            this.slamFallSpeed = 0.0F;
            this.slamOrigin = this.kirin.position();
            this.slamSecondSide = this.kirin.getRandom().nextBoolean() ? 1 : -1;
            this.slamFirstGoal = this.slamOrigin.add(forward.scale(toTarget.length() * 0.5D)).add(left.scale(this.slamSecondSide * -6.0D));
        }
        if (this.timer == 4) {
            this.kirin.level().broadcastEntityEvent(this.kirin, Kirin.SLAM_STEP_EVENT);
        }
        if (this.timer == 11) {
            this.kirin.setAnimationState(Kirin.JUMP_FALL_ANIMATION);
        }
        if (this.timer == 32) {
            this.slamSecondStart = this.kirin.position();
        }
        if (this.timer == 30) {
            this.kirin.setAnimationState(this.slamSecondSide > 0 ? Kirin.JUMP_MIDAIR_LEFT_ANIMATION : Kirin.JUMP_MIDAIR_RIGHT_ANIMATION);
        }
        if (this.timer == 33) {
            this.kirin.level().broadcastEntityEvent(this.kirin, Kirin.SLAM_STEP_EVENT);
        }
        if (this.timer == 44) {
            this.kirin.setAnimationState(Kirin.SLAM_START_ANIMATION);
        }
        if (this.timer == 74) {
            this.kirin.setAnimationState(Kirin.SLAM_ANIMATION);
        }
        if (this.timer < 69) {
            this.lookAtTarget(target, 30.0F, 30.0F);
            this.faceTowards(target.position());
            Vec3 drift = Vec3.ZERO;
            if (this.timer > 3 && this.timer <= 32) {
                float progress = (this.timer - 3) / 29.0F;
                Vec3 desired = this.slamOrigin.lerp(this.slamFirstGoal, progress);
                drift = desired.subtract(this.kirin.position());
            } else if (this.timer > 32 && this.timer <= 44) {
                float progress = (this.timer - 32) / 12.0F;
                Vec3 desired = this.slamSecondStart.lerp(target.position(), this.easeOut(progress)).add(left.scale(this.slamSecondSide * Mth.sin(progress * Mth.PI) * 3.0D));
                drift = desired.subtract(this.kirin.position());
            } else if (this.timer > 44 && this.timer <= 64) {
                drift = toTarget.normalize().scale(Math.min(0.3D, toTarget.length()));
            }
            drift = drift.multiply(1.0D, 0.0D, 1.0D);
            if (drift.length() > 1.5D) {
                drift = drift.normalize().scale(1.5D);
            }
            this.kirin.setDeltaMovement(drift.x, this.slamStartY + this.slamHeight(this.timer) - this.kirin.getY(), drift.z);
        } else {
            this.slamFallSpeed = Math.min(this.slamFallSpeed + 0.15F, 2.5F);
            Vec3 drift = toTarget.lengthSqr() > 1.0E-4D ? toTarget.normalize().scale(Math.min(0.03D, toTarget.length())) : Vec3.ZERO;
            this.kirin.setDeltaMovement(drift.x, -0.2D - this.slamFallSpeed, drift.z);
            if (this.kirin.onGround() || this.timer >= 200) {
                this.kirin.setNoGravity(false);
                this.kirin.setAnimationState(Kirin.SLAM_END_ANIMATION);
                this.kirin.slamImpact();
                this.setState(7);
            }
        }
    }

    private float easeOut(float progress) {
        float inverse = 1.0F - progress;
        return 1.0F - inverse * inverse * inverse;
    }

    private double slamHeight(int time) {
        double height = 0.0D;
        if (time > 3 && time <= 15) {
            double air = time - 3;
            height = 1.833D * air - 0.0764D * air * air;
        } else if (time > 15 && time <= 32) {
            double air = time - 15;
            height = 11.0D - 0.0329D * air * air;
        } else if (time > 32 && time <= 44) {
            double air = time - 32;
            height = 1.5D + 1.583D * air - 0.066D * air * air;
        } else if (time > 44) {
            double air = time - 44;
            height = 11.0D - 0.001D * air * air;
        }
        return height;
    }

    private void tickSlamEnd() {
        this.timer++;
        this.kirin.getNavigation().stop();
        this.kirin.setDeltaMovement(0.0D, this.kirin.getDeltaMovement().y, 0.0D);
        if (this.timer >= 20) {
            this.kirin.setAnimationState(0);
            this.slamCooldown = 400;
            this.teleportCooldown = Math.max(this.teleportCooldown, 20);
            this.setState(0);
        }
    }

    private void teleportNear(LivingEntity target) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, target.getYHeadRot());
        Vec3 side = new Vec3(-forward.z, 0.0D, forward.x);
        Vec3[] directions = {forward.reverse(), side, side.reverse()};
        int first = this.kirin.getRandom().nextInt(directions.length);
        boolean found = false;
        for (int i = 0; i < directions.length * 3 && !found; i++) {
            Vec3 direction = directions[(first + i) % directions.length];
            double distance = 6.0D - (i / directions.length) * 1.5D;
            Vec3 spot = this.findGround(target.position().add(direction.scale(distance)));
            if (spot != null) {
                this.kirin.teleportTo(spot.x, spot.y, spot.z);
                float yaw = (float) (Mth.atan2(target.getZ() - spot.z, target.getX() - spot.x) * Mth.RAD_TO_DEG) - 90.0F;
                this.kirin.setYRot(yaw);
                this.kirin.setYBodyRot(yaw);
                this.kirin.setYHeadRot(yaw);
                found = true;
            }
        }
    }

    private Vec3 findGround(Vec3 position) {
        BlockPos start = BlockPos.containing(position.x, position.y + 3.0D, position.z);
        Vec3 ground = null;
        for (int i = 0; i <= 7 && ground == null; i++) {
            BlockPos pos = start.below(i);
            Vec3 spot = new Vec3(position.x, pos.getY(), position.z);
            boolean solidBelow = this.kirin.level().getBlockState(pos.below()).isFaceSturdy(this.kirin.level(), pos.below(), Direction.UP);
            boolean clear = this.kirin.level().noCollision(this.kirin, this.kirin.getBoundingBox().move(spot.subtract(this.kirin.position())));
            boolean dry = this.kirin.level().getFluidState(pos).isEmpty();
            if (solidBelow && clear && dry) {
                ground = spot;
            }
        }
        return ground;
    }

    private void setState(int state) {
        this.attackState = state;
        this.timer = 0;
    }
}