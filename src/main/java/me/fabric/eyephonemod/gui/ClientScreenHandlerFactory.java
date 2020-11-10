package me.fabric.eyephonemod.gui;

import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;

@FunctionalInterface
public interface ClientScreenHandlerFactory<T extends ClientScreenHandler> {
    T createNewScreenHandler(int syncId);
}
