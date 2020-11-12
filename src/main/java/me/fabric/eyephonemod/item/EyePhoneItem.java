package me.fabric.eyephonemod.item;

import me.fabric.eyephonemod.gui.handler.ItemStackScreenHandlerFactory;
import me.fabric.eyephonemod.gui.handler.eyephone.EyePhoneServerScreenHandler;
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

public class EyePhoneItem extends ScreenHandlingItem {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ItemStackScreenHandlerFactory SCREEN_HANDLER_FACTORY = new ItemStackScreenHandlerFactory() {
        @Override
        public Text getDisplayName() {
            return new TranslatableText("gui.eyephone.dummygui");
        }

        @Override
        public @NotNull ScreenHandler createMenu(int syncId, ServerPlayerEntity player, ItemStack itemStack) {
            return new EyePhoneServerScreenHandler(syncId, player, itemStack);
        }
    };
    private ItemRegistry itemRegistry;

    public EyePhoneItem(Settings settings) {
        super(settings);
    }


    @Override
    public CompoundTag deserializeStack(@NotNull CompoundTag compoundTag) {
        return compoundTag;
    }

    @Override
    public void serializeStack(@NotNull ItemStack itemStack, @NotNull CompoundTag compoundTag) {

    }

    @Override
    public boolean interact(World world, ServerPlayerEntity user, Hand hand, ItemStack itemStack) {
        final OptionalInt optionalInt = openItemStackScreenHandler(SCREEN_HANDLER_FACTORY, user, itemStack);
        if (!optionalInt.isPresent()) LOGGER.error("Cannot open handled screen for player {}!", user.getEntityName());
        return true;
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }
}
