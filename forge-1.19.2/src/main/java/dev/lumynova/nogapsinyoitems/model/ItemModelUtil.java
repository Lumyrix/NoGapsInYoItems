package dev.lumynova.nogapsinyoitems.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.joml.Vector3f;

public final class ItemModelUtil {
    private static final float FRONT_Z_MIN = 7.5F;
    private static final float FRONT_Z_MAX = 8.5F;
    private static final float SIDE_Z_EXPAND = 0.08F;
    private static final float SIDE_XY_EXPAND_PX = 0.032F;

    public static List<BlockElement> createPixelLayerElements(int layer, String key, SpriteContents sprite) {
        List<BlockElement> elements = new ArrayList<>();
        int width = sprite.width();
        int height = sprite.height();
        float xFactor = width / 16.0F;
        float yFactor = height / 16.0F;
        int[] frames = getFrameIds(sprite);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
                    elements.add(createFrontBackElement(layer, key, x, y, height, xFactor, yFactor));
                }
            }
        }
        addOutlineElements(elements, sprite, frames, width, height, xFactor, yFactor, layer, key, true);
        addOutlineElements(elements, sprite, frames, width, height, xFactor, yFactor, layer, key, false);
        return elements;
    }

    private static int[] getFrameIds(SpriteContents sprite) {
        try {
            java.lang.reflect.Field framesField = SpriteContents.class.getDeclaredField("frames");
            framesField.setAccessible(true);
            java.util.List<?> frameList = (java.util.List<?>) framesField.get(sprite);
            int[] ids = new int[frameList.size()];
            for (int i = 0; i < frameList.size(); i++) {
                Object frameInfo = frameList.get(i);
                java.lang.reflect.Field indexField = frameInfo.getClass().getDeclaredField("index");
                indexField.setAccessible(true);
                ids[i] = indexField.getInt(frameInfo);
            }
            return ids;
        } catch (Exception e) {
            return new int[]{0};
        }
    }

    // ... (restante dos métodos auxiliares permanece igual)
    private static void addOutlineElements(List<BlockElement> elements, SpriteContents sprite, int[] frames,
                                            int width, int height, float xFactor, float yFactor,
                                            int layer, String key, boolean horizontal) {
        int first = -1, last = -1;
        for (int main = 0; main < (horizontal ? height : width); ++main) {
            for (int sub = 0; sub < (horizontal ? width : height); ++sub) {
                int x = horizontal ? sub : main;
                int y = horizontal ? main : sub;
                if (!isPixelAlwaysTransparent(sprite, frames, x, y) && doesPixelHaveEdge(sprite, frames, x, y,
                        horizontal ? PixelDirection.DOWN : PixelDirection.RIGHT)) {
                    if (first == -1) first = sub;
                    last = sub;
                } else if (first != -1) {
                    elements.add(horizontal
                        ? createHorizontalOutlineElement(Direction.DOWN, layer, key, first, last, main, width, height, xFactor, yFactor)
                        : createVerticalOutlineElement(Direction.EAST, layer, key, first, last, main, width, height, xFactor, yFactor));
                    first = -1;
                }
            }
            if (first != -1) {
                elements.add(horizontal
                    ? createHorizontalOutlineElement(Direction.DOWN, layer, key, first, last, main, width, height, xFactor, yFactor)
                    : createVerticalOutlineElement(Direction.EAST, layer, key, first, last, main, width, height, xFactor, yFactor));
                first = -1;
            }
        }
    }

    private static BlockElement createFrontBackElement(int layer, String key, int x, int y, int height, float xFactor, float yFactor) {
        Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(Direction.SOUTH, new BlockElementFace(null, layer, key, new BlockFaceUV(new float[]{x / xFactor, y / yFactor, (x + 1) / xFactor, (y + 1) / yFactor}, 0)));
        faces.put(Direction.NORTH, new BlockElementFace(null, layer, key, new BlockFaceUV(new float[]{(x + 1) / xFactor, y / yFactor, x / xFactor, (y + 1) / yFactor}, 0)));
        return new BlockElement(
            new Vector3f(x / xFactor, (height - (y + 1)) / yFactor, FRONT_Z_MIN),
            new Vector3f((x + 1) / xFactor, (height - y) / yFactor, FRONT_Z_MAX),
            faces, null, true);
    }

    private static BlockElement createHorizontalOutlineElement(Direction dir, int layer, String key, int start, int end, int y, int width, int height, float xFactor, float yFactor) {
        float pixelStart = clamp(start - SIDE_XY_EXPAND_PX, 0, width);
        float pixelEnd = clamp(end + 1 + SIDE_XY_EXPAND_PX, 0, width);
        Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(dir, new BlockElementFace(null, layer, key, new BlockFaceUV(new float[]{start / xFactor, y / yFactor, (end + 1) / xFactor, (y + 1) / yFactor}, 0)));
        return new BlockElement(
            new Vector3f(pixelStart / xFactor, (height - (y + 1)) / yFactor, FRONT_Z_MIN - SIDE_Z_EXPAND),
            new Vector3f(pixelEnd / xFactor, (height - y) / yFactor, FRONT_Z_MAX + SIDE_Z_EXPAND),
            faces, null, true);
    }

    private static BlockElement createVerticalOutlineElement(Direction dir, int layer, String key, int start, int end, int x, int width, int height, float xFactor, float yFactor) {
        float pixelStart = clamp(start - SIDE_XY_EXPAND_PX, 0, height);
        float pixelEnd = clamp(end + 1 + SIDE_XY_EXPAND_PX, 0, height);
        Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(dir, new BlockElementFace(null, layer, key, new BlockFaceUV(new float[]{(x + 1) / xFactor, start / yFactor, x / xFactor, (end + 1) / yFactor}, 0)));
        return new BlockElement(
            new Vector3f(x / xFactor, (height - pixelEnd) / yFactor, FRONT_Z_MIN - SIDE_Z_EXPAND),
            new Vector3f((x + 1) / xFactor, (height - pixelStart) / yFactor, FRONT_Z_MAX + SIDE_Z_EXPAND),
            faces, null, true);
    }

    private static float clamp(float v, float min, float max) { return Math.max(min, Math.min(max, v)); }
    private static boolean isPixelOutsideSprite(SpriteContents sprite, int x, int y) { return x < 0 || y < 0 || x >= sprite.width() || y >= sprite.height(); }
    private static boolean isPixelTransparent(SpriteContents sprite, int frame, int x, int y) { return isPixelOutsideSprite(sprite, x, y) || sprite.isTransparent(frame, x, y); }
    private static boolean isPixelAlwaysTransparent(SpriteContents sprite, int[] frames, int x, int y) {
        for (int f : frames) if (!isPixelTransparent(sprite, f, x, y)) return false;
        return true;
    }
    private static boolean doesPixelHaveEdge(SpriteContents sprite, int[] frames, int x, int y, PixelDirection dir) {
        int nx = x + dir.offsetX, ny = y + dir.offsetY;
        if (isPixelOutsideSprite(sprite, nx, ny)) return true;
        for (int f : frames) if (!isPixelTransparent(sprite, f, x, y) && isPixelTransparent(sprite, f, nx, ny)) return true;
        return false;
    }
}
