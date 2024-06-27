package com.kellen.umcfixer.mixins.umc;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.render.GlobalRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GlobalRender.class, remap = false)
abstract class GlobalRenderMixin{

    @Inject(method = "lambda$registerItemMouseover$4(Lcam72cam/mod/item/CustomItem;Lcam72cam/mod/render/GlobalRender$MouseoverEvent;Ljava/lang/Float;)V", at = @At("HEAD"), cancellable = true)
    private static void lambda$registerItemMouseover$4(CustomItem item, GlobalRender.MouseoverEvent fn, Float partialTicks, CallbackInfo ci){
        if (MinecraftClient.getBlockMouseOver() != null && MinecraftClient.getPlayer() == null){
            ci.cancel();
        }
    }
}