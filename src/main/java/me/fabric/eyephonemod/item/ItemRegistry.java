package me.fabric.eyephonemod.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public enum ItemRegistry {
    // phones variation
    RED_PHONE(EyeModItem::new),
    WHITE_PHONE(EyeModItem::new),
    BLACK_PHONE(EyeModItem::new),
    BLUE_PHONE(EyeModItem::new);

    public final String path;
    public final Item item;
    ItemRegistry(Supplier<Item> itemSupplier) {
        path = name().toLowerCase();
        item = itemSupplier.get();
    }

    public static void registerItems(String namespace) {
        for (ItemRegistry value : values()) {
            Registry.register(Registry.ITEM, new Identifier(namespace, value.path), value.item);
        }
    }
}
