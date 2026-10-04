package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.TarantulaModel;
import com.barl_inc.opposing_force.client.render.entity.layer.TarantulaEyesLayer;
import com.barl_inc.opposing_force.entity.Tarantula;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TarantulaRenderer extends MobRenderer<Tarantula, TarantulaModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/tarantula/tarantula.png");

    public TarantulaRenderer(EntityRendererProvider.Context context) {
        super(context, new TarantulaModel(context.bakeLayer(OFModelLayers.TARANTULA)), 1.5F);
        this.addLayer(new TarantulaEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Tarantula tarantula) {
        return TEXTURE_LOCATION;
    }
}