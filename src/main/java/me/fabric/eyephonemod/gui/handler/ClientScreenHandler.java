package me.fabric.eyephonemod.gui.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public abstract class ClientScreenHandler extends ScreenHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    protected ClientScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    public void confirmScreenOpenToServer() {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId,
                PacketAction.DefaultPacketAction.INIT.getActionOrdinal());
        LOGGER.info("C2S: INIT {}", syncId);
        ScreenPacket.sendToServer(packetByteBuf);
    }

    public abstract void onPacket(PacketByteBuf packetByteBuf, int packetAction);


    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


}
