package dev.lumynova.nogapsinyoitems.mixin;

import dev.lumynova.nogapsinyoitems.context.RenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

@Mixin(Sprite.class)
public class SpriteMixin {
    private static final Logger NO_GAPS_LOGGER = LoggerFactory.getLogger("NoGapsInYoItems");
    private static boolean noGapsLogged;

    @Inject(method = "getUvScaleDelta", at = @At("HEAD"), cancellable = true)
    private void nogaps$disableTextureZoom(CallbackInfoReturnable<Float> cir) {
        if (!RenderContext.isRenderingFirstPersonHeldItem()) {
            return;
        }

        Sprite self = (Sprite) (Object) this;
        Identifier spriteId = self.getContents().getId();
        if (!spriteId.getPath().startsWith("item/")) {
            return;
        }

        if (!noGapsLogged) {
            noGapsLogged = true;
            NO_GAPS_LOGGER.info("[NoGapsInYoItems] UV shrink ativo apenas para item na sua mao");
        }
        cir.setReturnValue(0.0F);
    }
}
