package me.fabric.eyephonemod.block;

import me.fabric.eyephonemod.item.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public enum BlockRegistry {
    CHARGER_SIDE(DummyBlock::new),
    SERVER_SIDE(DummyBlock::new);

    public final String path;
    public final Block block;
    private volatile BlockItem item = null;

    BlockRegistry(Supplier<Block> blockSupplier) {
        path = name().toLowerCase();
        block = blockSupplier.get();
    }

    public static void registerBlocks(String namespace) {
        for (BlockRegistry value : values()) {
            value.item = new BlockItem(value.block, new Item.Settings().group(ItemRegistry.getItemGroup()));
            Registry.register(Registry.BLOCK, new Identifier(namespace, value.path), value.block);
            Registry.register(Registry.ITEM, new Identifier(namespace, value.path), value.item);
        }
    }

    public BlockItem getItem() {
        return item;
    }
}
