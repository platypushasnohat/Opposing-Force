package com.barl_inc.opposing_force.entity.post_end;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.network.MountedEntityKeyPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Set;

public class KirinRider {

    private final Kirin kirin;
    private final Set<LivingEntity> dashHits = new HashSet<>();
    private int action;
    private int timer;
    private int midairJumps;
    private int airTicks;
    private boolean wasJumpDown;
    private boolean wasAttackDown;
    private boolean sprinting;
    private boolean airWalking;
    private boolean airWalkLocked;
    private float pendingJump;
    private int sentAnimation = -1;
    private int jumpAnimation;
    private int jumpAnimationTicks;
    private Vec3 dashDirection = Vec3.ZERO;
    private double slamStartY;
    private float slamFallSpeed;
    private int serverDashTicks;
    private Vec3 dashPrevious = Vec3.ZERO;

    public KirinRider(Kirin kirin) {
        this.kirin = kirin;
    }

    public boolean isDashing() {
        return this.action == 1;
    }

    public float getDashYaw() {
        return (float) (Mth.atan2(this.dashDirection.z, this.dashDirection.x) * Mth.RAD_TO_DEG) - 90.0F;
    }

    public boolean isIdle() {
        return this.action == 0;
    }

    public boolean blocksInput() {
        return this.action != 0 || this.airWalking;
    }

    public float getSpeedFactor() {
        return this.sprinting ? 1.3125F : 0.16F;
    }

    public void onPlayerJump(int power) {
        if (power > 0) {
            this.pendingJump = power >= 90 ? 1.0F : 0.4F + 0.6F * power / 90.0F;
        }
    }

    public void tick(Player player) {
        if (this.kirin.isControlledByLocalInstance()) {
            this.tickControls(player);
        }
        if (!this.kirin.level().isClientSide) {
            this.tickDashHits();
        }
    }

    public void onRiderKey(int key) {
        int state = this.kirin.getAnimationState();
        if (key == 1 && this.serverDashTicks == 0) {
            this.serverDashTicks = 1;
            this.dashPrevious = this.kirin.position();
            this.dashHits.clear();
        } else if (key == 2) {
            this.kirin.level().broadcastEntityEvent(this.kirin, Kirin.SLAM_STEP_EVENT);
        } else if (key == 3 && (state == Kirin.SLAM_START_ANIMATION || state == Kirin.SLAM_ANIMATION)) {
            this.kirin.slamImpact(this.kirin::isValidRideVictim, (float) this.kirin.getAttributeValue(Attributes.ATTACK_DAMAGE));
        } else if (key >= 100 && Set.of(0, Kirin.RUN_ANIMATION, Kirin.DASH_ANIMATION, Kirin.JUMP_START_ANIMATION, Kirin.JUMP_FALL_ANIMATION, Kirin.AIR_WALK_ANIMATION, Kirin.JUMP_MIDAIR_ANIMATION, Kirin.SLAM_START_ANIMATION, Kirin.SLAM_ANIMATION, Kirin.SLAM_END_ANIMATION).contains(key - 100)) {
            this.kirin.setAnimationState(key - 100);
        }
    }

    private void attack() {
        if (this.action == 0 && this.kirin.isAlive() && !this.kirin.isSitState()) {
            this.timer = 0;
            if (this.kirin.onGround()) {
                this.action = 1;
                this.dashDirection = Vec3.directionFromRotation(0.0F, this.kirin.getYRot());
                this.sendKey(1);
                this.sendAnimation(Kirin.DASH_ANIMATION);
            } else {
                this.action = 2;
                this.slamStartY = this.kirin.getY();
                this.slamFallSpeed = 0.0F;
                this.sendKey(2);
                this.sendAnimation(Kirin.JUMP_MIDAIR_ANIMATION);
            }
        }
    }

    private void endAction() {
        this.action = 0;
        this.sentAnimation = -1;
    }

    private void sendKey(int key) {
        LivingEntity rider = this.kirin.getControllingPassenger();
        if (rider != null) {
            PacketDistributor.sendToServer(new MountedEntityKeyPacket(this.kirin.getId(), rider.getId(), key));
        }
    }

    private void sendAnimation(int animation) {
        if (animation != this.sentAnimation) {
            this.sentAnimation = animation;
            this.kirin.setAnimationState(animation);
            this.sendKey(100 + animation);
        }
    }

    private void tickControls(Player player) {
        boolean jumpDown = Sinew.PROXY.isKeyDown(0);
        boolean sprintDown = Sinew.PROXY.isKeyDown(1);
        boolean attackDown = Sinew.PROXY.isKeyDown(2);
        if (attackDown && !this.wasAttackDown) {
            this.attack();
        }
        this.wasAttackDown = attackDown;
        boolean grounded = this.kirin.onGround();
        this.sprinting = sprintDown;
        if (grounded) {
            this.midairJumps = 0;
            this.airTicks = 0;
            this.jumpAnimation = 0;
        } else {
            this.airTicks++;
        }
        if (this.action == 1) {
            this.tickDash();
        } else if (this.action == 2) {
            this.tickSlam(player, grounded);
        } else if (this.action == 3) {
            this.tickSlamEnd();
        } else {
            this.tickMovement(player, grounded, jumpDown, sprintDown);
        }
        this.wasJumpDown = jumpDown;
    }

    private void tickMovement(Player player, boolean grounded, boolean jumpDown, boolean sprintDown) {
        Vec3 motion = this.kirin.getDeltaMovement();
        boolean jumped = grounded && this.pendingJump > 0.0F;
        this.airWalkLocked = sprintDown && (grounded || this.airWalkLocked);
        this.airWalking = !grounded && sprintDown && !this.airWalkLocked;
        int animation = this.sentAnimation;
        this.jumpAnimationTicks++;
        if (jumped) {
            Vec3 boost = player.zza > 0.0F ? Vec3.directionFromRotation(0.0F, this.kirin.getYRot()).scale(0.6D * this.pendingJump) : Vec3.ZERO;
            this.kirin.setDeltaMovement(motion.x + boost.x, 1.5D * this.pendingJump, motion.z + boost.z);
            this.kirin.hasImpulse = true;
            this.pendingJump = 0.0F;
            CommonHooks.onLivingJump(this.kirin);
            this.jumpAnimation = Kirin.JUMP_START_ANIMATION;
            this.jumpAnimationTicks = 0;
            this.sendKey(2);
        } else if (this.airWalking) {
            Vec3 walk = this.steer(player, 0.3D);
            this.kirin.setDeltaMovement(walk.x, 0.0D, walk.z);
        } else if (!grounded && jumpDown && !this.wasJumpDown && this.midairJumps < 2) {
            Vec3 forward = Vec3.directionFromRotation(0.0F, this.kirin.getYRot()).scale(0.4D);
            this.midairJumps++;
            this.kirin.setDeltaMovement(motion.x + forward.x, 0.9D, motion.z + forward.z);
            this.jumpAnimation = Kirin.JUMP_MIDAIR_ANIMATION;
            this.jumpAnimationTicks = 0;
            this.sendKey(2);
        }
        if (jumped) {
            animation = Kirin.JUMP_START_ANIMATION;
        } else if (grounded) {
            animation = sprintDown && this.kirin.getDeltaMovement().horizontalDistanceSqr() > 0.0025D ? Kirin.RUN_ANIMATION : 0;
        } else if (this.airWalking) {
            animation = Kirin.AIR_WALK_ANIMATION;
        } else if (this.jumpAnimation != 0 && this.jumpAnimationTicks < 10) {
            animation = this.jumpAnimation;
        } else if (this.airTicks > 3) {
            animation = Kirin.JUMP_FALL_ANIMATION;
        }
        this.sendAnimation(animation);
    }

    private Vec3 steer(Player player, double speed) {
        Vec3 input = new Vec3(player.xxa, 0.0D, player.zza).normalize();
        float sin = Mth.sin(this.kirin.getYRot() * Mth.DEG_TO_RAD);
        float cos = Mth.cos(this.kirin.getYRot() * Mth.DEG_TO_RAD);
        return new Vec3(input.x * cos - input.z * sin, 0.0D, input.z * cos + input.x * sin).scale(speed);
    }

    private void tickDash() {
        this.timer++;
        double speed = 0.0D;
        if (this.timer > 16 && this.timer <= 31) {
            speed = 1.75D;
        } else if (this.timer > 31) {
            speed = 0.6D * (1.0D - (this.timer - 31) / 20.0D);
        }
        this.kirin.setDeltaMovement(this.dashDirection.x * speed, this.kirin.getDeltaMovement().y, this.dashDirection.z * speed);
        if (this.timer >= 51) {
            this.endAction();
        }
    }

    private void tickSlam(Player player, boolean grounded) {
        this.timer++;
        if (this.timer == 12) {
            this.sendAnimation(Kirin.SLAM_START_ANIMATION);
        }
        if (this.timer == 42) {
            this.sendAnimation(Kirin.SLAM_ANIMATION);
        }
        if (this.timer <= 37) {
            double hover = Math.max(this.timer - 12, 0);
            double height = Kirin.slamRise(Math.min(this.timer, 12)) - 0.001D * hover * hover;
            Vec3 steer = this.timer <= 32 ? this.steer(player, 0.3D) : Vec3.ZERO;
            this.kirin.setDeltaMovement(steer.x, this.slamStartY + height - this.kirin.getY(), steer.z);
        } else if (grounded) {
            this.sendKey(3);
            this.sendAnimation(Kirin.SLAM_END_ANIMATION);
            this.action = 3;
            this.timer = 0;
        } else {
            this.slamFallSpeed = Math.min(this.slamFallSpeed + 0.15F, 2.5F);
            this.kirin.setDeltaMovement(0.0D, -0.2D - this.slamFallSpeed, 0.0D);
            if (this.timer >= 200) {
                this.endAction();
            }
        }
    }

    private void tickSlamEnd() {
        this.timer++;
        this.kirin.setDeltaMovement(0.0D, this.kirin.getDeltaMovement().y, 0.0D);
        if (this.timer >= 20) {
            this.endAction();
        }
    }

    private void tickDashHits() {
        if (this.serverDashTicks > 0) {
            this.serverDashTicks++;
            Vec3 current = this.kirin.position();
            Vec3 travel = current.subtract(this.dashPrevious).multiply(1.0D, 0.0D, 1.0D);
            if (this.serverDashTicks >= 16 && this.serverDashTicks <= 40 && travel.lengthSqr() > 1.0E-4D) {
                AABB box = this.kirin.getBoundingBox().inflate(0.5D, 0.0D, 0.5D).expandTowards(travel.reverse());
                for (LivingEntity living : this.kirin.level().getEntitiesOfClass(LivingEntity.class, box, this.kirin::isValidRideVictim)) {
                    if (this.dashHits.add(living)) {
                        this.kirin.dashHit(living, travel.normalize());
                    }
                }
            }
            this.dashPrevious = current;
            if (this.serverDashTicks > 55) {
                this.serverDashTicks = 0;
                this.dashHits.clear();
            }
        }
    }
}