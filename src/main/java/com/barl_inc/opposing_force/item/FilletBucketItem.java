package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.registry.OFEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class FilletBucketItem extends MobBucketItem {

    public FilletBucketItem(Properties properties) {
        super(OFEntities.FILLET.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties.stacksTo(1));
    }

    private void spawn(ServerLevel level, ItemStack stack, BlockPos pos) {
        Entity entity = OFEntities.FILLET.get().spawn(level, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            CustomData customdata = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
            bucketable.loadFromBucketTag(customdata.copyTag());
            bucketable.setFromBucket(true);
        }
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack stack, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            this.spawn(serverLevel, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }
}