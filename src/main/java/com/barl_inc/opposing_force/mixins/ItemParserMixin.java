package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.debug.OFDebugItems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemParser.class)
public class ItemParserMixin {

    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/HolderLookup$Provider;lookupOrThrow(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/core/HolderLookup$RegistryLookup;"))
    private HolderLookup.RegistryLookup<Item> opposingForce$hideDebugItems(HolderLookup.RegistryLookup<Item> items) {
        return items.filterElements(item -> !OFDebugItems.isDebugItem(item));
    }
}
