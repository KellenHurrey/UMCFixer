package com.kellen.umcfixer.mixins.umc;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftClient.class, remap = false)
public class MinecraftClientMixin {

    @Shadow
    private static Player playerCache;

    @Inject(method = "Lcam72cam/mod/MinecraftClient;getPlayer()Lcam72cam/mod/entity/Player;", at = @At("RETURN"), cancellable = true)
    private static void getPlayer(CallbackInfoReturnable<Player> cir) {
        if (playerCache == null){
            cir.setReturnValue(new Player(Minecraft.getMinecraft().thePlayer));
        }
    }
}
