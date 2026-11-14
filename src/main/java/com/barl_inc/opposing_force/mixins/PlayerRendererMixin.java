package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.armor.ReconKnightArmorModel;
import com.barl_inc.opposing_force.client.model.armor.StormLordArmorModel;
import com.barl_inc.opposing_force.registry.OFItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Unique
    private static final ResourceLocation RECON_KNIGHT_TEXTURE = OpposingForce.location("textures/models/armor/recon_knight_layer_1.png");
    @Unique
    private static final ResourceLocation STORM_LORD_TEXTURE = OpposingForce.location("textures/models/armor/storm_lord_layer_1.png");

    @Inject(method = "renderHand", at = @At("TAIL"))
    private void opposingForce$renderArmorArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        boolean recon = chestplate.is(OFItems.RECON_KNIGHT_CHESTPLATE);
        if (recon || chestplate.is(OFItems.STORM_LORD_CHESTPLATE)) {
            ReconKnightArmorModel model = recon ? ReconKnightArmorModel.INSTANCE : StormLordArmorModel.INSTANCE;
            boolean rightArm = arm == ((PlayerRenderer) (Object) this).getModel().rightArm;
            ModelPart armorArm = rightArm ? model.rightArm : model.leftArm;
            armorArm.copyFrom(arm);
            armorArm.visible = true;
            VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(recon ? RECON_KNIGHT_TEXTURE : STORM_LORD_TEXTURE), chestplate.hasFoil());
            armorArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
 