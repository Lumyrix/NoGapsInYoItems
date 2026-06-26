package dev.lumynova.nogapsinyoitems.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_2350;
import net.minecraft.class_7764;
import net.minecraft.class_783;
import net.minecraft.class_785;
import net.minecraft.class_787;
import org.joml.Vector3f;

public final class ItemModelUtil {
    private static final float FRONT_Z_MIN = 7.5F;
    private static final float FRONT_Z_MAX = 8.5F;
    private static final float SIDE_Z_EXPAND = 0.08F;
    private static final float SIDE_XY_EXPAND_PX = 0.032F;

    private ItemModelUtil() {
    }

    public static List<class_785> createPixelLayerElements(int layer, String key, class_7764 sprite) {
        List<class_785> elements = new ArrayList<>();

        int width = sprite.method_45807();
        int height = sprite.method_45815();
        float xFactor = width / 16.0F;
        float yFactor = height / 16.0F;
        int[] frames = sprite.method_45817().toArray();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
                    elements.add(createFrontBackElement(layer, key, x, y, height, xFactor, yFactor));
                }
            }
        }

        int first = -1;
        int last = -1;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y) && doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.DOWN)) {
                    if (first == -1) {
                        first = x;
                    }
                    last = x;
                } else if (first != -1) {
                    elements.add(createHorizontalOutlineElement(class_2350.field_11033, layer, key, first, last, y, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createHorizontalOutlineElement(class_2350.field_11033, layer, key, first, last, y, width, height, xFactor, yFactor));
                first = -1;
            }
        }

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y) && doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.UP)) {
                    if (first == -1) {
                        first = x;
                    }
                    last = x;
                } else if (first != -1) {
                    elements.add(createHorizontalOutlineElement(class_2350.field_11036, layer, key, first, last, y, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createHorizontalOutlineElement(class_2350.field_11036, layer, key, first, last, y, width, height, xFactor, yFactor));
                first = -1;
            }
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y) && doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.RIGHT)) {
                    if (first == -1) {
                        first = y;
                    }
                    last = y;
                } else if (first != -1) {
                    elements.add(createVerticalOutlineElement(class_2350.field_11034, layer, key, first, last, x, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createVerticalOutlineElement(class_2350.field_11034, layer, key, first, last, x, width, height, xFactor, yFactor));
                first = -1;
            }
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y) && doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.LEFT)) {
                    if (first == -1) {
                        first = y;
                    }
                    last = y;
                } else if (first != -1) {
                    elements.add(createVerticalOutlineElement(class_2350.field_11039, layer, key, first, last, x, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createVerticalOutlineElement(class_2350.field_11039, layer, key, first, last, x, width, height, xFactor, yFactor));
                first = -1;
            }
        }

        return elements;
    }

    private static class_785 createFrontBackElement(int layer, String key, int x, int y, int height, float xFactor, float yFactor) {
        Map<class_2350, class_783> faces = new EnumMap<>(class_2350.class);
        faces.put(class_2350.field_11035, new class_783(
                null,
                layer,
                key,
                new class_787(new float[] {x / xFactor, y / yFactor, (x + 1) / xFactor, (y + 1) / yFactor}, 0)
        ));
        faces.put(class_2350.field_11043, new class_783(
                null,
                layer,
                key,
                new class_787(new float[] {(x + 1) / xFactor, y / yFactor, x / xFactor, (y + 1) / yFactor}, 0)
        ));

        return new class_785(
                new Vector3f(x / xFactor, (height - (y + 1)) / yFactor, FRONT_Z_MIN),
                new Vector3f((x + 1) / xFactor, (height - y) / yFactor, FRONT_Z_MAX),
                faces,
                null,
                true
        );
    }

    private static class_785 createHorizontalOutlineElement(
            class_2350 direction,
            int layer,
            String key,
            int start,
            int end,
            int y,
            int width,
            int height,
            float xFactor,
            float yFactor
    ) {
        float pixelStart = clamp(start - SIDE_XY_EXPAND_PX, 0.0F, width);
        float pixelEnd = clamp(end + 1 + SIDE_XY_EXPAND_PX, 0.0F, width);

        Map<class_2350, class_783> faces = new EnumMap<>(class_2350.class);
        faces.put(direction, new class_783(
                null,
                layer,
                key,
                new class_787(new float[] {start / xFactor, y / yFactor, (end + 1) / xFactor, (y + 1) / yFactor}, 0)
        ));

        return new class_785(
                new Vector3f(pixelStart / xFactor, (height - (y + 1)) / yFactor, FRONT_Z_MIN - SIDE_Z_EXPAND),
                new Vector3f(pixelEnd / xFactor, (height - y) / yFactor, FRONT_Z_MAX + SIDE_Z_EXPAND),
                faces,
                null,
                true
        );
    }

    private static class_785 createVerticalOutlineElement(
            class_2350 direction,
            int layer,
            String key,
            int start,
            int end,
            int x,
            int width,
            int height,
            float xFactor,
            float yFactor
    ) {
        float pixelStart = clamp(start - SIDE_XY_EXPAND_PX, 0.0F, height);
        float pixelEnd = clamp(end + 1 + SIDE_XY_EXPAND_PX, 0.0F, height);

        Map<class_2350, class_783> faces = new EnumMap<>(class_2350.class);
        faces.put(direction, new class_783(
                null,
                layer,
                key,
                new class_787(new float[] {(x + 1) / xFactor, start / yFactor, x / xFactor, (end + 1) / yFactor}, 0)
        ));

        return new class_785(
                new Vector3f(x / xFactor, (height - pixelEnd) / yFactor, FRONT_Z_MIN - SIDE_Z_EXPAND),
                new Vector3f((x + 1) / xFactor, (height - pixelStart) / yFactor, FRONT_Z_MAX + SIDE_Z_EXPAND),
                faces,
                null,
                true
        );
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static boolean isPixelOutsideSprite(class_7764 sprite, int x, int y) {
        return x < 0 || y < 0 || x >= sprite.method_45807() || y >= sprite.method_45815();
    }

    private static boolean isPixelTransparent(class_7764 sprite, int frame, int x, int y) {
        return isPixelOutsideSprite(sprite, x, y) || sprite.method_45810(frame, x, y);
    }

    private static boolean isPixelAlwaysTransparent(class_7764 sprite, int[] frames, int x, int y) {
        for (int frame : frames) {
            if (!isPixelTransparent(sprite, frame, x, y)) {
                return false;
            }
        }
        return true;
    }

    private static boolean doesPixelHaveEdge(class_7764 sprite, int[] frames, int x, int y, PixelDirection direction) {
        int x1 = x + direction.getOffsetX();
        int y1 = y + direction.getOffsetY();
        if (isPixelOutsideSprite(sprite, x1, y1)) {
            return true;
        }
        for (int frame : frames) {
            if (!isPixelTransparent(sprite, frame, x, y) && isPixelTransparent(sprite, frame, x1, y1)) {
                return true;
            }
        }
        return false;
    }
}
