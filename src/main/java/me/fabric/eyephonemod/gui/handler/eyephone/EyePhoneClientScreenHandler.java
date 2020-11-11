package me.fabric.eyephonemod.gui.handler.eyephone;

import me.fabric.eyephonemod.gui.ScreenRegistry;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;

public class EyePhoneClientScreenHandler extends ClientScreenHandler {

    private BiConsumer<String, String> phoneUpdateListener = (s, t) -> {
    };
    private static final Logger LOGGER = LogManager.getLogger();

    public EyePhoneClientScreenHandler(int syncId) {
        super(ScreenRegistry.EYEPHONE_GUI.getScreenHandlerType(), syncId);
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
        if (packetAction == EyePhonePacketAction.PHONE_ENTRIES_UPDATE.getActionOrdinal()) {
            phoneUpdateListener.accept(packetByteBuf.readString(), packetByteBuf.readString());
        }
    }

    public void updatePhoneName(String name) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.PHONE_ENTRY_UPDATE.getActionOrdinal());
        packetByteBuf.writeString(EyePhoneContext.Attr.NAME.camelCase);
        packetByteBuf.writeString(name);
        ScreenPacket.sendToServer(packetByteBuf);
    }

    public void setPhoneUpdateListener(BiConsumer<String, String> listener) {
        phoneUpdateListener = listener;
    }
}
