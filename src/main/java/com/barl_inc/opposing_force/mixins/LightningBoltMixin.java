package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.entity.misc.SuperLightningBolt;
import com.barl_inc.opposing_force.utils.OFLightning;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {

    @Shadow
    private int life;

    @Shadow
    private boolean visualOnly;

    @Shadow
    @Final
    private Set<Entity> hitEntities;

    @Inject(at = @At("HEAD"), method = "spawnFire", cancellable = true)
    private void opposingForce$spawnFire(int extraIgnitions, CallbackInfo ci) {
        if (OFLightning.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void opposingForce$impact(CallbackInfo ci) {
        LightningBolt bolt = (LightningBolt) (Object) this;
        if (this.life == 2 && !this.visualOnly && OFLightning.isEnabled() && bolt.level() instanceof ServerLevel level) {
            if (bolt instanceof SuperLightningBolt) {
                OFLightning.impact(level, bolt, 9.0D, 24.0F, 900);
            } else {
                OFLightning.impact(level, bolt, 5.0D, 10.0F, 400);
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void opposingForce$clearFire(CallbackInfo ci) {
        if (OFLightning.isEnabled()) {
            this.hitEntities.forEach(Entity::clearFire);
        }
    }
}