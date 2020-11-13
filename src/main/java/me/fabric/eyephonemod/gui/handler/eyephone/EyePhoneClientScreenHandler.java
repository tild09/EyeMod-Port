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

    private Consumer<String> phoneNameUpdateListener = EyePhoneClientScreenHandler::dummy;
    private Consumer<String> phoneBgUpdateListener = EyePhoneClientScreenHandler::dummy;
    private Consumer<String> phoneTypeUpdateListener = EyePhoneClientScreenHandler::dummy;
    private Consumer<String> phonePasswordUpdateListener = EyePhoneClientScreenHandler::dummy;
    private Runnable onPasswordSuccess = EyePhoneClientScreenHandler::dummy;
    private Runnable onPasswordFailure = EyePhoneClientScreenHandler::dummy;
    private String contextPassword = "";

    private static final Logger LOGGER = LogManager.getLogger();

    public EyePhoneClientScreenHandler(int syncId) {
        super(ScreenRegistry.EYEPHONE_GUI.getScreenHandlerType(), syncId);
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
        if (packetAction == EyePhonePacketAction.PHONE_ENTRIES_UPDATE.getActionOrdinal()) {
            phoneNameUpdateListener.accept(packetByteBuf.readString());
            phoneBgUpdateListener.accept(packetByteBuf.readString());
            phoneTypeUpdateListener.accept(packetByteBuf.readEnumConstant(ItemRegistry.class).path);
            phonePasswordUpdateListener.accept(packetByteBuf.readString());
        } else if (packetAction == EyePhonePacketAction.PHONE_VERIFY_PASSWORD_FAIL.getActionOrdinal()) {
            onPasswordFailure.run();
        } else if (packetAction == EyePhonePacketAction.PHONE_VERIFY_PASSWORD_SUCCESS.getActionOrdinal()) {
            onPasswordSuccess.run();
        }
    }

    public void updatePhoneName(String name) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.PHONE_ENTRY_UPDATE.getActionOrdinal());
        packetByteBuf.writeEnumConstant(EyePhoneContext.Attr.NAME);
        packetByteBuf.writeString(name);
        ScreenPacket.sendToServer(packetByteBuf);
    }

    public void updatePassword(String password) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.PHONE_ENTRY_UPDATE.getActionOrdinal());
        packetByteBuf.writeEnumConstant(EyePhoneContext.Attr.PASSWORD);
        packetByteBuf.writeString(password);
        ScreenPacket.sendToServer(packetByteBuf);
    }

    private void authenticatePassword(String password) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.PHONE_VERIFY_PASSWORD.getActionOrdinal());
        packetByteBuf.writeString(password);
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

    public void appendPhonePasswordUpdateListener(Consumer<String> phonePasswordUpdateListener) {
        final Consumer<String> previousListener = this.phonePasswordUpdateListener;
        this.phonePasswordUpdateListener = (s) -> {
            previousListener.accept(s);
            phonePasswordUpdateListener.accept(s);
        };
    }

    public void setOnPasswordSuccess(Runnable onPasswordSuccess) {
        this.onPasswordSuccess = onPasswordSuccess;
    }

    public void setOnPasswordFailure(Runnable onPasswordFailure) {
        this.onPasswordFailure = onPasswordFailure;
    }

    private static void dummy(String s) {
    }

    private static void dummy() {
    }

    public void updatePasswordToUnlock(String password) {
        contextPassword = password;
    }

    public void submitUnlockRequest() {
        authenticatePassword(contextPassword);
    }
}
