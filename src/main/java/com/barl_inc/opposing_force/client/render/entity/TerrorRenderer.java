package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.TerrorModel;
import com.barl_inc.opposing_force.client.render.entity.layer.TerrorGlowLayer;
import com.barl_inc.opposing_force.entity.Terror;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TerrorRenderer extends MobRenderer<Terror, TerrorModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/terror/terror.png");

    public TerrorRenderer(EntityRendererProvider.Context context) {
        super(context, new TerrorModel(context.bakeLayer(OFModelLayers.TERROR)), 1.0F);
        this.addLayer(new TerrorGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Terror terror) {
        return TEXTURE_LOCATION;
    }
}