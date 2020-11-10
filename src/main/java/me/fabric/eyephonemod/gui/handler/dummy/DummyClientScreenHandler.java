package me.fabric.eyephonemod.gui.handler.dummy;

import me.fabric.eyephonemod.gui.Screen;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import net.minecraft.network.PacketByteBuf;

public class DummyClientScreenHandler extends ClientScreenHandler {
    public DummyClientScreenHandler(int syncId) {
        super(Screen.DUMMY_GUI.getScreenHandlerType(), syncId);
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
    }

    public void onClick(double mouseX, double mouseY) {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, DummyPacketAction.MOUSE_CLICK.getActionOrdinal());
        packetByteBuf.writeDouble(mouseX);
        packetByteBuf.writeDouble(mouseY);
        ScreenPacket.sendToServer(packetByteBuf);
    }

}
