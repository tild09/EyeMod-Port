package me.fabric.eyephonemod.gui.handler;

import java.util.ArrayList;
import java.util.Arrays;

public interface PacketAction<T extends PacketAction<T>> {

    ArrayList<PacketAction<T>> getActions();

    default int getActionOrdinal() {
        return getActions().indexOf(this);
    }

    enum DefaultPacketAction implements PacketAction<DefaultPacketAction> {
        INIT;

        private static final ArrayList<PacketAction<DefaultPacketAction>> actions = new ArrayList<>();

        static {
            actions.addAll(Arrays.asList(values()));
        }

        @Override
        public ArrayList<PacketAction<DefaultPacketAction>> getActions() {
            return actions;
        }
    }
}
