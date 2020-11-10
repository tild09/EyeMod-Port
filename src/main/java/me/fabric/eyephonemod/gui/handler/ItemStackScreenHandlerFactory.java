package me.fabric.eyephonemod.gui.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public interface ItemStackScreenHandlerFactory {

    Text getDisplayName();

    @Nullable ScreenHandler createMenu(int syncId, ServerPlayerEntity player, ItemStack itemStack);
}
