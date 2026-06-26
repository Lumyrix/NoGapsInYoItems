package dev.lumynova.nogapsinyoitems.model;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.renderer.texture.SpriteContents;

public final class ItemModelUtil {
    private static final Constructor<?> SIDE_FACE_CTOR;
    private static final Object SIDE_UP;
    private static final Object SIDE_DOWN;
    private static final Object SIDE_LEFT;
    private static final Object SIDE_RIGHT;

    static {
        try {
            Class<?> sideDirectionClass = Class.forName("net.minecraft.client.resources.model.cuboid.ItemModelGenerator$SideDirection");
            Class<?> sideFaceClass = Class.forName("net.minecraft.client.resources.model.cuboid.ItemModelGenerator$SideFace");

            SIDE_FACE_CTOR = sideFaceClass.getDeclaredConstructor(sideDirectionClass, int.class, int.class);
            SIDE_FACE_CTOR.setAccessible(true);

            SIDE_UP = Enum.valueOf(sideDirectionClass.asSubclass(Enum.class), "UP");
            SIDE_DOWN = Enum.valueOf(sideDirectionClass.asSubclass(Enum.class), "DOWN");
            SIDE_LEFT = Enum.valueOf(sideDirectionClass.asSubclass(Enum.class), "LEFT");
            SIDE_RIGHT = Enum.valueOf(sideDirectionClass.asSubclass(Enum.class), "RIGHT");
        } catch (ReflectiveOperationException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private ItemModelUtil() {
    }

    public static Set<Object> createPixelSideFaces(SpriteContents sprite) {
        Set<Object> sideFaces = new HashSet<>();
        int width = sprite.width();
        int height = sprite.height();
        int[] frames = sprite.getUniqueFrames().toIntArray();

        for (int frame : frames) {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    if (isTransparent(sprite, frame, x, y, width, height)) {
                        continue;
                    }

                    addIfEdge(sideFaces, sprite, frame, x, y, width, height, PixelDirection.UP);
                    addIfEdge(sideFaces, sprite, frame, x, y, width, height, PixelDirection.DOWN);
                    addIfEdge(sideFaces, sprite, frame, x, y, width, height, PixelDirection.LEFT);
                    addIfEdge(sideFaces, sprite, frame, x, y, width, height, PixelDirection.RIGHT);
                }
            }
        }

        return sideFaces;
    }

    private static void addIfEdge(Set<Object> sideFaces, SpriteContents sprite, int frame, int x, int y, int width, int height, PixelDirection direction) {
        int neighborX = x + direction.offsetX();
        int neighborY = y + direction.offsetY();

        if (!isTransparent(sprite, frame, neighborX, neighborY, width, height)) {
            return;
        }

        sideFaces.add(createSideFace(direction.sideDirection(), x, y));
    }

    private static Object createSideFace(Object direction, int x, int y) {
        try {
            return SIDE_FACE_CTOR.newInstance(direction, x, y);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Failed to create ItemModelGenerator side face", exception);
        }
    }

    private static boolean isTransparent(SpriteContents sprite, int frame, int x, int y, int width, int height) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return true;
        }

        return sprite.isTransparent(frame, x, y);
    }

    public static Object sideUp() {
        return SIDE_UP;
    }

    public static Object sideDown() {
        return SIDE_DOWN;
    }

    public static Object sideLeft() {
        return SIDE_LEFT;
    }

    public static Object sideRight() {
        return SIDE_RIGHT;
    }
}
