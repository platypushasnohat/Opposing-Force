package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "sweepAttack", at = @At(value = "HEAD"), cancellable = true)
    public void opposingForce$replaceSweepParticle(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (player.getMainHandItem().is(OFItems.LASER_BLADE.get())) {
            double d0 = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
            double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(OFParticleTypes.LASER_SWEEP.get(), player.getX() + d0, player.getY(0.5F), player.getZ() + d1, 0, d0, 0.0F, d1, 0.0F);
            }
            ci.cancel();
        }
    }
}
