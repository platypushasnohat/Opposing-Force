package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.GnatModel;
import com.barl_inc.opposing_force.entity.Gnat;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GnatRenderer extends MobRenderer<Gnat, GnatModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/gnat/gnat.png");

    public GnatRenderer(EntityRendererProvider.Context context) {
        super(context, new GnatModel(context.bakeLayer(OFModelLayers.GNAT)), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(Gnat gnat) {
        return TEXTURE_LOCATION;
    }
}