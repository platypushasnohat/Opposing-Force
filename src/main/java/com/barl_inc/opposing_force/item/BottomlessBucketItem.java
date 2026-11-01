package com.barl_inc.opposing_force.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BottomlessBucketItem extends BucketItem {

    public BottomlessBucketItem(Fluid content, Properties properties) {
        super(content, properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        } else if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        } else {
            BlockPos resultBlockPos = hitResult.getBlockPos();
            Direction direction = hitResult.getDirection();
            BlockPos relative = resultBlockPos.relative(direction);
            if (level.mayInteract(player, resultBlockPos) && player.mayUseItemAt(relative, direction, stack)) {
                BlockState state = level.getBlockState(resultBlockPos);
                BlockPos blockPos = this.canBlockContainFluid(player, level, resultBlockPos, state) ? resultBlockPos : relative;
                if (this.emptyContents(player, level, blockPos, hitResult, stack)) {
                    this.checkExtraContent(player, level, stack, blockPos);
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, blockPos, stack);
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                } else {
                    return InteractionResultHolder.fail(stack);
                }
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }
    }
}
