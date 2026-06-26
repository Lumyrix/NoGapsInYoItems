package dev.lumynova.nogapsinyoitems.model;

import net.minecraft.util.math.Direction;

public enum PixelDirection {
    UP(0, -1, Direction.UP),
    DOWN(0, 1, Direction.DOWN),
    LEFT(-1, 0, Direction.WEST),
    RIGHT(1, 0, Direction.EAST);

    public static final PixelDirection[] VALUES = values();

    private final int offsetX;
    private final int offsetY;
    private final Direction direction;

    PixelDirection(int offsetX, int offsetY, Direction direction) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.direction = direction;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isVertical() {
        return offsetX == 0;
    }
}
