package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.client.model.entity.RoamerModel;
import com.barl_inc.opposing_force.client.render.entity.layer.RoamerGlowLayer;
import com.barl_inc.opposing_force.entity.Roamer;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RoamerRenderer extends MobRenderer<Roamer, RoamerModel> {

    public RoamerRenderer(EntityRendererProvider.Context context) {
        super(context, new RoamerModel(context.bakeLayer(OFModelLayers.ROAMER)), 0.0F);
        this.addLayer(new RoamerGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Roamer roamer) {
        return roamer.getVariantTexture();
    }
}
