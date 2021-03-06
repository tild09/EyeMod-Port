package me.fabric.eyephonemod.item;

import me.fabric.eyephonemod.gui.handler.ItemStackScreenHandlerFactory;
import me.fabric.eyephonemod.gui.handler.dummy.DummyServerScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

public final class DummyItem extends ScreenHandlingItem {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ItemStackScreenHandlerFactory SCREEN_HANDLER_FACTORY = new ItemStackScreenHandlerFactory() {
        @Override
        public Text getDisplayName() {
            return new TranslatableText("gui.eyephone.dummygui");
        }

        @Override
        public @NotNull ScreenHandler createMenu(int syncId, ServerPlayerEntity player, ItemStack itemStack) {
            return new DummyServerScreenHandler(syncId, player, itemStack);
        }
    };

    public DummyItem(Settings settings) {
        super(settings);
    }

    @Override
    public CompoundTag deserializeStack(@NotNull CompoundTag compoundTag) {
        LOGGER.info("Deserializing dummy item");
        if (!compoundTag.contains("isDummy"))
            LOGGER.info("Item has not yet been tagged. Try saving and reloading the world.");
        else LOGGER.info("Item has the tag 'isDummy': {}", compoundTag.getString("isDummy"));
        return compoundTag;
    }

    @Override
    public void serializeStack(@NotNull ItemStack itemStack, @NotNull CompoundTag compoundTag) {
        LOGGER.info("Serializing dummy item");
        compoundTag.putString("isDummy", "yes is it dummy");
    }

    @Override
    public boolean interact(World world, ServerPlayerEntity user, Hand hand, ItemStack itemStack) {
        final OptionalInt optionalInt = openItemStackScreenHandler(SCREEN_HANDLER_FACTORY, user, itemStack);
        if (!optionalInt.isPresent()) LOGGER.error("Cannot open handled screen for player {}!", user.getEntityName());
        return true;
    }
}
