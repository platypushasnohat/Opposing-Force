package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.item.TriBlasterItem;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TriBlasterModel extends BlasterModel<TriBlasterItem> {

    public TriBlasterModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -8.0F, 4.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(28, 36).addBox(-1.5F, 1.0F, 0.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 20).addBox(-1.5F, -1.5F, -13.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(36, 26).addBox(-2.5F, -2.5F, -16.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 44).addBox(-2.5F, -2.5F, -11.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(42, 42).addBox(-2.5F, -2.5F, -12.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 42).addBox(-1.5F, 1.0F, 8.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 47).addBox(-2.5F, -2.5F, -10.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(37, 11).addBox(-1.5F, -2.0F, 3.0F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(50, 37).addBox(-1.0F, 2.0F, 5.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 32).addBox(-1.5F, -2.0F, 8.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-1.0F, -5.0F, -1.0F, 0.0F, 3.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(-1.5F, -2.0F, 11.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(51, 8).addBox(-1.5F, -1.0F, 13.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(51, 8).addBox(-1.5F, -1.0F, 15.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(49, 9).addBox(-1.5F, 2.0F, 13.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 9).addBox(-1.5F, -1.0F, 13.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 18.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}