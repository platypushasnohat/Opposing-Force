package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.ScorcherModel;
import com.barl_inc.opposing_force.client.render.entity.layer.ScorcherGlowLayer;
import com.barl_inc.opposing_force.entity.Scorcher;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ScorcherRenderer extends MobRenderer<Scorcher, ScorcherModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/scorcher/scorcher.png");

    public ScorcherRenderer(EntityRendererProvider.Context context) {
        super(context, new ScorcherModel(context.bakeLayer(OFModelLayers.SCORCHER)), 0.5F);
        this.addLayer(new ScorcherGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Scorcher scorcher) {
        return TEXTURE_LOCATION;
    }
}