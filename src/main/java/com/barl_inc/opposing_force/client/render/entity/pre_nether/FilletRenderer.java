package com.barl_inc.opposing_force.client.render.entity.pre_nether;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.pre_nether.FilletModel;
import com.barl_inc.opposing_force.client.render.entity.layer.pre_nether.FilletEyesLayer;
import com.barl_inc.opposing_force.entity.pre_nether.Fillet;
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