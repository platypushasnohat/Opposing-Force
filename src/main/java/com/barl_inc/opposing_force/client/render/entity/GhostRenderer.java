package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.client.model.entity.GhostModel;
import com.barl_inc.opposing_force.client.render.entity.layer.GhostItemLayer;
import com.barl_inc.opposing_force.entity.Ghost;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GhostRenderer extends PhasingMonsterRenderer<Ghost, GhostModel> {

    public GhostRenderer(EntityRendererProvider.Context context) {
        super(context, new GhostModel(context.bakeLayer(OFModelLayers.GHOST)), "ghost", 1.4F);
        this.addLayer(new GhostItemLayer(this, context.getItemInHandRenderer()));
    }
}
