package com.barl_inc.opposing_force.loot;

import com.barl_inc.opposing_force.item.scroll.TornScrollOrigin;
import com.barl_inc.opposing_force.registry.OFDataComponents;
import com.barl_inc.opposing_force.registry.OFLootFunctions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SetTornScrollOriginFunction extends LootItemConditionalFunction {

    public static final MapCodec<SetTornScrollOriginFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).apply(instance, SetTornScrollOriginFunction::new));

    public SetTornScrollOriginFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin != null) {
            ServerLevel level = context.getLevel();
            BlockPos pos = BlockPos.containing(origin);
            stack.set(OFDataComponents.TORN_SCROLL_ORIGIN.get(), new TornScrollOrigin(
                    level.structureManager().getAllStructuresAt(pos).keySet().stream()
                            .filter(structure -> level.structureManager().getStructureWithPieceAt(pos, structure).isValid())
                            .findFirst()
                            .flatMap(structure -> level.registryAccess().registryOrThrow(Registries.STRUCTURE).getResourceKey(structure)),
                    level.getBiome(pos).unwrapKey()
            ));
        }
        return stack;
    }

    @Override
    public LootItemFunctionType<SetTornScrollOriginFunction> getType() {
        return OFLootFunctions.SET_TORN_SCROLL_ORIGIN.get();
    }
}
