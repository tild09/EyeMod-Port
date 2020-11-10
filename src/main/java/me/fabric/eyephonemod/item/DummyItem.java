package me.fabric.eyephonemod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public final class DummyItem extends TaggedItem {
    private static final Logger LOGGER = LogManager.getLogger();

    public DummyItem(Settings settings) {
        super(settings);
    }

    @Override
    public CompoundTag deserializeStack(@NotNull CompoundTag compoundTag) {
        LOGGER.info("Deserializing dummy item");
        if (!compoundTag.contains("isDummy")) LOGGER.info("Item has not yet been tagged. Try saving and reloading the world.");
        else LOGGER.info("Item has the tag 'isDummy': {}", compoundTag.getString("isDummy"));
        return compoundTag;
    }

    @Override
    public void serializeStack(@NotNull ItemStack itemStack, @NotNull CompoundTag compoundTag) {
        LOGGER.info("Serializing dummy item");
        compoundTag.putString("isDummy", "yes is it dummy");
    }

    @Override
    public boolean interact(World world, PlayerEntity user, Hand hand, ItemStack itemStack) {
        return true;
    }
}
