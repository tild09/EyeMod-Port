package me.fabric.eyephonemod.gui.handler.eyephone;

import me.fabric.eyephonemod.gui.ScreenRegistry;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import net.minecraft.network.PacketByteBuf;

public class EyePhoneClientScreenHandler extends ClientScreenHandler {
    public EyePhoneClientScreenHandler(int syncId) {
        super(ScreenRegistry.EYEPHONE_GUI.getScreenHandlerType(), syncId);
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
    }

    public void onClick(double mouseX, double mouseY) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.MOUSE_CLICK.getActionOrdinal());
        packetByteBuf.writeDouble(mouseX);
        packetByteBuf.writeDouble(mouseY);
        ScreenPacket.sendToServer(packetByteBuf);
    }

}
