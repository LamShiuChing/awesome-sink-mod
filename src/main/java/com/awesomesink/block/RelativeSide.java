package com.awesomesink.block;

import net.minecraft.core.Direction;

/**
 * A face named relative to a block's horizontal facing. Lets side configuration follow the block
 * when rotated (concept borrowed from Mekanism's RelativeSide; implemented from scratch).
 */
public enum RelativeSide {
    FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM;

    /** Absolute direction of this face for a block whose front points at {@code facing} (horizontal). */
    public Direction toDirection(Direction facing) {
        return switch (this) {
            case FRONT -> facing;
            case BACK -> facing.getOpposite();
            case LEFT -> facing.getClockWise();
            case RIGHT -> facing.getCounterClockWise();
            case TOP -> Direction.UP;
            case BOTTOM -> Direction.DOWN;
        };
    }

    /** Inverse of {@link #toDirection}: which relative face {@code side} is, given the block's facing. */
    public static RelativeSide fromDirection(Direction facing, Direction side) {
        for (RelativeSide rel : values()) {
            if (rel.toDirection(facing) == side) {
                return rel;
            }
        }
        throw new IllegalStateException("Unreachable: every direction maps to a relative side");
    }
}
