package com.barl_inc.opposing_force.datagen.client;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.debug.OFDebugItems;
import com.barl_inc.opposing_force.registry.*;
import com.platypushasnohat.sinew.datagen.client.SinewLanguageProvider;
import net.minecraft.data.PackOutput;

public class OFLanguageProvider extends SinewLanguageProvider {

    public OFLanguageProvider(PackOutput output) {
        super(output, OpposingForce.MOD_ID);
    }

    @Override
    protected void addTranslations() {
        this.addCreativeTab(OFCreativeTabs.OPPOSING_FORCE_TAB.get(), "Opposing Force");
        OFBlocks.BLOCK_TRANSLATIONS.forEach(this::forBlock);
        OFItems.ITEM_TRANSLATIONS.forEach(this::forItem);
        OFEntities.ENTITY_TRANSLATIONS.forEach(this::forEntity);

        this.add(OFEntities.GRELL_CLONE.get(), "Grell");

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

        this.addSound(OFSoundEvents.BULLET_IMPACT, "Bullet impacts");

        this.addSound(OFSoundEvents.GUN_SHOOT, "Gun fires");
        this.addSound(OFSoundEvents.GUN_SHOOT_SHORT, "Gun fires fast");
        this.addSound(OFSoundEvents.GUN_WIND_DOWN, "Gun winds down");
        this.addSound(OFSoundEvents.GUN_WIND_UP, "Gun winds up");

        this.addSound(OFSoundEvents.BUZZSAW_START, "Buzzsaw starts sawing");
        this.addSound(OFSoundEvents.BUZZSAW_LOOP, "Buzzsaw saws");
        this.addSound(OFSoundEvents.BUZZSAW_END, "Buzzsaw stops sawing");

        this.add(OFItems.TRI_BLASTER.get(), "Tri-Blaster");

        this.add(OFItems.DISABLED_LASER_BLADE.get(), "Laser Blade");
        this.add(OFItems.DISABLED_CURSED_RINGLINE.get(), "Cursed Ringline");

        this.add(OFItems.FILLET_BUCKET.get(), "Bucket of Fillet");

        this.add("item.opposing_force.tooltip.damage", "%s Damage");
        this.add("item.opposing_force.tooltip.multi_damage", "%sx%s Damage");
        this.add("item.opposing_force.tooltip.uses_mud", "Uses Mud as ammo");
        this.add("item.opposing_force.tooltip.requires_power_cell", "Requires a Power Cell to operate");
        this.add("item.opposing_force.tooltip.uses_bullets", "Uses Bullets as ammo");
        this.add("item.opposing_force.tooltip.uses_basic_mana", "Uses Basic Mana");
        this.add("item.opposing_force.tooltip.basic_mana", "Basic Mana");

        this.add("item.opposing_force.power_cell.description", "Refill with Redstone Dust");
        this.add("item.opposing_force.laser_blade.description", "Right-click to throw");
        this.add("item.opposing_force.spectral_shield.description", "Blocks up to six hits from any direction");
        this.add("item.opposing_force.spectral_cleaver.description", "Summons a homing soul on hit");
        this.add("item.opposing_force.chitin_lance.description", "Hold right-click to charge a dash");
        this.add("item.opposing_force.buzzsaw.description", "Hold right-click to activate saw");
        this.add("item.opposing_force.tooltip.when_fired", "When fired:");
        this.add("item.opposing_force.tooltip.when_cast", "When cast:");
        this.add("item.opposing_force.tooltip.when_thrown", "When thrown:");
        this.add("item.opposing_force.tooltip.when_used_as_ammo", "When used as ammo:");

        this.add("item.opposing_force.bullet.damage", "x%s Damage");
        this.add("item.opposing_force.bullet.spread", "x%s Spread");
        this.add("item.opposing_force.bullet.velocity", "x%s Velocity");

        this.add("item.opposing_force.powered_item.power", "Power: %s / %s");

        this.add("item.opposing_force.relic_feather.description", "+Slow Falling");
        this.add("item.opposing_force.momentum_gauntlet.description", "+Mining Momentum");
        this.add("item.opposing_force.slimy_shield.description", "+Slimy Shielding");
        this.add("item.opposing_force.stalker_gloves.description", "+30% Initial Damage");

        this.add("item.opposing_force.crystallized_heart.description", "+1 Charm Slot");
        this.add("item.opposing_force.spectral_goggles.description", "Pierce the Veil...");

        this.add("enchantment.opposing_force.magic_protection", "Magic Protection");
        this.add("enchantment.opposing_force.magic_protection.desc", "Reduces magic damageMultiplier taken and shortens the duration of negative effects applied to the wearer.");

        this.add("enchantment.opposing_force.frost_aspect", "Frost Aspect");
        this.add("enchantment.opposing_force.frost_aspect.desc", "Inflicts freezing on hit");

        this.add("enchantment.opposing_force.mana_thrifter", "Mana Thrifter");
        this.add("enchantment.opposing_force.mana_thrifter.desc", "Magic weapons do not consume mana, but deal 33% less damage.");

        this.add("enchantment.opposing_force.magic_attunement", "Magic Attunement");
        this.add("enchantment.opposing_force.magic_attunement.desc", "Increases the potential of a magic weapon.");

        this.add("enchantment.opposing_force.freeze", "Freeze");
        this.add("enchantment.opposing_force.freeze.desc", "Inflicts freezing on hit");

        this.add("pack.opposing_force.protection_tweaks", "Opposing Force Protection Tweaks");

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

        this.add("death.attack.bullet", "%s was shot");
        this.add("death.attack.bullet.player", "%s was shot by %s");
        this.add("death.attack.bullet.item", "%s was shot by %s using %s");

        this.add("death.attack.cursed_ring_0", "%s was killed with cursed magic");
        this.add("death.attack.cursed_ring_1", "%s was hexed");
        this.add("death.attack.cursed_ring_0.entity", "%s was killed with cursed magic by %s");
        this.add("death.attack.cursed_ring_1.entity", "%s was hexed by %s");

        this.add("death.attack.soul_0", "%s had their soul reaped");
        this.add("death.attack.soul_1", "%s had their soul torn apart");
        this.add("death.attack.soul_0.entity", "%s has their soul torn apart by %s");
        this.add("death.attack.soul_1.entity", "%s was reaped %s");

        this.add("death.attack.grell_bite", "%1$s was torn apart by %2$s");

        this.add(OFDebugItems.RINGMASTER.get(), "Ringmaster");
        this.add(OFDebugItems.SOULCLEAVE.get(), "Soulcleave");
        this.add("item.opposing_force.tooltip.debug_item", "DEBUG ITEM");
    }
}
