package me.fabric.eyephonemod.gui.handler.eyephone;

import me.fabric.eyephonemod.gui.ScreenRegistry;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import me.fabric.eyephonemod.item.ItemRegistry;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class EyePhoneClientScreenHandler extends ClientScreenHandler {

    private Consumer<String> phoneNameUpdateListener = (s) -> {
    };

    private Consumer<String> phoneBgUpdateListener = (s) -> {
    };

    private Consumer<String> phoneTypeUpdateListener = (s) -> {
    };

    private static final Logger LOGGER = LogManager.getLogger();

    public EyePhoneClientScreenHandler(int syncId) {
        super(ScreenRegistry.EYEPHONE_GUI.getScreenHandlerType(), syncId);
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
        if (packetAction == EyePhonePacketAction.PHONE_ENTRIES_UPDATE.getActionOrdinal()) {
            System.out.println("Got entries packet");
            phoneNameUpdateListener.accept(packetByteBuf.readString());
            phoneBgUpdateListener.accept(packetByteBuf.readString());
            phoneTypeUpdateListener.accept(packetByteBuf.readEnumConstant(ItemRegistry.class).path);
        }
    }

    public void updatePhoneName(String name) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.PHONE_ENTRY_UPDATE.getActionOrdinal());
        packetByteBuf.writeString(EyePhoneContext.Attr.NAME.camelCase);
        packetByteBuf.writeString(name);
        ScreenPacket.sendToServer(packetByteBuf);
    }

    public void setPhoneNameUpdateListener(Consumer<String> listener) {
        phoneNameUpdateListener = listener;
    }

    public void setPhoneBgUpdateListener(Consumer<String> phoneBgUpdateListener) {
        this.phoneBgUpdateListener = phoneBgUpdateListener;
    }

    public void setPhoneTypeUpdateListener(Consumer<String> phoneTypeUpdateListener) {
        this.phoneTypeUpdateListener = phoneTypeUpdateListener;
    }
}
