package com.barl_inc.opposing_force.registry.misc;

import com.barl_inc.opposing_force.entity.projectile.Sawblade;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

public class OFCompat {

    public static void registerCompat() {
        registerDispenserBehaviors();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerProjectileBehavior(OFItems.ACID_CHARGE.get());
        DispenserBlock.registerProjectileBehavior(OFItems.ECTOPLASM.get());
        DispenserBlock.registerBehavior(OFItems.SAWBLADE.get(), new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                Position position = DispenserBlock.getDispensePosition(source);
                Sawblade sawblade = OFEntities.SAWBLADE.get().create(source.level());
                sawblade.setPos(position.x(), position.y() - sawblade.getBbHeight() / 2.0F, position.z());
                sawblade.setDeltaMovement(Vec3.atLowerCornerOf(direction.getNormal()).scale(1.25D));
                sawblade.setYRot(180.0F + direction.toYRot());
                sawblade.setLifespan(80);
                source.level().addFreshEntity(sawblade);
                stack.shrink(1);
                return stack;
            }

            @Override
            protected void playSound(BlockSource source) {
                source.level().levelEvent(1002, source.pos(), 0);
            }
        });
    }
}
