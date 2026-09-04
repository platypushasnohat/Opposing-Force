package com.barl_inc.opposing_force.entity.base;

import com.platypushasnohat.sinew.entity.ai.navigation.SmoothGroundNavigation;
import com.platypushasnohat.sinew.entity.animation.SmoothAnimationState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class OFMonster extends Monster {

    private static final EntityDataAccessor<Integer> ATTACK_ANIMATION = SynchedEntityData.defineId(OFMonster.class, EntityDataSerializers.INT);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();

    protected OFMonster(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_ANIMATION, 0);
    }

    public int getAttackAnimation() {
        return this.getEntityData().get(ATTACK_ANIMATION);
    }

    public void setAttackAnimation(int flag) {
        this.getEntityData().set(ATTACK_ANIMATION, flag);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
    }

    public void setupAnimationStates() {
    }
}
