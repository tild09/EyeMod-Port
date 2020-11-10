package me.fabric.eyephonemod.item;

import me.fabric.eyephonemod.gui.handler.ItemStackScreenHandlerFactory;
import me.fabric.eyephonemod.mixin.ServerPlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public abstract class ScreenHandlingItem extends Item {
    public ScreenHandlingItem(Settings settings) {
        super(settings);
    }

    @Override
    public final boolean postProcessTag(@NotNull CompoundTag tag) {
        final boolean b = super.postProcessTag(tag);
        final CompoundTag tag1 = deserializeStack(tag.getCompound("tag"));
        tag.put("tag", tag1);
        return b;
    }

    public abstract CompoundTag deserializeStack(@NotNull CompoundTag compoundTag);

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        final ItemStack stackInHand = user.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.success(stackInHand, true);
        return interact(world, (ServerPlayerEntity) user, hand, stackInHand) ? super.use(world, user, hand) : TypedActionResult.fail(stackInHand);
    }

    public abstract void serializeStack(@NotNull ItemStack itemStack, @NotNull CompoundTag compoundTag);

    public abstract boolean interact(World world, ServerPlayerEntity user, Hand hand, ItemStack itemStack);

    public final OptionalInt openItemStackScreenHandler(@Nullable ItemStackScreenHandlerFactory factory, ServerPlayerEntity player, ItemStack itemStack) {
        if (factory == null) {
            return OptionalInt.empty();
        } else {
            if (player.currentScreenHandler != player.playerScreenHandler) {
                player.closeHandledScreen();
            }

            ((ServerPlayerEntityAccessor)player).eyephone_incrementScreenHandlerSyncId();
            ScreenHandler screenHandler = factory.createMenu(((ServerPlayerEntityAccessor)player).eyephone_screenHandlerSyncId(), player, itemStack);
            if (screenHandler == null) {
                if (player.isSpectator()) {
                    player.sendMessage((new TranslatableText("container.spectatorCantOpen")).formatted(Formatting.RED), true);
                }

                return OptionalInt.empty();
            } else {
                player.networkHandler.sendPacket(new OpenScreenS2CPacket(screenHandler.syncId, screenHandler.getType(), factory.getDisplayName()));
                screenHandler.addListener(player);
                player.currentScreenHandler = screenHandler;
                return OptionalInt.of(((ServerPlayerEntityAccessor)player).eyephone_screenHandlerSyncId());
            }
        }
    }
}
