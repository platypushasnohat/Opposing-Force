package com.barl_inc.opposing_force.events;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.DicerModel;
import com.barl_inc.opposing_force.client.particle.LaserDustParticle;
import com.barl_inc.opposing_force.client.render.entity.DicerLaserRenderer;
import com.barl_inc.opposing_force.client.render.entity.DicerRenderer;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = OpposingForce.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OFEntities.DICER.get(), DicerRenderer::new);
        event.registerEntityRenderer(OFEntities.DICER_LASER.get(), DicerLaserRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OFModelLayers.DICER, DicerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(OFParticleTypes.LASER_DUST.get(), LaserDustParticle.Factory::new);
    }
}