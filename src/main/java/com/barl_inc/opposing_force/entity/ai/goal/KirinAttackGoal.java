package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Kirin;
import com.platypushasnohat.sinew.entity.ai.goal.AttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class KirinAttackGoal extends AttackGoal {

    private final Kirin kirin;
    private int teleportCooldown;
    private int lightningCallCooldown;
    private final List<Player> trackedPlayers = new ArrayList<>();
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
        this.setState(0);
    }

    @Override
    public void stop() {
        super.stop();
        this.trackedPlayers.clear();
        this.strikes.clear();
        if (this.kirin.getAnimationState() == Kirin.TELEPORT_ANIMATION || this.kirin.getAnimationState() == Kirin.LIGHTNING_CALL_ANIMATION) {
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
        if (target != null) {
            switch (this.attackState) {
                case 1 -> this.tickTeleport(target);
                case 2 -> this.tickLightningCall(target);
                default -> this.tickChase(target);
            }
        }
    }

    private void tickChase(LivingEntity target) {
        this.lookAtTarget(target, 30.0F, 30.0F);
        double distance = this.kirin.distanceToSqr(target);
        if (this.lightningCallCooldown <= 0 && !this.kirin.getLightningCallTargets().isEmpty()) {
            this.setState(2);
        } else if (this.teleportCooldown <= 0 && distance < 1024.0D) {
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
            this.teleportNear(target);
        }
        if (this.timer == 7) {
            this.kirin.teleportBurst();
        }
        if (this.timer >= 10) {
            this.kirin.setAnimationState(0);
            this.teleportCooldown = 100;
            this.setState(0);
        }
    }

    private void tickLightningCall(LivingEntity target) {
        this.timer++;
        this.lookAtTarget(target, 30.0F, 30.0F);
        this.kirin.getNavigation().stop();
        if (this.timer == 1) {
            this.kirin.setAnimationState(Kirin.LIGHTNING_CALL_ANIMATION);
            for (Player player : this.kirin.getLightningCallTargets()) {
                this.trackedPlayers.add(player);
                this.strikes.add(player.position());
            }
        }
        if (this.timer > 1 && this.timer <= 15) {
            for (int i = 0; i < this.strikes.size(); i++) {
                Vec3 strike = this.strikes.get(i);
                Vec3 toPlayer = this.trackedPlayers.get(i).position().subtract(strike);
                if (toPlayer.length() > 0.24D) {
                    toPlayer = toPlayer.normalize().scale(0.24D);
                }
                this.strikes.set(i, strike.add(toPlayer));
            }
        }
        if (this.timer == 15) {
            for (Vec3 strike : this.strikes) {
                this.kirin.lightningCall(strike);
            }
        }
        if (this.timer == 18) {
            for (Vec3 strike : this.strikes) {
                this.kirin.lightningCallImpact(strike);
            }
            this.trackedPlayers.clear();
            this.strikes.clear();
        }
        if (this.timer >= 30) {
            this.kirin.setAnimationState(0);
            this.lightningCallCooldown = 240;
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