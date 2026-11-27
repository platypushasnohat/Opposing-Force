package com.barl_inc.opposing_force.client.render.entity.pre_nether;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.pre_nether.RoamerModel;
import com.barl_inc.opposing_force.client.render.entity.layer.pre_nether.RoamerGlowLayer;
import com.barl_inc.opposing_force.entity.pre_nether.Roamer;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RoamerRenderer extends MobRenderer<Roamer, RoamerModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/roamer/roamer.png");
    private static final ResourceLocation FRESNO_LOCATION = OpposingForce.location("textures/entity/roamer/roamer_fresno.png");

    public RoamerRenderer(EntityRendererProvider.Context context) {
        super(context, new RoamerModel(context.bakeLayer(OFModelLayers.ROAMER)), 0.0F);
        this.addLayer(new RoamerGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Roamer roamer) {
        return roamer.isFresno() ? FRESNO_LOCATION : TEXTURE_LOCATION;
    }
}
