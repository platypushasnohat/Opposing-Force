package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.loot.ReplaceTableLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class OFLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, OpposingForce.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceTableLootModifier>> REPLACE_TABLE = LOOT_MODIFIERS.register("replace_table", () -> ReplaceTableLootModifier.CODEC);
}
