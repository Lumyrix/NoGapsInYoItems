package dev.lumynova.nogapsinyoitems.model;

import net.minecraft.class_2350;

public enum PixelDirection {
    UP(0, -1, class_2350.field_11036),
    DOWN(0, 1, class_2350.field_11033),
    LEFT(-1, 0, class_2350.field_11039),
    RIGHT(1, 0, class_2350.field_11034);

    public static final PixelDirection[] VALUES = values();

    private final int offsetX;
    private final int offsetY;
    private final class_2350 direction;

    PixelDirection(int offsetX, int offsetY, class_2350 direction) {
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

    public class_2350 getDirection() {
        return direction;
    }

    public boolean isVertical() {
        return offsetX == 0;
    }
}
