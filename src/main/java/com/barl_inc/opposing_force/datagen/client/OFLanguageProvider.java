package com.barl_inc.opposing_force.datagen.client;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFCreativeTabs;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.datagen.client.SinewLanguageProvider;
import net.minecraft.data.PackOutput;

public class OFLanguageProvider extends SinewLanguageProvider {

    public OFLanguageProvider(PackOutput output) {
        super(output, OpposingForce.MOD_ID);
    }

    @Override
    protected void addTranslations() {
        this.addCreativeTab(OFCreativeTabs.OPPOSING_FORCE_TAB.get(), "Opposing Force");
        OFItems.ITEM_TRANSLATIONS.forEach(this::forItem);
        OFEntities.ENTITY_TRANSLATIONS.forEach(this::forEntity);

        this.addSound(OFSoundEvents.DICER_HURT, "Dicer hurts");
        this.addSound(OFSoundEvents.DICER_DEATH, "Dicer dies");
        this.addSound(OFSoundEvents.DICER_IDLE, "Dicer gurgles");
        this.addSound(OFSoundEvents.DICER_ATTACK, "Dicer slices");
        this.addSound(OFSoundEvents.DICER_LASER, "Dicer lasers");
        this.addSound(OFSoundEvents.DICER_LASER_START, "Dicer powers up laser");
        this.addSound(OFSoundEvents.DICER_LASER_END, "Dicer powers down laser");

        this.addSound(OFSoundEvents.LASER_BOLT_IMPACT, "Laser bolt disintegrates");
        this.addSound(OFSoundEvents.BLASTER_SHOOT, "Blaster shoots");

        this.addSound(OFSoundEvents.SLAYSER_DISC, "Music Disc");
        this.addMusicDisc(OFItems.MUSIC_DISC_SLAYSER.get(), "ChipsTheCat - Slayser");

        this.add(OFItems.TRI_BLASTER.get(), "Tri-Blaster");

        this.add("jukebox_song.opposing_force.slayser", "ChipsTheCat - Slayser");

        this.add("death.attack.laser_0", "%s was disintegrated");
        this.add("death.attack.laser_1", "%s was vaporized");
        this.add("death.attack.laser_0.entity", "%s was disintegrated by %s");
        this.add("death.attack.laser_1.entity", "%s was vaporized by %s");

        this.add("death.attack.laser_bolt_0", "%s was disintegrated");
        this.add("death.attack.laser_bolt_1", "%s was vaporized");
        this.add("death.attack.laser_bolt_0.entity", "%s was disintegrated by %s");
        this.add("death.attack.laser_bolt_1.entity", "%s was vaporized by %s");
    }
}
