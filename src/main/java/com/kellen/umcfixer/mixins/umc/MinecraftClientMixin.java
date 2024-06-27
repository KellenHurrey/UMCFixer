package com.kellen.umcfixer.mixins.umc;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Entity;
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

    // Add getPlayer not null as part of being ready
    @Inject(method = "isReady()Z", at = @At("HEAD"), cancellable = true)
    private static void isReady(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && MinecraftClient.getPlayer() != null);
    }

    // Handle the possibility of a null world (happens when going through dimensions)
    @Inject(method = "getPlayer()Lcam72cam/mod/entity/Player;", at = @At("HEAD"), cancellable = true)
    private static void getPlayer(CallbackInfoReturnable<Player> cir) {
        EntityPlayerSP internal = Minecraft.getMinecraft().thePlayer;
        if (internal == null) {
            throw new RuntimeException("Called to get the player before minecraft has actually started!");
        } else {
            if (playerCache == null || internal != playerCache.internal) {
                if (internal.worldObj == null || World.get(internal.worldObj) == null || internal.getUniqueID() == null || World.get(internal.worldObj).getEntity(internal) == null){
                    cir.setReturnValue(null);
                    return;
                }
                playerCache = World.get(internal.worldObj).getEntity(internal).asPlayer();
            }
            cir.setReturnValue(playerCache);
        }
    }

    // Fix a NullPointerException while looking at an entity
    @Inject(method = "Lcam72cam/mod/MinecraftClient;getEntityMouseOver()Lcam72cam/mod/entity/Entity;", at = @At("HEAD"), cancellable = true)
    private static void getEntityMouseOver(CallbackInfoReturnable<Entity> cir){
        if (MinecraftClient.getPlayer() == null){
            cir.setReturnValue(null);
        }
    }
}
