package com.barl_inc.opposing_force.datagen.client;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.datagen.client.SinewSoundDefinitionsProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class OFSoundDefinitionsProvider extends SinewSoundDefinitionsProvider {

    public OFSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, OpposingForce.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        this.registerSound(OFSoundEvents.DICER_HURT,
                sound(OpposingForce.location("mob/dicer/hurt1")),
                sound(OpposingForce.location("mob/dicer/hurt2"))
        );
        this.registerSound(OFSoundEvents.DICER_DEATH,
                sound(OpposingForce.location("mob/dicer/death"))
        );
        this.registerSound(OFSoundEvents.DICER_IDLE,
                sound(OpposingForce.location("mob/dicer/idle1")),
                sound(OpposingForce.location("mob/dicer/idle2"))
        );
        this.registerSound(OFSoundEvents.DICER_ATTACK,
                sound(OpposingForce.location("mob/dicer/attack"))
        );
        this.registerSound(OFSoundEvents.DICER_LASER,
                sound(OpposingForce.location("mob/dicer/laser"))
        );
        this.registerSound(OFSoundEvents.DICER_LASER_START,
                sound(OpposingForce.location("mob/dicer/laser_start"))
        );
        this.registerSound(OFSoundEvents.DICER_LASER_END,
                sound(OpposingForce.location("mob/dicer/laser_end"))
        );

        this.registerSound(OFSoundEvents.LASER_BOLT_IMPACT,
                sound(OpposingForce.location("entity/laser_bolt/impact"))
        );

        this.registerSound(OFSoundEvents.BLASTER_SHOOT,
                sound(OpposingForce.location("item/blaster/shoot"))
        );

        this.registerSound(OFSoundEvents.SLAYSER_DISC,
                sound(OpposingForce.location("record/slayser")).stream()
        );
    }
}
