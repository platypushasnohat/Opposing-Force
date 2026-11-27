package com.barl_inc.opposing_force.client.render.entity.post_nether;

import com.barl_inc.opposing_force.client.model.entity.post_nether.GhostModel;
import com.barl_inc.opposing_force.client.render.entity.PhasingMonsterRenderer;
import com.barl_inc.opposing_force.client.render.entity.layer.post_nether.GhostItemLayer;
import com.barl_inc.opposing_force.entity.post_nether.Ghost;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GhostRenderer extends PhasingMonsterRenderer<Ghost, GhostModel> {

    public GhostRenderer(EntityRendererProvider.Context context) {
        super(context, new GhostModel(context.bakeLayer(OFModelLayers.GHOST)), "ghost", 1.4F);
        this.addLayer(new GhostItemLayer(this, context.getItemInHandRenderer()));
    }
}
