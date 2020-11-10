package me.fabric.eyephonemod.gui.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public abstract class ClientScreenHandler extends ScreenHandler {
    protected ClientScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
        confirmScreenOpenToServer();
    }

    private void confirmScreenOpenToServer() {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId);
        packetByteBuf.writeVarInt(ScreenPacket.PacketActions.INIT.ordinal());
        ScreenPacket.sendToServer(packetByteBuf);
    }

    public abstract void onPacket(PacketByteBuf packetByteBuf, int packetAction);


    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
