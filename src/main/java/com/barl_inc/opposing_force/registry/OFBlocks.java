package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.block.MysticPotBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OFBlocks {
    public static final DeferredRegister.Blocks BLOCK = DeferredRegister.createBlocks(OpposingForce.MOD_ID);

    public static List<DeferredBlock<? extends Block>> BLOCK_TRANSLATIONS = new ArrayList<>();

    public static final DeferredBlock<Block> MYSTIC_POT = registerBlock("mystic_pot", () -> new MysticPotBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_RED).strength(0.1F).sound(SoundType.DECORATED_POT).noOcclusion().pushReaction(PushReaction.DESTROY)));

    private static <B extends Block> DeferredBlock<B> registerBlock(String name, Supplier<? extends B> supplier) {
        DeferredBlock<B> block = BLOCK.register(name, supplier);
        BLOCK_TRANSLATIONS.add(block);
        return block;
    }
}
