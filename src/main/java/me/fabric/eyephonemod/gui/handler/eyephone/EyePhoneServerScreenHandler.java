package me.fabric.eyephonemod.gui.handler.eyephone;

import me.fabric.eyephonemod.gui.ScreenRegistry;
import me.fabric.eyephonemod.gui.handler.ServerScreenHandler;
import me.fabric.eyephonemod.item.ScreenHandlingItem;
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

    public EyePhoneServerScreenHandler(int syncId, ServerPlayerEntity player, ItemStack itemStack) {
        super(ScreenRegistry.EYEPHONE_GUI.getScreenHandlerType(), syncId);
        if (!(itemStack.getItem() instanceof ScreenHandlingItem))
            throw new RuntimeException("ItemStack is not an item of TaggedItem!");
        phone = itemStack;
        playerEntity = player;
        if (phone.getTag() != null) {
            final CompoundTag tag = phone.getTag();
            if (tag.contains("mouseX") && tag.contains("mouseY")) {
                LOGGER.info("Item has already a tag of mouseX[{}] mouseY[{}]", tag.getDouble("mouseX"), tag.getDouble("mouseY"));
            }
        }
    }

    @Override
    public void onPacket(PacketByteBuf packetByteBuf, int packetAction) {
        if (packetAction == EyePhonePacketAction.MOUSE_CLICK.getActionOrdinal()) {
            final double mouseX = packetByteBuf.readDouble();
            final double mouseY = packetByteBuf.readDouble();
            LOGGER.info("Got client mouse action of {} {}", mouseX, mouseY);
            final CompoundTag tag = phone.getOrCreateTag();
            tag.putDouble("mouseX", mouseX);
            tag.putDouble("mouseY", mouseY);
            phone.setTag(tag);
        }
    }
}
