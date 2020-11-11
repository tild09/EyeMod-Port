package me.fabric.eyephonemod.gui.handler.eyephone;

import me.fabric.eyephonemod.gui.ScreenRegistry;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class EyePhoneClientScreenHandler extends ClientScreenHandler {

    private Consumer<String> onServerSendPhoneEntryUpdate = (s) -> {};
    private static final Logger LOGGER = LogManager.getLogger();

    public EyePhoneClientScreenHandler(int syncId) {
        super(ScreenRegistry.EYEPHONE_GUI.getScreenHandlerType(), syncId);
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
        if (packetAction == EyePhonePacketAction.PHONE_NAME_UPDATE.getActionOrdinal()) {
            onServerSendPhoneEntryUpdate.accept(packetByteBuf.readString());
        }
    }

    public void updatePhoneName(String name) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.PHONE_NAME_UPDATE.getActionOrdinal());
        packetByteBuf.writeString(name);
        LOGGER.info("C2S: PHONE_NAME_UPDATE {}", syncId);
        ScreenPacket.sendToServer(packetByteBuf);
    }

    public void setServerUpdateListener(Consumer<String> listener) {
        onServerSendPhoneEntryUpdate = listener;
    }
}
