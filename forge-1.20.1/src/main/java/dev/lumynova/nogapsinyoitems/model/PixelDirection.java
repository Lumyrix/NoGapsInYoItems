package dev.lumynova.nogapsinyoitems.model;

import net.minecraft.core.Direction;

public enum PixelDirection {
    UP(0, -1, Direction.UP),
    DOWN(0, 1, Direction.DOWN),
    LEFT(-1, 0, Direction.WEST),
    RIGHT(1, 0, Direction.EAST);

    public final int offsetX, offsetY;
    public final Direction direction;

    PixelDirection(int x, int y, Direction dir) { offsetX = x; offsetY = y; direction = dir; }
}
