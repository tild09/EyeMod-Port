package me.fabric.eyephonemod.gui.handler.eyephone;

import me.fabric.eyephonemod.gui.handler.PacketAction;

public enum EyePhonePacketAction implements PacketAction {
    PHONE_NAME_UPDATE,
    /* for server to send all item stack info */
    PHONE_ENTRIES_UPDATE,

    /* for client to send individual item stack info */
    PHONE_ENTRY_UPDATE
}
