package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.KirinModel;
import com.barl_inc.opposing_force.client.render.entity.layer.KirinGlowLayer;
import com.barl_inc.opposing_force.entity.Kirin;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class KirinRenderer extends MobRenderer<Kirin, KirinModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/kirin/kirin.png");

    public KirinRenderer(EntityRendererProvider.Context context) {
        super(context, new KirinModel(context.bakeLayer(OFModelLayers.KIRIN)), 0.0F);
        this.addLayer(new KirinGlowLayer(this));
    }

    @Override
    protected int getBlockLightLevel(Kirin kirin, BlockPos pos) {
        return Math.max(super.getBlockLightLevel(kirin, pos), Math.round(10.0F * kirin.getDarkness()));
    }

    @Override
    public ResourceLocation getTextureLocation(Kirin kirin) {
        return TEXTURE_LOCATION;
    }
}