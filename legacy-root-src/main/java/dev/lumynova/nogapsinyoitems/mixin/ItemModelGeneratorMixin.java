package dev.lumynova.nogapsinyoitems.mixin;

import java.util.List;

import dev.lumynova.nogapsinyoitems.model.ItemModelUtil;
import net.minecraft.client.render.model.json.GeneratedItemModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.texture.SpriteContents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeneratedItemModel.class)
public class ItemModelGeneratorMixin {
    private static final Logger NO_GAPS_LOGGER = LoggerFactory.getLogger("NoGapsInYoItems");
    private static boolean noGapsLogged;

    private static void noGaps$logOnce(String methodName, int layer, String key, SpriteContents sprite) {
        if (noGapsLogged) {
            return;
        }
        noGapsLogged = true;
        NO_GAPS_LOGGER.info(
                "[NoGapsInYoItems] Hook ativo em {} (layer={}, key={}, sprite={}, size={}x{})",
                methodName,
                layer,
                key,
                sprite.getId(),
                sprite.getWidth(),
                sprite.getHeight()
        );
    }

    @Inject(method = "addLayerElements", at = @At("HEAD"), cancellable = true)
    private static void nogaps$replaceLayerElements(int layer, String key, SpriteContents sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
        noGaps$logOnce("addLayerElements", layer, key, sprite);
        cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
    }

    @Inject(method = "addSubComponents", at = @At("HEAD"), cancellable = true)
    private static void nogaps$replaceSubComponents(SpriteContents sprite, String key, int layer, CallbackInfoReturnable<List<ModelElement>> cir) {
        noGaps$logOnce("addSubComponents", layer, key, sprite);
        cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
    }
}
