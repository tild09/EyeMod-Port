package me.fabric.eyephonemod.gui;

import me.fabric.eyephonemod.gui.handler.PacketAction;

@FunctionalInterface
public interface PacketActionsProvider {
    PacketAction[] get();
}
