package com.kellen.umcfixer.mixins.umc;

import cam72cam.mod.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "cam72cam/mod/render/ItemRender$BakedItemModel", remap = false)
public abstract class BakedItemModelMixin {

    @Inject(method = "renderItem(Lnet/minecraftforge/client/IItemRenderer$ItemRenderType;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(IItemRenderer.ItemRenderType typeIn, ItemStack item, Object[] data, CallbackInfo ci){
        if (!MinecraftClient.isReady()) { ci.cancel(); }
    }
}
