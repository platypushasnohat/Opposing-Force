package com.barl_inc.opposing_force.client.render.entity.pre_nether;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.pre_nether.GrasperModel;
import com.barl_inc.opposing_force.entity.pre_nether.Grasper;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GrasperRenderer extends MobRenderer<Grasper, GrasperModel> {

    private static final ResourceLocation TEXTURE = OpposingForce.location("textures/entity/grasper/grasper.png");

    public GrasperRenderer(EntityRendererProvider.Context context) {
        super(context, new GrasperModel(context.bakeLayer(OFModelLayers.GRASPER)), 0.30F);
    }

    @Override
    public ResourceLocation getTextureLocation(Grasper grasper) {
        return TEXTURE;
    }
}
