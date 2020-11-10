package me.fabric.eyephonemod.gui.handler.dummy;

import me.fabric.eyephonemod.gui.handler.PacketAction;

import java.util.ArrayList;
import java.util.Arrays;

public enum DummyPacketAction implements PacketAction<DummyPacketAction> {
    MOUSE_CLICK;

    private static final ArrayList<PacketAction<DummyPacketAction>> actions = new ArrayList<>();

    static {
        actions.addAll(Arrays.asList(values()));
    }

    @Override
    public ArrayList<PacketAction<DummyPacketAction>> getActions() {
        return actions;
    }
}
