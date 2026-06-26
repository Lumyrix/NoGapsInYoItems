package dev.lumynova.nogapsinyoitems.mixin;

import dev.lumynova.nogapsinyoitems.context.RenderContext;
import net.minecraft.class_1058;
import net.minecraft.class_2960;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1058.class)
public class SpriteMixin {
    private static final Logger NO_GAPS_LOGGER = LoggerFactory.getLogger("NoGapsInYoItems");
    private static boolean noGapsLogged;

    @Inject(method = "getUvScaleDelta", at = @At("HEAD"), cancellable = true)
    private void nogaps$disableTextureZoom(CallbackInfoReturnable<Float> cir) {
        if (!RenderContext.isRenderingFirstPersonHeldItem()) {
            return;
        }

        class_1058 self = (class_1058) (Object) this;
        class_2960 spriteId = self.method_45851().method_45816();
        if (!spriteId.method_12832().startsWith("item/")) {
            return;
        }

        if (!noGapsLogged) {
            noGapsLogged = true;
            NO_GAPS_LOGGER.info("[NoGapsInYoItems] UV shrink ativo apenas para item na sua mao");
        }
        cir.setReturnValue(0.0F);
    }
}
