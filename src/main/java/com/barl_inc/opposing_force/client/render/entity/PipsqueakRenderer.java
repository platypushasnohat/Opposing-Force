package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.PipsqueakModel;
import com.barl_inc.opposing_force.entity.Pipsqueak;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PipsqueakRenderer extends MobRenderer<Pipsqueak, PipsqueakModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/pipsqueak/pipsqueak.png");

    public PipsqueakRenderer(EntityRendererProvider.Context context) {
        super(context, new PipsqueakModel(context.bakeLayer(OFModelLayers.PIPSQUEAK)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Pipsqueak pipsqueak) {
        return TEXTURE_LOCATION;
    }
}
