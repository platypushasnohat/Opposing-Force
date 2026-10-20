package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.GhostModel;
import com.barl_inc.opposing_force.client.render.entity.layer.GhostGlowLayer;
import com.barl_inc.opposing_force.client.render.entity.layer.GhostItemLayer;
import com.barl_inc.opposing_force.entity.Ghost;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class GhostRenderer extends MobRenderer<Ghost, GhostModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/ghost/ghost.png");

    public GhostRenderer(EntityRendererProvider.Context context) {
        super(context, new GhostModel(context.bakeLayer(OFModelLayers.GHOST)), 0.5F);
        this.addLayer(new GhostGlowLayer(this));
        this.addLayer(new GhostItemLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Ghost ghost) {
        return TEXTURE_LOCATION;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(Ghost ghost, boolean bodyVisible, boolean translucent, boolean glowing) {
        return SpectralSight.isPerceivedByClient(ghost) ? super.getRenderType(ghost, bodyVisible, translucent, glowing) : null;
    }

    @Override
    protected float getShadowRadius(Ghost ghost) {
        return SpectralSight.isPerceivedByClient(ghost) ? super.getShadowRadius(ghost) : 0.0F;
    }
}
