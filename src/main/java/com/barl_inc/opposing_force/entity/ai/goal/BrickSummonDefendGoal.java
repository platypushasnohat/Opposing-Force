package com.barl_inc.opposing_force.entity.ai.goal;

import com.barl_inc.opposing_force.entity.Brick;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BrickSummonDefendGoal extends Goal {

    private final Brick brick;
    private int nextDefendTick = 30;
    private int timer;

    public BrickSummonDefendGoal(Brick brick) {
        this.brick = brick;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.brick.isSummoned() && this.brick.onGround() && this.brick.getAnimationState() == 0 && this.brick.tickCount >= this.nextDefendTick;
    }

    @Override
    public boolean canContinueToUse() {
        return this.brick.isAlive() && this.timer <= 100;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.timer = 0;
        this.brick.getNavigation().stop();
        this.brick.setAnimationState(Brick.DEFEND_ANIMATION);
    }

    @Override
    public void stop() {
        if (this.brick.getAnimationState() == Brick.DEFEND_ANIMATION) {
            this.brick.setAnimationState(0);
        }
        this.nextDefendTick = this.brick.tickCount + 100 + this.brick.getRandom().nextInt(100);
    }

    @Override
    public void tick() {
        this.timer++;
        this.brick.getNavigation().stop();
        if (this.timer % 20 == 1) {
            this.brick.level().playSound(null, this.brick.getX(), this.brick.getY(), this.brick.getZ(), SoundEvents.EVOKER_CAST_SPELL, this.brick.getSoundSource(), 0.5F, 1.25F * SinewSoundUtils.randomizePitch(this.brick));
            for (LivingEntity entity : this.brick.level().getEntitiesOfClass(LivingEntity.class, this.brick.getBoundingBox().inflate(8.0D), entity -> entity.isAlive() && (entity == this.brick || this.brick.isAlliedTo(entity)))) {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 2), this.brick);
            }
            if (this.brick.level() instanceof ServerLevel serverLevel) {
                this.brick.spawnSporeParticles(serverLevel, 8);
            }
        }
    }
}
