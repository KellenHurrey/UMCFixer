package com.kellen.umcfixer.mixins.umc;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MinecraftClient.class, remap = false)
public class MinecraftClientMixin {

    @Shadow
    private static Player playerCache;

    @Overwrite
    public static Player getPlayer() {
        EntityPlayerSP internal = Minecraft.getMinecraft().thePlayer;
        if (internal == null) {
            throw new RuntimeException("Called to get the player before minecraft has actually started!");
        } else {
            if (playerCache == null || internal != playerCache.internal) {
                playerCache = World.get(internal.worldObj).getEntity(internal).asPlayer();
            }
            if (playerCache == null){
                return new Player(Minecraft.getMinecraft().thePlayer);
            }
            return playerCache;
        }
    }
}
