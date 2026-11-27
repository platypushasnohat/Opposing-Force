package com.barl_inc.opposing_force.client.render.entity.pre_nether;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.pre_nether.BewilderModel;
import com.barl_inc.opposing_force.entity.pre_nether.Bewilder;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BewilderRenderer extends MobRenderer<Bewilder, BewilderModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/bewilder/bewilder.png");

    public BewilderRenderer(EntityRendererProvider.Context context) {
        super(context, new BewilderModel(context.bakeLayer(OFModelLayers.BEWILDER)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Bewilder bewilder) {
        return TEXTURE_LOCATION;
    }
}