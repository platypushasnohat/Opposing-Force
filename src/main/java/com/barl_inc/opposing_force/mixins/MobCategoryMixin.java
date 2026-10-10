package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.config.OFConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(MobCategory.class)
public class MobCategoryMixin {

    @Shadow
    @Final
    private String name;

    @Shadow
    @Mutable
    @Final
    private static MobCategory[] $VALUES;

    @ModifyReturnValue(method = "getMaxInstancesPerChunk", at = @At("RETURN"))
    private int opposingForce$getMaxInstancesPerChunk(int maxInstancesPerChunk) {
        if (OFConfig.INCREASE_MONSTER_SPAWN_CAP.get() && this.name.equals(MobCategory.MONSTER.getName())) {
            return OFConfig.MONSTER_SPAWN_CAP.get();
        }
        return maxInstancesPerChunk;
    }

    @Invoker("<init>")
    public static MobCategory newCategory(String name, int ordinal, String serializedName, int max, boolean isFriendly, boolean isPersistent, int noDespawnDistance) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/MobCategory;$VALUES:[Lnet/minecraft/world/entity/MobCategory;", shift = At.Shift.AFTER, opcode = 179))
    private static void opposingForce$addCustomCategories(CallbackInfo ci) {
        List<MobCategory> categories = new ArrayList<>(Arrays.asList($VALUES));
        MobCategory last = categories.getLast();
        int nextOrdinal = last.ordinal() + 1;
        MobCategory aquiferMonster = newCategory("OPPOSING_FORCE_AQUIFER_MONSTER", nextOrdinal, "opposing_force:aquifer_monster", 2, false, false, 128);
        categories.add(aquiferMonster);
        $VALUES = categories.toArray(new MobCategory[0]);
    }
}
