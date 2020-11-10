package me.fabric.eyephonemod.gui;

import me.fabric.eyephonemod.gui.client.BaseScreen;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ScreenFactory {
    BaseScreen<ClientScreenHandler> createNewScreen(@NotNull ClientScreenHandler handler, @NotNull PlayerInventory inventory, @NotNull Text title);
}
