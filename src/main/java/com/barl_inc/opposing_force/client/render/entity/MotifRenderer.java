package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.MotifModel;
import com.barl_inc.opposing_force.entity.Motif;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MotifRenderer extends MobRenderer<Motif, MotifModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/motif/motif.png");

    public MotifRenderer(EntityRendererProvider.Context context) {
        super(context, new MotifModel(context.bakeLayer(OFModelLayers.MOTIF)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Motif motif) {
        return TEXTURE_LOCATION;
    }
}