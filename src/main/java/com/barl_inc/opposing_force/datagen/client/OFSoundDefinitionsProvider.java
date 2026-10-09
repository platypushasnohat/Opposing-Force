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
        this.registerSound(OFSoundEvents.DICER_STEP,
                sound(OpposingForce.location("mob/dicer/step1")),
                sound(OpposingForce.location("mob/dicer/step2"))
        );
        this.registerSound(OFSoundEvents.DICER_ATTACK,
                sound(OpposingForce.location("mob/dicer/attack"))
        );
        this.registerSound(OFSoundEvents.DICER_DASH_WARN,
                sound(OpposingForce.location("mob/dicer/dash_warn"))
        );
        this.registerSound(OFSoundEvents.DICER_DASH,
                sound(OpposingForce.location("mob/dicer/dash"))
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

        this.registerSound(OFSoundEvents.GNAT_BUZZING,
                sound(OpposingForce.location("mob/gnat/buzz"))
        );

        this.registerSound(OFSoundEvents.FURBALL_DEATH,
                sound(OpposingForce.location("mob/furball/death"))
        );
        this.registerSound(OFSoundEvents.FURBALL_HURT,
                sound(OpposingForce.location("mob/furball/hurt1")),
                sound(OpposingForce.location("mob/furball/hurt2"))
        );
        this.registerSound(OFSoundEvents.FURBALL_IDLE,
                sound(OpposingForce.location("mob/furball/idle1")),
                sound(OpposingForce.location("mob/furball/idle2")),
                sound(OpposingForce.location("mob/furball/idle3")),
                sound(OpposingForce.location("mob/furball/idle4")),
                sound(OpposingForce.location("mob/furball/idle5"))
        );
        this.registerSound(OFSoundEvents.FURBALL_ATTACK,
                sound(OpposingForce.location("mob/furball/attack"))
        );

        this.registerSound(OFSoundEvents.TERROR_DEATH,
                sound(OpposingForce.location("mob/terror/death"))
        );
        this.registerSound(OFSoundEvents.TERROR_HURT,
                sound(OpposingForce.location("mob/terror/hurt"))
        );
        this.registerSound(OFSoundEvents.TERROR_IDLE,
                sound(OpposingForce.location("mob/terror/idle1")),
                sound(OpposingForce.location("mob/terror/idle2"))
        );
        this.registerSound(OFSoundEvents.TERROR_SAW_START,
                sound(OpposingForce.location("mob/terror/saw_start"))
        );
        this.registerSound(OFSoundEvents.TERROR_SAW_LOOP,
                sound(OpposingForce.location("mob/terror/saw_loop"))
        );
        this.registerSound(OFSoundEvents.TERROR_SAW_END,
                sound(OpposingForce.location("mob/terror/saw_end"))
        );

        this.registerSound(OFSoundEvents.LASER_BOLT_IMPACT,
                sound(OpposingForce.location("entity/laser_bolt/impact"))
        );

        this.registerSound(OFSoundEvents.BLASTER_SHOOT,
                sound(OpposingForce.location("item/blaster/shoot"))
        );

        this.registerSound(OFSoundEvents.LASER_BLADE_SWING,
                sound(OpposingForce.location("item/laser_blade/swing1")),
                sound(OpposingForce.location("item/laser_blade/swing2")),
                sound(OpposingForce.location("item/laser_blade/swing3")),
                sound(OpposingForce.location("item/laser_blade/swing4"))
        );
        this.registerSound(OFSoundEvents.LASER_BLADE_IMPACT,
                sound(OpposingForce.location("item/laser_blade/impact1")),
                sound(OpposingForce.location("item/laser_blade/impact2")),
                sound(OpposingForce.location("item/laser_blade/impact3"))
        );
        this.registerSound(OFSoundEvents.LASER_BLADE_SPIN,
                sound(OpposingForce.location("item/laser_blade/spin"))
        );
        this.registerSound(OFSoundEvents.LASER_BLADE_CATCH,
                sound(OpposingForce.location("item/laser_blade/catch1")),
                sound(OpposingForce.location("item/laser_blade/catch2")),
                sound(OpposingForce.location("item/laser_blade/catch3"))
        );

        this.registerSound(OFSoundEvents.ACID_CHARGE_EXPLODE,
                sound(OpposingForce.location("entity/acid_charge/explode"))
        );
        this.registerSound(OFSoundEvents.ACID_BURNING,
                sound(OpposingForce.location("entity/acid_charge/burn1")),
                sound(OpposingForce.location("entity/acid_charge/burn2"))
        );

        this.registerSound(OFSoundEvents.SLAYSER_DISC,
                sound(OpposingForce.location("record/slayser")).stream()
        );

        this.registerSound(OFSoundEvents.ZOMBIE_REINFORCEMENT,
                sound("mob/zombie/infect").pitch(0.9F)
        );
    }
}
