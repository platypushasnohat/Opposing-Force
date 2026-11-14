package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OFAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, OpposingForce.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> KIRIN_JUMPS = ATTRIBUTES.register("kirin_jumps", () -> new RangedAttribute("attribute.name.opposing_force.kirin_jumps", 0.0D, 0.0D, 64.0D).setSyncable(true));
}
