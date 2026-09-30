package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.config.OFConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobCategory.class)
public class MobCategoryMixin {

    @Shadow
    @Final
    private String name;

    @ModifyReturnValue(method = "getMaxInstancesPerChunk", at = @At("RETURN"))
    private int opposingForce$getMaxInstancesPerChunk(int maxInstancesPerChunk) {
        if (OFConfig.INCREASE_MONSTER_SPAWN_CAP.get() && this.name.equals(MobCategory.MONSTER.getName())) {
            return OFConfig.MONSTER_SPAWN_CAP.get();
        }
        return maxInstancesPerChunk;
    }
}
