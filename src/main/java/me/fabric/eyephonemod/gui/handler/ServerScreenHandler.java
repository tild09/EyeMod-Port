package me.fabric.eyephonemod.gui.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public abstract class ServerScreenHandler extends ScreenHandler {
    protected ServerScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public abstract void onPacket(PacketByteBuf packetByteBuf, int packetAction);
}
