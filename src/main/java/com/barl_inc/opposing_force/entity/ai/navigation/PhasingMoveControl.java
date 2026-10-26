package com.barl_inc.opposing_force.entity.ai.navigation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class PhasingMoveControl extends MoveControl {

    public PhasingMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            Vec3 offset = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
            double distance = offset.length();
            if (distance > 1.0E-4D) {
                double speed = Math.min(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED) * 0.5D, distance * 0.25D);
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().lerp(offset.scale(speed / distance), 0.15D));
                if (this.mob.getTarget() == null && offset.horizontalDistanceSqr() > 1.0E-4D) {
                    this.mob.setYRot(this.rotlerp(this.mob.getYRot(), (float) (Mth.atan2(offset.z, offset.x) * Mth.RAD_TO_DEG) - 90.0F, 20.0F));
                    this.mob.yBodyRot = this.mob.getYRot();
                }
            }
        }
    }
}
