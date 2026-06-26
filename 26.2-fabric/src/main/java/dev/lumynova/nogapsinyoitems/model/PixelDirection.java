package dev.lumynova.nogapsinyoitems.model;

public enum PixelDirection {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int offsetX;
    private final int offsetY;

    PixelDirection(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public int offsetX() {
        return offsetX;
    }

    public int offsetY() {
        return offsetY;
    }

    public Object sideDirection() {
        return switch (this) {
            case UP -> ItemModelUtil.sideUp();
            case DOWN -> ItemModelUtil.sideDown();
            case LEFT -> ItemModelUtil.sideLeft();
            case RIGHT -> ItemModelUtil.sideRight();
        };
    }
}
