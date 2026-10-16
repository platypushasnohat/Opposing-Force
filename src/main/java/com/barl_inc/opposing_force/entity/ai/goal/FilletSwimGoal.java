package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Fillet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FilletSwimGoal extends Goal {

    private final Fillet fillet;
    private final int radius;
    private final int height;
    private float circleDistance = 3.0F;
    private boolean clockwise = false;

    public FilletSwimGoal(Fillet fillet) {
        this(fillet, 10, 7);
    }

    public FilletSwimGoal(Fillet fillet, int radius, int height) {
        this.fillet = fillet;
        this.radius = radius;
        this.height = height;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.fillet.isGroupLeader() || this.fillet.hasNoLeader() || this.fillet.hasGroupLeader() && this.fillet.groupLeader.circlePos != null;
    }

    @Override
    public void tick() {
        if (this.fillet.circleTime > this.fillet.maxCircleTime) {
            this.fillet.circleTime = 0;
            this.fillet.circlePos = null;
        }
        if (this.fillet.circlePos != null) {
            this.fillet.circleTime++;
            Vec3 orbitPos = this.orbitAroundPos(this.fillet.circlePos);
            this.fillet.getNavigation().moveTo(orbitPos.x, orbitPos.y, orbitPos.z, this.fillet.circleSpeed);
        }
        else if (this.fillet.isGroupLeader()) {
            if (this.fillet.baitballCooldown == 0) {
                this.fillet.baitballCooldown = 100 + this.fillet.getRandom().nextInt(150);
                if (this.fillet.circlePos == null || this.fillet.circleTime >= this.fillet.maxCircleTime) {
                    this.fillet.circleTime = 0;
                    this.fillet.maxCircleTime = 100 + this.fillet.getRandom().nextInt(200);
                    this.circleDistance = 1 + this.fillet.getRandom().nextFloat() * 1.5F;
                    this.fillet.circleSpeed = 0.75F + this.fillet.getRandom().nextFloat() * 0.5F;
                    this.clockwise = this.fillet.getRandom().nextBoolean();
                    this.fillet.circlePos = this.getPosition();
                }
            }
        }
        else if (this.fillet.getRandom().nextInt(40) == 0 || this.fillet.hasNoLeader()) {
            Vec3 position = this.getPosition();
            if (position != null) {
                this.fillet.getNavigation().moveTo(position.x, position.y, position.z, 1.0F);
            }
        }
        else if (this.fillet.hasGroupLeader() && this.fillet.groupLeader.circlePos != null) {
            if (this.fillet.circlePos == null) {
                this.fillet.circlePos = this.fillet.groupLeader.circlePos;
                this.fillet.circleTime = this.fillet.groupLeader.circleTime;
                this.fillet.maxCircleTime = this.fillet.groupLeader.maxCircleTime;
                this.circleDistance = 1 + this.fillet.getRandom().nextFloat() * 1.5F;
                this.clockwise = this.fillet.getRandom().nextBoolean();
                this.fillet.circleSpeed = 0.75F + this.fillet.getRandom().nextFloat() * 0.5F;
            }
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        return BehaviorUtils.getRandomSwimmablePos(this.fillet, this.radius, this.height);
    }

    public Vec3 orbitAroundPos(Vec3 target) {
        float progress = 1.0F - (this.fillet.circleTime / (float) this.fillet.maxCircleTime);
        float angle = (0.0174532925F * 10.0F * this.fillet.circleSpeed * (this.clockwise ? -this.fillet.circleTime : this.fillet.circleTime));
        double extraX = (this.circleDistance * progress + 1.75F) * Mth.sin((angle));
        double extraY = Math.sin(1F + this.fillet.getId() * 0.2F + this.fillet.circleTime * 0.2F);
        double extraZ = (this.circleDistance * progress + 1.75F) * progress * Mth.cos(angle);
        return new Vec3(target.x + 0.5F + extraX, target.y + 0.5F + extraY, target.z + 0.5F + extraZ);
    }
}