package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class OFModelLayers {

    public static final ModelLayerLocation DICER = register("dicer");

    private static ModelLayerLocation register(String id) {
        return new ModelLayerLocation(OpposingForce.location(id), "main");
    }
}
