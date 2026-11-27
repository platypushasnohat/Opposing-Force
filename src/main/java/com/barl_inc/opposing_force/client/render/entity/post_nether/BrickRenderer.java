package com.barl_inc.opposing_force.client.render.entity.post_nether;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.post_nether.BrickModel;
import com.barl_inc.opposing_force.client.render.entity.layer.post_nether.BrickEyesLayer;
import com.barl_inc.opposing_force.entity.post_nether.Brick;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BrickRenderer extends MobRenderer<Brick, BrickModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/brick/brick.png");

    public BrickRenderer(EntityRendererProvider.Context context) {
        super(context, new BrickModel(context.bakeLayer(OFModelLayers.BRICK)), 0.5F);
        this.addLayer(new BrickEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Brick brick) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected float getFlipDegrees(Brick brick) {
        return brick.isMelting() ? 0.0F : super.getFlipDegrees(brick);
    }
}
