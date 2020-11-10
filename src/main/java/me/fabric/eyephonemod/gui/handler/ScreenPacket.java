package me.fabric.eyephonemod.gui.handler;

import io.netty.buffer.Unpooled;
import me.fabric.eyephonemod.gui.client.EyePhoneScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScreenPacket {
    private static final Identifier S2CPacket = new Identifier("eyephone", "screenpackets2c");
    private static final Identifier C2SPacket = new Identifier("eyephone", "screenpacketc2s");
    private static final Logger LOGGER = LogManager.getLogger();

    @NotNull
    public static PacketByteBuf newPacket(int syncId, int action) {
        final PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeVarInt(syncId);
        packetByteBuf.writeVarInt(action);
        return packetByteBuf;
    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(@NotNull PacketByteBuf buffer) {
        ClientSidePacketRegistryImpl.INSTANCE.sendToServer(C2SPacket, buffer);
    }

    public static void sendToClient(@NotNull PacketByteBuf buffer, @NotNull ServerPlayerEntity target) {
        ServerSidePacketRegistryImpl.INSTANCE.sendToPlayer(target, S2CPacket, buffer);
    }

    @Environment(EnvType.CLIENT)
    public static int getClientSyncId() {
        final Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if (currentScreen == null) return -1;
        if (!(currentScreen instanceof HandledScreen)) return -1;
        return ((HandledScreen<?>) currentScreen).getScreenHandler().syncId;
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static EyePhoneScreen<? extends ClientScreenHandler> getClientScreen() {
        final Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if (currentScreen == null) return null;
        if (!(currentScreen instanceof EyePhoneScreen)) return null;
        return ((EyePhoneScreen<? extends ClientScreenHandler>) currentScreen);
    }

    public static int getServerSyncId(@NotNull ServerPlayerEntity player) {
        return player.currentScreenHandler.syncId;
    }

    public static void initClientScreen() {
        ClientSidePacketRegistryImpl.INSTANCE.register(S2CPacket, ((packetContext, packetByteBuf) -> {
            final int syncId = packetByteBuf.readVarInt();
            if (getClientSyncId() != syncId) {
                LOGGER.error("Expected sync id of {} but got {} instead! Closing screen...", getClientSyncId(), syncId);
                packetContext.getTaskQueue().execute(() -> MinecraftClient.getInstance().openScreen(null));
                return;
            }
            final EyePhoneScreen<? extends ClientScreenHandler> clientScreen = getClientScreen();

            if (clientScreen == null) {
                LOGGER.error("Client screen is not an EyePhone screen! Closing screen...");
                packetContext.getTaskQueue().execute(() -> MinecraftClient.getInstance().openScreen(null));
                return;
            }
            clientScreen.getEyePhoneScreenHandler().onPacket(packetByteBuf, packetByteBuf.readVarInt());
        }));
    }

    public static void initServerScreen() {
        ServerSidePacketRegistryImpl.INSTANCE.register(C2SPacket, ((packetContext, packetByteBuf) -> {
            final PlayerEntity player = packetContext.getPlayer();
            final ScreenHandler currentScreenHandler = player.currentScreenHandler;

            if (currentScreenHandler == null) {
                LOGGER.error("Client player has no opened screen!");
                return;
            }

            if (!(currentScreenHandler instanceof ServerScreenHandler)) {
                LOGGER.error("Client player is not opening an EyePhone screen!");
                return;
            }

            final int syncId = packetByteBuf.readVarInt();
            final int serverSyncId = getServerSyncId((ServerPlayerEntity) packetContext.getPlayer());

            if (syncId != serverSyncId) {
                LOGGER.error("Expected sync id of {} but got {} instead!", serverSyncId, syncId);
                return;
            }

            ((ServerScreenHandler) currentScreenHandler).onPacket(packetByteBuf, packetByteBuf.readVarInt());
        }));
    }
}
