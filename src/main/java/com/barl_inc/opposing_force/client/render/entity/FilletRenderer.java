package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.FilletModel;
import com.barl_inc.opposing_force.client.render.entity.layer.FilletEyesLayer;
import com.barl_inc.opposing_force.entity.Fillet;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FilletRenderer extends MobRenderer<Fillet, FilletModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/fillet/fillet.png");

    public FilletRenderer(EntityRendererProvider.Context context) {
        super(context, new FilletModel(context.bakeLayer(OFModelLayers.FILLET)), 0.25F);
        this.addLayer(new FilletEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Fillet fillet) {
        return TEXTURE_LOCATION;
    }
}