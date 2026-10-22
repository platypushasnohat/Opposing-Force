package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.OpposingForce;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class CrystalizedHeartItem extends Item {
    //todo later
    //add gui overlay or visual effect when eating

    private static final ResourceLocation SLOT_MODIFIER = OpposingForce.location("crystallized_heart");

    public CrystalizedHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return hasEatenHeart(player) ? InteractionResultHolder.fail(player.getItemInHand(hand)) : super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && !hasEatenHeart(entity)) {
            CuriosApi.getCuriosInventory(entity).ifPresent(inventory -> inventory.addPermanentSlotModifier("charm", SLOT_MODIFIER, 1.0D, AttributeModifier.Operation.ADD_VALUE));
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.5F, 1.5F);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.opposing_force.crystalized_heart.description").withStyle(ChatFormatting.BLUE));
    }

    public static boolean hasEatenHeart(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).flatMap(inventory -> inventory.getStacksHandler("charm")).map(handler -> handler.getModifiers().containsKey(SLOT_MODIFIER)).orElse(false);
    }
}
