package dev.lumynova.nogapsinyoitems.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;

public final class ItemModelUtil {
    private static final float FRONT_Z_MIN = 7.5F;
    private static final float FRONT_Z_MAX = 8.5F;
    private static final float SIDE_Z_EXPAND = 0.08F;
    private static final float SIDE_XY_EXPAND_PX = 0.08F;

    private ItemModelUtil() {
    }

    public static List<ModelElement> createPixelLayerElements(int layer, String key, SpriteContents sprite) {
        List<ModelElement> elements = new ArrayList<>();

        int width = sprite.getWidth();
        int height = sprite.getHeight();
        float xFactor = width / 16.0F;
        float yFactor = height / 16.0F;
        int[] frames = sprite.getDistinctFrameCount().toArray();

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
                    elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first, last, y, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first, last, y, width, height, xFactor, yFactor));
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
                    elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first, last, y, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first, last, y, width, height, xFactor, yFactor));
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
                    elements.add(createVerticalOutlineElement(Direction.EAST, layer, key, first, last, x, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createVerticalOutlineElement(Direction.EAST, layer, key, first, last, x, width, height, xFactor, yFactor));
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
                    elements.add(createVerticalOutlineElement(Direction.WEST, layer, key, first, last, x, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(createVerticalOutlineElement(Direction.WEST, layer, key, first, last, x, width, height, xFactor, yFactor));
                first = -1;
            }
        }

        return elements;
    }

    private static ModelElement createFrontBackElement(int layer, String key, int x, int y, int height, float xFactor, float yFactor) {
        Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(Direction.SOUTH, new ModelElementFace(
                null,
                layer,
                key,
                new ModelElementFace.UV(x / xFactor, y / yFactor, (x + 1) / xFactor, (y + 1) / yFactor),
                AxisRotation.R0
        ));
        faces.put(Direction.NORTH, new ModelElementFace(
                null,
                layer,
                key,
                new ModelElementFace.UV((x + 1) / xFactor, y / yFactor, x / xFactor, (y + 1) / yFactor),
                AxisRotation.R0
        ));

        return new ModelElement(
                new Vector3f(x / xFactor, (height - (y + 1)) / yFactor, FRONT_Z_MIN),
                new Vector3f((x + 1) / xFactor, (height - y) / yFactor, FRONT_Z_MAX),
                faces,
                null,
                true,
                0
        );
    }

    private static ModelElement createHorizontalOutlineElement(
            Direction direction,
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

        Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(direction, new ModelElementFace(
                null,
                layer,
                key,
                new ModelElementFace.UV(start / xFactor, y / yFactor, (end + 1) / xFactor, (y + 1) / yFactor),
                AxisRotation.R0
        ));

        return new ModelElement(
                new Vector3f(pixelStart / xFactor, (height - (y + 1)) / yFactor, FRONT_Z_MIN - SIDE_Z_EXPAND),
                new Vector3f(pixelEnd / xFactor, (height - y) / yFactor, FRONT_Z_MAX + SIDE_Z_EXPAND),
                faces,
                null,
                true,
                0
        );
    }

    private static ModelElement createVerticalOutlineElement(
            Direction direction,
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

        Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(direction, new ModelElementFace(
                null,
                layer,
                key,
                new ModelElementFace.UV((x + 1) / xFactor, start / yFactor, x / xFactor, (end + 1) / yFactor),
                AxisRotation.R0
        ));

        return new ModelElement(
                new Vector3f(x / xFactor, (height - pixelEnd) / yFactor, FRONT_Z_MIN - SIDE_Z_EXPAND),
                new Vector3f((x + 1) / xFactor, (height - pixelStart) / yFactor, FRONT_Z_MAX + SIDE_Z_EXPAND),
                faces,
                null,
                true,
                0
        );
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static boolean isPixelOutsideSprite(SpriteContents sprite, int x, int y) {
        return x < 0 || y < 0 || x >= sprite.getWidth() || y >= sprite.getHeight();
    }

    private static boolean isPixelTransparent(SpriteContents sprite, int frame, int x, int y) {
        return isPixelOutsideSprite(sprite, x, y) || sprite.isPixelTransparent(frame, x, y);
    }

    private static boolean isPixelAlwaysTransparent(SpriteContents sprite, int[] frames, int x, int y) {
        for (int frame : frames) {
            if (!isPixelTransparent(sprite, frame, x, y)) {
                return false;
            }
        }
        return true;
    }

    private static boolean doesPixelHaveEdge(SpriteContents sprite, int[] frames, int x, int y, PixelDirection direction) {
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
