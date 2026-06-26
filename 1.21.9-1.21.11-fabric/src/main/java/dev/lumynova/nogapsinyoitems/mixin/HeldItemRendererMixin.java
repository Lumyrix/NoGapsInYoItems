package dev.lumynova.nogapsinyoitems.mixin;

import dev.lumynova.nogapsinyoitems.context.RenderContext;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_759.class)
public class HeldItemRendererMixin {
    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), require = 0)
    private void nogaps$enterFirstPersonContext(CallbackInfo ci) {
        RenderContext.pushFirstPersonHeldItem();
    }

    @Inject(method = "renderFirstPersonItem", at = @At("RETURN"), require = 0)
    private void nogaps$exitFirstPersonContext(CallbackInfo ci) {
        RenderContext.popFirstPersonHeldItem();
    }
}
