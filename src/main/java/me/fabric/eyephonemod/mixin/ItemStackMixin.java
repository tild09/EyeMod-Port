package me.fabric.eyephonemod.mixin;

import me.fabric.eyephonemod.item.ScreenHandlingItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Shadow private CompoundTag tag;

    @Inject(method = "toTag", at = @At("HEAD"))
    private void onToTag(CompoundTag incomingTag, CallbackInfoReturnable<CompoundTag> cir) {
        if (!(((ItemStack) (Object) this).getItem() instanceof ScreenHandlingItem)) return;
        if (tag == null) return;
        ((ScreenHandlingItem) ((ItemStack) (Object) this).getItem()).serializeStack((((ItemStack) (Object) this)), tag);
    }
}
