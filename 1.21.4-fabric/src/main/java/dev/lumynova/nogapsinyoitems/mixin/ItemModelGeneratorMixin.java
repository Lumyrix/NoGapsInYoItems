package dev.lumynova.nogapsinyoitems.mixin;

import java.util.List;
import net.minecraft.class_7764;
import net.minecraft.class_785;
import net.minecraft.class_801;
import dev.lumynova.nogapsinyoitems.model.ItemModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_801.class)
public class ItemModelGeneratorMixin {
    private static final Logger NO_GAPS_LOGGER = LoggerFactory.getLogger("NoGapsInYoItems");
    private static boolean noGapsLogged;

    private static void noGaps$logOnce(String methodName, int layer, String key, class_7764 sprite) {
        if (noGapsLogged) {
            return;
        }
        noGapsLogged = true;
        NO_GAPS_LOGGER.info(
                "[NoGapsInYoItems] Hook ativo em {} (layer={}, key={}, sprite={}, size={}x{})",
                methodName,
                layer,
                key,
                sprite.method_45816(),
                sprite.method_45807(),
                sprite.method_45815()
        );
    }

    @Inject(method = "addLayerElements", at = @At("HEAD"), cancellable = true)
    private void nogaps$replaceLayerElements(int layer, String key, class_7764 sprite, CallbackInfoReturnable<List<class_785>> cir) {
        noGaps$logOnce("addLayerElements", layer, key, sprite);
        cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
    }

    @Inject(method = "addSubComponents", at = @At("HEAD"), cancellable = true)
    private void nogaps$replaceSubComponents(class_7764 sprite, String key, int layer, CallbackInfoReturnable<List<class_785>> cir) {
        noGaps$logOnce("addSubComponents", layer, key, sprite);
        cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
    }
}
