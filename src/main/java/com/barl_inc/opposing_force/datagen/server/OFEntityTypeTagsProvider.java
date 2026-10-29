package com.barl_inc.opposing_force.datagen.server;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.datapack.tags.OFEntityTypeTags;
import com.platypushasnohat.sinew.tags.SinewEntityTags;
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

		this.tag(SinewEntityTags.POST_NETHER_SPAWNS).add(
				OFEntities.DICER.get(),
				OFEntities.GUSHER.get(),
				OFEntities.SCORCHER.get(),
				OFEntities.TARANTULA.get(),
				OFEntities.MUSHY.get(),
				OFEntities.TERROR.get(),
                OFEntities.GHOST.get(),
                OFEntities.HAUNT.get(),
                OFEntities.PIPSQUEAK.get()
		);

        this.tag(EntityTypeTags.UNDEAD).add(
                OFEntities.GHOST.get(),
                OFEntities.HAUNT.get(),
                OFEntities.GRASPER.get()
        );

        this.tag(SinewEntityTags.POST_END_SPAWNS).add(
                OFEntities.GRELL.get()
        );

        this.tag(OFEntityTypeTags.PIPSQUEAK_CANNOT_SWALLOW).add(
                OFEntities.SOUNDWAVE.get(),
                OFEntities.SOUL.get(),
                OFEntities.DICER_LASER.get(),
                OFEntities.LASER_BLADE.get()
        );

        this.tag(OFEntityTypeTags.BUGS).add(
                EntityType.SPIDER,
                EntityType.CAVE_SPIDER,
                OFEntities.GUSHER.get(),
                OFEntities.SCORCHER.get(),
                OFEntities.TARANTULA.get(),
                OFEntities.BEWILDER.get(),
                OFEntities.GNAT.get()
        );
	}
}