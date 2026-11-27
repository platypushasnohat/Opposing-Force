package com.barl_inc.opposing_force.client.render.entity.pre_nether;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.pre_nether.PipsqueakModel;
import com.barl_inc.opposing_force.entity.pre_nether.Pipsqueak;
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
