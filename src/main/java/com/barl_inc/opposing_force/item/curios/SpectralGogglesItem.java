package com.barl_inc.opposing_force.item.curios;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Consumer;

public class SpectralGogglesItem extends CurioItem {

    public static final ResourceLocation SPECTRAL_SIGHT = OpposingForce.location("spectral_goggles");

    public SpectralGogglesItem(Properties properties, CurioDefinition definition) {
        super(properties, definition);
    }

    @Override
    public void addTooltipLines(Consumer<Component> consumer) {
        super.addTooltipLines(component -> consumer.accept(component.copy().withStyle(ChatFormatting.ITALIC)));
    }

    public static void updateSight(Player player) {
        if (CuriosApi.getCuriosInventory(player).map(inventory -> inventory.isEquipped(OFItems.SPECTRAL_GOGGLES.get())).orElse(false)) {
            SpectralSight.addSource(player, SPECTRAL_SIGHT);
        } else {
            SpectralSight.removeSource(player, SPECTRAL_SIGHT);
        }
    }
}
