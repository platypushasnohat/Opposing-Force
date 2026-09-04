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
    }
}
