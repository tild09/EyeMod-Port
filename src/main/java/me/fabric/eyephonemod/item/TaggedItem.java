package me.fabric.eyephonemod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class TaggedItem extends Item {
    public TaggedItem(Settings settings) {
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
}
