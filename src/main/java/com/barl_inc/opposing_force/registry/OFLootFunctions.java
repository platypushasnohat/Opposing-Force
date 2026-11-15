package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.loot.SetTornScrollOriginFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OFLootFunctions {

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPE = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, OpposingForce.MOD_ID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SetTornScrollOriginFunction>> SET_TORN_SCROLL_ORIGIN = LOOT_FUNCTION_TYPE.register("set_torn_scroll_origin", () -> new LootItemFunctionType<>(SetTornScrollOriginFunction.CODEC));
}