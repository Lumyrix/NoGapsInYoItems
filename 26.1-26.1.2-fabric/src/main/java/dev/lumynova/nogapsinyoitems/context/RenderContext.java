package dev.lumynova.nogapsinyoitems.context;

public final class RenderContext {
    private static final ThreadLocal<Integer> FIRST_PERSON_DEPTH = ThreadLocal.withInitial(() -> 0);

    private RenderContext() {
    }

    public static void pushFirstPersonHeldItem() {
        FIRST_PERSON_DEPTH.set(FIRST_PERSON_DEPTH.get() + 1);
    }

    public static void popFirstPersonHeldItem() {
        int depth = FIRST_PERSON_DEPTH.get();
        FIRST_PERSON_DEPTH.set(Math.max(0, depth - 1));
    }

    public static boolean isRenderingFirstPersonHeldItem() {
        return FIRST_PERSON_DEPTH.get() > 0;
    }
}
