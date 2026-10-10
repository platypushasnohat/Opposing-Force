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
        this.addSound(OFSoundEvents.DICER_STEP, "Dicer steps");
        this.addSound(OFSoundEvents.DICER_ATTACK, "Dicer slices");
        this.addSound(OFSoundEvents.DICER_DASH_WARN, "Dicer prepares dash");
        this.addSound(OFSoundEvents.DICER_DASH, "Dicer dashes");
        this.addSound(OFSoundEvents.DICER_LASER, "Dicer lasers");
        this.addSound(OFSoundEvents.DICER_LASER_START, "Dicer powers up laser");
        this.addSound(OFSoundEvents.DICER_LASER_END, "Dicer powers down laser");

        this.addSound(OFSoundEvents.GNAT_BUZZING, "Gnat buzzes");

        this.addSound(OFSoundEvents.FURBALL_DEATH, "Furball dies");
        this.addSound(OFSoundEvents.FURBALL_HURT, "Furball hurts");
        this.addSound(OFSoundEvents.FURBALL_IDLE, "Furball snarls");
        this.addSound(OFSoundEvents.FURBALL_ATTACK, "Furball attacks");

        this.addSound(OFSoundEvents.TERROR_DEATH, "Terror dies");
        this.addSound(OFSoundEvents.TERROR_HURT, "Terror hurts");
        this.addSound(OFSoundEvents.TERROR_IDLE, "Terror grumbles");
        this.addSound(OFSoundEvents.TERROR_SAW_START, "Terror starts sawing");
        this.addSound(OFSoundEvents.TERROR_SAW_LOOP, "Terror saws");
        this.addSound(OFSoundEvents.TERROR_SAW_END, "Terror stops sawing");

        this.addSound(OFSoundEvents.MUSHY_HURT, "Mushy hurts");
        this.addSound(OFSoundEvents.MUSHY_DEATH, "Mushy dies");
        this.addSound(OFSoundEvents.MUSHY_IDLE, "Mushy squelches");

        this.addSound(OFSoundEvents.LASER_BOLT_IMPACT, "Laser bolt disintegrates");
        this.addSound(OFSoundEvents.BLASTER_SHOOT, "Blaster shoots");

        this.addSound(OFSoundEvents.LASER_BLADE_SWING, "Laser blade swings");
        this.addSound(OFSoundEvents.LASER_BLADE_SPIN, "Laser blade spins");
        this.addSound(OFSoundEvents.LASER_BLADE_IMPACT, "Laser blade slices");
        this.addSound(OFSoundEvents.LASER_BLADE_CATCH, "Laser blade caught");

        this.addSound(OFSoundEvents.ACID_CHARGE_EXPLODE, "Acid charge explodes");
        this.addSound(OFSoundEvents.ACID_BURNING, "Acid burns");

        this.addSound(OFSoundEvents.SLAYSER_DISC, "Music Disc");
        this.addMusicDisc(OFItems.MUSIC_DISC_SLAYSER.get(), "ChipsTheCat - Slayser");

        this.addSound(OFSoundEvents.ZOMBIE_REINFORCEMENT, "Zombie summons reinforcement");

        this.add(OFItems.TRI_BLASTER.get(), "Tri-Blaster");

        this.add(OFItems.DISABLED_LASER_BLADE.get(), "Laser Blade");

        this.add("item.opposing_force.powered_item.power", "Power: %s / %s");

        this.add("jukebox_song.opposing_force.slayser", "ChipsTheCat - Slayser");

        this.add("death.attack.laser_0", "%s was disintegrated");
        this.add("death.attack.laser_1", "%s was vaporized");
        this.add("death.attack.laser_0.entity", "%s was disintegrated by %s");
        this.add("death.attack.laser_1.entity", "%s was vaporized by %s");

        this.add("death.attack.laser_bolt_0", "%s was disintegrated");
        this.add("death.attack.laser_bolt_1", "%s was vaporized");
        this.add("death.attack.laser_bolt_0.entity", "%s was disintegrated by %s");
        this.add("death.attack.laser_bolt_1.entity", "%s was vaporized by %s");

        this.add("death.attack.laser_blade_0", "%s was sliced in half");
        this.add("death.attack.laser_blade_1", "%s was bisected");
        this.add("death.attack.laser_blade_0.entity", "%s was sliced in half by %s");
        this.add("death.attack.laser_blade_1.entity", "%s was bisected by %s");

        this.add("death.attack.acid_0", "%s was melted by acid");
        this.add("death.attack.acid_1", "%s was liquefied");
        this.add("death.attack.acid_0.entity", "%s was dunked in acid by %s");
        this.add("death.attack.acid_1.entity", "%s was liquefied by %s");

        this.add("death.attack.scorch_0", "%s was scorched");
        this.add("death.attack.scorch_1", "%s was burnt to ash");
        this.add("death.attack.scorch_0.entity", "%s was scorched by %s");
        this.add("death.attack.scorch_1.entity", "%s was burnt to ash by %s");

        this.add("death.attack.saw_0", "%s was sawed in half");
        this.add("death.attack.saw_1", "%s was sawed into pieces");
        this.add("death.attack.saw_0.entity", "%s was sawed in half by %s");
        this.add("death.attack.saw_1.entity", "%s was sawed into pieces by %s");
    }
}
