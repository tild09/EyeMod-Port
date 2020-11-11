package me.fabric.eyephonemod.gui.handler.eyephone;

import me.fabric.eyephonemod.gui.ScreenRegistry;
import me.fabric.eyephonemod.gui.handler.PacketAction;
import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import me.fabric.eyephonemod.gui.handler.ServerScreenHandler;
import me.fabric.eyephonemod.item.ScreenHandlingItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EyePhoneServerScreenHandler extends ServerScreenHandler {

    public static final Logger LOGGER = LogManager.getLogger();
    final ItemStack phone;
    final ServerPlayerEntity playerEntity;
    final EyePhoneContext eyePhoneContext;

    public EyePhoneServerScreenHandler(int syncId, ServerPlayerEntity player, ItemStack itemStack) {
        super(ScreenRegistry.EYEPHONE_GUI.getScreenHandlerType(), syncId);
        if (!(itemStack.getItem() instanceof ScreenHandlingItem))
            throw new RuntimeException("ItemStack is not an item of TaggedItem!");
        phone = itemStack;
        playerEntity = player;
        eyePhoneContext = new EyePhoneContext(phone.getOrCreateTag());
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
        if (packetAction == EyePhonePacketAction.PHONE_NAME_UPDATE.getActionOrdinal()) {
            updatePhoneName(packetByteBuf.readString());
        } else if (packetAction == PacketAction.DefaultPacketAction.INIT.getActionOrdinal()) {
            sendEntryUpdates();
        } else if (packetAction == EyePhonePacketAction.PHONE_ENTRY_UPDATE.getActionOrdinal()) {
            final String key = packetByteBuf.readString();
            final String value = packetByteBuf.readString();
            eyePhoneContext.updateTag(key, value);
        }
    }

    private void sendEntryUpdates() {
        final PacketByteBuf packetByteBuf = ScreenPacket.newPacket(syncId, EyePhonePacketAction.PHONE_ENTRIES_UPDATE.getActionOrdinal());
        packetByteBuf.writeString(eyePhoneContext.name);
        packetByteBuf.writeString(eyePhoneContext.backgroundIdentifier);
        ScreenPacket.sendToClient(packetByteBuf, playerEntity);
    }

    private void updatePhoneName(String name) {
        final CompoundTag phoneInfo = phone.getOrCreateSubTag("phoneInfo");
        phoneInfo.putString("name", name);
        phone.putSubTag("phoneInfo", phoneInfo);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        LOGGER.info("Closing server screen handler {}", syncId);
        eyePhoneContext.applyChanges(phone);
    }
}
