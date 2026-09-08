package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class OFModelLayers {

    public static final ModelLayerLocation DICER = register("dicer");
    public static final ModelLayerLocation LASER_BOLT = register("laser_bolt");
    public static final ModelLayerLocation BLASTER = register("blaster");

    private static ModelLayerLocation register(String id) {
        return new ModelLayerLocation(OpposingForce.location(id), "main");
    }
}
