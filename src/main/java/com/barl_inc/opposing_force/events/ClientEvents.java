package com.barl_inc.opposing_force.events;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.DicerModel;
import com.barl_inc.opposing_force.client.model.entity.LaserBoltModel;
import com.barl_inc.opposing_force.client.model.item.BlasterModel;
import com.barl_inc.opposing_force.client.model.item.ScatterBlasterModel;
import com.barl_inc.opposing_force.client.model.item.TriBlasterModel;
import com.barl_inc.opposing_force.client.particle.LaserDustParticle;
import com.barl_inc.opposing_force.client.particle.LaserImpactParticle;
import com.barl_inc.opposing_force.client.render.entity.DicerLaserRenderer;
import com.barl_inc.opposing_force.client.render.entity.DicerRenderer;
import com.barl_inc.opposing_force.client.render.entity.LaserBladeRenderer;
import com.barl_inc.opposing_force.client.render.entity.LaserBoltRenderer;
import com.barl_inc.opposing_force.client.render.item.OFItemExtensions;
import com.barl_inc.opposing_force.item.BlasterItem;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.events.custom.PoseHandEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.util.TriState;

@EventBusSubscriber(modid = OpposingForce.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OFEntities.DICER.get(), DicerRenderer::new);
        event.registerEntityRenderer(OFEntities.DICER_LASER.get(), DicerLaserRenderer::new);
        event.registerEntityRenderer(OFEntities.LASER_BOLT.get(), LaserBoltRenderer::new);
        event.registerEntityRenderer(OFEntities.LASER_BLADE.get(), LaserBladeRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OFModelLayers.DICER, DicerModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.LASER_BOLT, LaserBoltModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.BLASTER, BlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.TRI_BLASTER, TriBlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.SCATTER_BLASTER, ScatterBlasterModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(OFParticleTypes.LASER_DUST.get(), LaserDustParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.LASER_IMPACT.get(), LaserImpactParticle.Factory::new);
    }

    @SubscribeEvent
    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.TRI_BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.SCATTER_BLASTER.get());
    }

    @SubscribeEvent
    public static void onPoseHand(PoseHandEvent event) {
        LivingEntity player = (LivingEntity) event.getEntity();
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BlasterItem) {
            if (player.getMainArm() == HumanoidArm.RIGHT) {
                event.getHumanoidModel().rightArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().rightArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().rightArm.zRot = 0;
            } else {
                event.getHumanoidModel().leftArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().leftArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().leftArm.zRot = 0;
            }
            event.setResult(TriState.TRUE);
        }
        if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof BlasterItem) {
            if (player.getMainArm() == HumanoidArm.RIGHT) {
                event.getHumanoidModel().leftArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().leftArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().leftArm.zRot = 0;
            } else {
                event.getHumanoidModel().rightArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().rightArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().rightArm.zRot = 0;
            }
            event.setResult(TriState.TRUE);
        }
    }
}