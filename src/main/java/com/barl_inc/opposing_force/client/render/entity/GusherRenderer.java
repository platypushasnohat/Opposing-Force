package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.GusherModel;
import com.barl_inc.opposing_force.entity.Gusher;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GusherRenderer extends MobRenderer<Gusher, GusherModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/gusher/gusher.png");

    public GusherRenderer(EntityRendererProvider.Context context) {
        super(context, new GusherModel(context.bakeLayer(OFModelLayers.GUSHER)), 0.75F);
    }

    @Override
    public ResourceLocation getTextureLocation(Gusher gusher) {
        return TEXTURE_LOCATION;
    }
}