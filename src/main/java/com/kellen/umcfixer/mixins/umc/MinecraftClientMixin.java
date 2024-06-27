package com.kellen.umcfixer.mixins.umc;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftClient.class, remap = false)
public class MinecraftClientMixin {

    @Shadow
    private static Player playerCache;

    @Inject(method = "Lcam72cam/mod/MinecraftClient;getPlayer()Lcam72cam/mod/entity/Player;", at = @At("HEAD"), cancellable = true)
    private static void getPlayer(CallbackInfoReturnable<Player> cir) {
        EntityPlayerSP internal = Minecraft.getMinecraft().thePlayer;
        if (internal == null) {
            throw new RuntimeException("Called to get the player before minecraft has actually started!");
        } else {
            if (playerCache == null || internal != playerCache.internal) {
                if (internal.worldObj == null || World.get(internal.worldObj) == null || internal.getUniqueID() == null){
                    cir.setReturnValue(null);
                }
                playerCache = World.get(internal.worldObj).getEntity(internal).asPlayer();
            }

            cir.setReturnValue(playerCache);
        }
    }
}
