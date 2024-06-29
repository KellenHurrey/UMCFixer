package com.kellen.umcfixer.mixins.umc;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.render.ItemRender;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "Lcam72cam/mod/render/ItemRender$BakedItemModel")
public abstract class ItemRenderMixin {

    @Inject(method = "Lcam72cam/mod/render/ItemRender$BakedItemModel;renderItem(Lnet/minecraftforge/client/IItemRenderer$ItemRenderType;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(ItemRender.ItemRenderType type, ItemStack item, Object[] data, CallbackInfo ci){
        if (!MinecraftClient.isReady()) { ci.cancel(); }
    }
}
