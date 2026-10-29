package com.barl_inc.opposing_force.entity.misc;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GrellShuffle {

    public static final int COLUMNS = 4;
    public static final int ROWS = 2;
    public static final int SLOTS = COLUMNS * ROWS;

    private final GrellClone[] clones;
    private final RandomSource random;
    private final double distance;
    private final double height;
    private final int moveTicks;
    private final int pauseTicks;
    private final int[] moves = new int[SLOTS];
    private Vec3 center;
    private Vec3 right;
    private Vec3 back;
    private int movesLeft;
    @Nullable
    private Move move;
    private int timer;
    private boolean flipped;
    private boolean collapseAll;

    public GrellShuffle(GrellClone[] clones, RandomSource random, LivingEntity target, Vec3 center, Vec3 back, boolean fast) {
        Vec3 eye = target.getEyePosition();
        this.clones = clones;
        this.random = random;
        this.center = center;
        this.back = back;
        this.right = rightOf(back);
        this.distance = center.subtract(eye).horizontalDistance();
        this.height = center.y - eye.y;
        if (fast) {
            this.moveTicks = 5;
            this.pauseTicks = 1;
            this.movesLeft = 33;
        } else {
            this.moveTicks = 8;
            this.pauseTicks = 2;
            this.movesLeft = 20;
        }
        for (int slot = 0; slot < SLOTS; slot++) {
            this.moves[slot] = slot;
        }
        this.timer = -GrellClone.FADE_TICKS;
    }

    private static Vec3 rightOf(Vec3 back) {
        return new Vec3(-back.z, 0.0D, back.x);
    }

    public Vec3 getCenter() {
        return this.center;
    }

    public Vec3 getSlotPosition(int slot) {
        return this.center.add(this.getOffset(column(slot) - 1.5D, 0.5D - row(slot), 0.0D));
    }

    private Vec3 getOffset(double x, double y, double depth) {
        return this.right.scale(x * 2.0D).add(0.0D, y * 2.3D, 0.0D).add(this.back.scale(depth));
    }

    private static int column(int slot) {
        return slot % COLUMNS;
    }

    private static int row(int slot) {
        return slot / COLUMNS;
    }

    private static int slot(int column, int row) {
        return row * COLUMNS + column;
    }

    public GrellClone getClone(int slot) {
        return this.clones[slot];
    }

    public boolean isDone() {
        return this.movesLeft <= 0 && this.move == null;
    }

    public void tick(LivingEntity target) {
        this.follow(target);
        if (this.move == null && this.timer >= this.pauseTicks && this.movesLeft > 0) {
            this.startMove();
        }
        this.timer++;
        float progress = 1.0F;
        if (this.move != null) {
            progress = Math.min((float) this.timer / this.moveTicks, 1.0F);
        }
        float eased = progress * progress * (3.0F - 2.0F * progress);
        for (int slot = 0; slot < SLOTS; slot++) {
            GrellClone clone = this.clones[slot];
            if (clone != null) {
                this.place(clone, slot, eased, target);
            }
        }
        if (this.move != null && progress >= 1.0F) {
            this.finishMove();
        }
    }

    private void follow(LivingEntity target) {
        Vec3 eye = target.getEyePosition();
        Vec3 look = target.getLookAngle().multiply(1.0D, 0.0D, 1.0D);
        if (look.lengthSqr() > 1.0E-4D) {
            Vec3 forward = this.back.lerp(look.normalize(), 0.25D).normalize();
            Vec3 wanted = eye.add(forward.scale(this.distance)).add(0.0D, this.height, 0.0D);
            HitResult hit = target.level().clip(new ClipContext(eye, wanted, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target));
            if (hit.getType() != HitResult.Type.MISS) {
                double reach = Math.max(hit.getLocation().distanceTo(eye) - 1.5D, 2.0D);
                wanted = eye.add(wanted.subtract(eye).normalize().scale(reach));
            }
            this.back = forward;
            this.right = rightOf(forward);
            this.center = this.center.lerp(wanted, 0.25D);
        }
    }

    private void place(GrellClone clone, int slot, float eased, LivingEntity target) {
        int to = this.moves[slot];
        Vec3 pos;
        float roll = 0.0F;
        if (this.flipped) {
            roll = 180.0F;
        }
        if (this.move == Move.FLIP) {
            double x = column(slot) - 1.5D;
            double y = 0.5D - row(slot);
            float angle = Mth.PI * eased;
            pos = this.center.add(this.getOffset(x * Mth.cos(angle) - y * Mth.sin(angle), x * Mth.sin(angle) + y * Mth.cos(angle), 0.0D));
            roll += 180.0F * eased;
        } else if (this.move == Move.COLLAPSE) {
            Vec3 middle = this.getCollapseCenter(slot);
            if (eased < 0.5F) {
                pos = this.getSlotPosition(slot).lerp(middle, eased * 2.0F);
            } else {
                pos = middle.lerp(this.getSlotPosition(to), eased * 2.0F - 1.0F);
            }
        } else {
            Vec3 from = this.getSlotPosition(slot);
            Vec3 end = this.getSlotPosition(to);
            double depth = 0.0D;
            if (this.move != null && slot != to) {
                double side = 1.0D;
                if ((slot + to) % 2 == 0) {
                    side = -1.0D;
                }
                depth = Mth.sin(Mth.PI * eased) * 1.2D * side;
            }
            pos = from.lerp(end, eased).add(this.back.scale(depth));
        }
        clone.moveInFormation(pos, target, roll);
    }

    private Vec3 getCollapseCenter(int slot) {
        double x = 1.0D;
        if (this.collapseAll) {
            x = 0.0D;
        } else if (column(slot) < 2) {
            x = -1.0D;
        }
        return this.center.add(this.getOffset(x, 0.0D, 0.0D));
    }

    private void finishMove() {
        GrellClone[] moved = new GrellClone[SLOTS];
        for (int slot = 0; slot < SLOTS; slot++) {
            moved[this.moves[slot]] = this.clones[slot];
            this.moves[slot] = slot;
        }
        System.arraycopy(moved, 0, this.clones, 0, SLOTS);
        if (this.move == Move.FLIP) {
            this.flipped = !this.flipped;
        }
        this.move = null;
        this.timer = 0;
        this.movesLeft--;
    }

    private void startMove() {
        this.timer = 0;
        if (this.movesLeft == 1 && this.flipped) {
            this.move = Move.FLIP;
        } else {
            this.move = Util.getRandom(Move.values(), this.random);
        }
        boolean reverse = this.random.nextBoolean();
        switch (this.move) {
            case ROTATE -> this.rotateBlock(this.random.nextInt(COLUMNS - 1), reverse);
            case MESSY -> this.messy(reverse);
            case CHAIN -> this.cycle(new int[]{slot(0, 0), slot(1, 0), slot(2, 0), slot(3, 0), slot(3, 1), slot(2, 1), slot(1, 1), slot(0, 1)}, reverse);
            case SECTION -> this.section();
            case CROSS -> this.cross();
            case FLIP -> this.flip();
            case COLLAPSE -> this.collapse();
            case LINE -> this.line(reverse);
        }
    }

    private void cycle(int[] order, boolean reverse) {
        for (int i = 0; i < order.length; i++) {
            int next = (i + 1) % order.length;
            if (reverse) {
                this.moves[order[next]] = order[i];
            } else {
                this.moves[order[i]] = order[next];
            }
        }
    }

    private void rotateBlock(int column, boolean reverse) {
        this.cycle(new int[]{slot(column, 0), slot(column + 1, 0), slot(column + 1, 1), slot(column, 1)}, reverse);
    }

    private void messy(boolean reverse) {
        int row = this.random.nextInt(ROWS);
        int leftOut = slot(1, row);
        int rightOut = slot(2, 1 - row);
        this.cycle(new int[]{slot(0, row), slot(0, 1 - row), slot(1, 1 - row)}, reverse);
        this.cycle(new int[]{slot(3, row), slot(3, 1 - row), slot(2, row)}, !reverse);
        this.moves[leftOut] = rightOut;
        this.moves[rightOut] = leftOut;
    }

    private void section() {
        for (int slot = 0; slot < SLOTS; slot++) {
            this.moves[slot] = slot((column(slot) + 2) % COLUMNS, row(slot));
        }
    }

    private void cross() {
        for (int slot = 0; slot < SLOTS; slot++) {
            this.moves[slot] = slot(column(slot) ^ 1, 1 - row(slot));
        }
    }

    private void flip() {
        for (int slot = 0; slot < SLOTS; slot++) {
            this.moves[slot] = slot(COLUMNS - 1 - column(slot), ROWS - 1 - row(slot));
        }
    }

    private void collapse() {
        this.collapseAll = this.random.nextInt(3) == 0;
        if (this.collapseAll) {
            this.scramble(new int[]{slot(0, 0), slot(1, 0), slot(2, 0), slot(3, 0), slot(0, 1), slot(1, 1), slot(2, 1), slot(3, 1)});
        } else {
            this.scramble(new int[]{slot(0, 0), slot(1, 0), slot(0, 1), slot(1, 1)});
            this.scramble(new int[]{slot(2, 0), slot(3, 0), slot(2, 1), slot(3, 1)});
        }
    }

    private void scramble(int[] group) {
        int[] targets = group.clone();
        for (int i = targets.length - 1; i > 0; i--) {
            int j = this.random.nextInt(i + 1);
            int swap = targets[i];
            targets[i] = targets[j];
            targets[j] = swap;
        }
        for (int i = 0; i < group.length; i++) {
            this.moves[group[i]] = targets[i];
        }
    }

    private void line(boolean reverse) {
        int shift = 1;
        if (reverse) {
            shift = COLUMNS - 1;
        }
        for (int slot = 0; slot < SLOTS; slot++) {
            this.moves[slot] = slot((column(slot) + shift) % COLUMNS, row(slot));
        }
    }

    private enum Move {
        ROTATE,
        MESSY,
        CHAIN,
        SECTION,
        CROSS,
        FLIP,
        LINE,
        COLLAPSE
    }
}
