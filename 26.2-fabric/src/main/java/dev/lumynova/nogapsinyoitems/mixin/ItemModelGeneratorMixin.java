package dev.lumynova.nogapsinyoitems.mixin;

import dev.lumynova.nogapsinyoitems.model.ItemModelUtil;
import java.util.Collection;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
    private static final Logger NO_GAPS_LOGGER = LoggerFactory.getLogger("NoGapsInYoItems");
    private static boolean noGapsLogged;

    @Inject(method = "getSideFaces", at = @At("HEAD"), cancellable = true)
    private static void nogaps$replaceSideFaces(SpriteContents sprite, CallbackInfoReturnable<Collection<?>> cir) {
        if (!noGapsLogged) {
            noGapsLogged = true;
            NO_GAPS_LOGGER.info(
                    "[NoGapsInYoItems] Hook ativo em getSideFaces (sprite={}, size={}x{})",
                    sprite.name(),
                    sprite.width(),
                    sprite.height()
            );
        }

        cir.setReturnValue(ItemModelUtil.createPixelSideFaces(sprite));
    }
}
