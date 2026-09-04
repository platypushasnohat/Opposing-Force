package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.DicerModel;
import com.barl_inc.opposing_force.entity.Dicer;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DicerRenderer extends MobRenderer<Dicer, DicerModel> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/entity/dicer/dicer.png");

    public DicerRenderer(EntityRendererProvider.Context context) {
        super(context, new DicerModel(context.bakeLayer(OFModelLayers.DICER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Dicer dicer) {
        return TEXTURE;
    }
}