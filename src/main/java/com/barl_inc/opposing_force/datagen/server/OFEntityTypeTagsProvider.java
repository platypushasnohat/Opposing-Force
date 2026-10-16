package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.tags.OFEntityTypeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class OFEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public OFEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, OpposingForce.MOD_ID, existingFileHelper);
	}

	@Override
	public void addTags(Provider provider) {

		this.tag(EntityTypeTags.ARTHROPOD).add(
				OFEntities.BEWILDER.get(),
				OFEntities.GUSHER.get(),
				OFEntities.GNAT.get(),
				OFEntities.SCORCHER.get(),
				OFEntities.TARANTULA.get()
		);

        this.tag(OFEntityTypeTags.SLIMY).add(
                EntityType.SLIME,
                EntityType.MAGMA_CUBE
        );

        this.tag(OFEntityTypeTags.PROJECTILE_BYPASSES_BRICK).add(
                OFEntities.ACID_CHARGE.get()
        );
	}
}