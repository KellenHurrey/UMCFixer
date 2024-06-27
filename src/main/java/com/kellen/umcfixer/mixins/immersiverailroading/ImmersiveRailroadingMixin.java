package com.kellen.umcfixer.mixins.immersiverailroading;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Entity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.render.opengl.RenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ImmersiveRailroading.class, remap = false)
abstract class ImmersiveRailroadingMixin{

    @Inject(method = "lambda$clientEvent$3(Lcam72cam/mod/render/opengl/RenderState;F)V", at = @At("HEAD"), cancellable = true)
    private static void lambda$clientEvent$3(RenderState state, float pt, CallbackInfo ci){
        Player player = MinecraftClient.getPlayer();
        if (player == null) {
            ci.cancel();
            return;
        }
        Entity riding = player.getRiding();
        if (riding instanceof EntityRollingStock) {
            EntityRollingStock stock = (EntityRollingStock)riding;
            if (stock.getDefinition().getOverlay() != null) {
                stock.getDefinition().getOverlay().render(state, stock);
            }
        }
        ci.cancel();
    }

}