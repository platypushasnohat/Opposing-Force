package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.entity.post_end.Kirin;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    public abstract Vec3 getPosition();

    @Shadow
    protected abstract void setPosition(Vec3 position);

    @Inject(method = "setup", at = @At("TAIL"))
    private void opposingForce$raiseKirinRider(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!detached && entity.getVehicle() instanceof Kirin) {
            this.setPosition(this.getPosition().add(0.0D, 0.5D, 0.0D));
        }
    }
}
