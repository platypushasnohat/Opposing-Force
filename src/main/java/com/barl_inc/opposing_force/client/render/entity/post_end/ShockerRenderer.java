package com.barl_inc.opposing_force.client.render.entity.post_end;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.post_end.ShockerModel;
import com.barl_inc.opposing_force.client.render.entity.layer.post_end.ShockerGlowLayer;
import com.barl_inc.opposing_force.entity.post_end.Shocker;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ShockerRenderer extends MobRenderer<Shocker, ShockerModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/shocker/shocker.png");

    public ShockerRenderer(EntityRendererProvider.Context context) {
        super(context, new ShockerModel(context.bakeLayer(OFModelLayers.SHOCKER)), 0.4F);
        this.addLayer(new ShockerGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Shocker shocker) {
        return TEXTURE_LOCATION;
    }
}