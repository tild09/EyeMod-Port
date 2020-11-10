package me.fabric.eyephonemod.gui.handler;

import java.util.ArrayList;

public interface PacketAction {
    ArrayList<PacketAction> PACKET_ACTIONS = new ArrayList<>();

    default int getActionOrdinal() {
        return PACKET_ACTIONS.indexOf(this);
    }

    enum DefaultPacketAction implements PacketAction {
        INIT
    }
}
