package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Unique
    private static final ResourceLocation ELECTRIFIED_FULL = OpposingForce.location("hud/heart/electrified_full");
    @Unique
    private static final ResourceLocation ELECTRIFIED_HALF = OpposingForce.location("hud/heart/electrified_half");
    @Unique
    private static final ResourceLocation ELECTRIFIED_HARDCORE_FULL = OpposingForce.location("hud/heart/electrified_hardcore_full");
    @Unique
    private static final ResourceLocation ELECTRIFIED_HARDCORE_HALF = OpposingForce.location("hud/heart/electrified_hardcore_half");

    @WrapOperation(method = "renderHeart", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui$HeartType;getSprite(ZZZ)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation opposingForce$electrifiedHeart(Gui.HeartType heartType, boolean hardcore, boolean halfHeart, boolean blinking, Operation<ResourceLocation> original) {
        if (!blinking && heartType != Gui.HeartType.CONTAINER && heartType != Gui.HeartType.ABSORBING && heartType != Gui.HeartType.FROZEN && Minecraft.getInstance().getCameraEntity() instanceof Player player && player.hasEffect(OFMobEffects.ELECTRIFIED)) {
            if (hardcore) {
                return halfHeart ? ELECTRIFIED_HARDCORE_HALF : ELECTRIFIED_HARDCORE_FULL;
            }
            return halfHeart ? ELECTRIFIED_HALF : ELECTRIFIED_FULL;
        }
        return original.call(heartType, hardcore, halfHeart, blinking);
    }
}
