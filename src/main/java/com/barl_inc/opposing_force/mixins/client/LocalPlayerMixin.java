package com.barl_inc.opposing_force.mixins.client;

import com.barl_inc.opposing_force.registry.OFItems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Unique
    private static final float BUZZSAW_MOVEMENT_MULTIPLIER = 0.75F;

    @WrapOperation(method = {"aiStep", "canStartSprinting"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean opposingForce$canStartSprinting(LocalPlayer player, Operation<Boolean> original) {
        if (original.call(player)) {
            return !this.playerHasBuzzsaw(player);
        }
        return false;
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue = 0.2F, ordinal = 0))
    private float opposingForce$moveFasterWhileUsingItemLeft(float value) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (this.playerHasBuzzsaw(player)) {
            return BUZZSAW_MOVEMENT_MULTIPLIER;
        }
        return value;
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue = 0.2F, ordinal = 1))
    private float opposingForce$moveFasterWhileUsingItemForward(float value) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (this.playerHasBuzzsaw(player)) {
            return BUZZSAW_MOVEMENT_MULTIPLIER;
        }
        return value;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean playerHasBuzzsaw(LocalPlayer player) {
        if (player.isUsingItem()) {
            ItemStack useItem = player.getUseItem();
            return useItem.is(OFItems.BUZZSAW);
        }
        return false;
    }
}