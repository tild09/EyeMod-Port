package me.fabric.eyephonemod.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.impl.item.group.CreativeGuiExtensions;
import net.fabricmc.fabric.impl.item.group.FabricCreativeGuiComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;
import java.util.function.Supplier;

public enum ItemRegistry {
    // phones variation
    RED_PHONE(DummyItem::new),
    WHITE_PHONE(DummyItem::new),
    BLACK_PHONE(DummyItem::new),
    BLUE_PHONE(DummyItem::new);

    public final String path;
    private volatile Item item = null;
    private final Function<Item.Settings, Item> itemSupplier;
    private static volatile ItemGroup EYE_PHONE_GROUP = null;
    ItemRegistry(Function<Item.Settings, Item> itemSupplier) {
        path = name().toLowerCase();
        this.itemSupplier = itemSupplier;
    }

    public static void registerItems(String namespace) {
        EYE_PHONE_GROUP = FabricItemGroupBuilder.build(new Identifier(namespace, "eyephone_group"), () -> new ItemStack(values()[0].item));

        for (ItemRegistry value : values()) {
            value.item = value.itemSupplier.apply(new Item.Settings().group(EYE_PHONE_GROUP));
            Registry.register(Registry.ITEM, new Identifier(namespace, value.path), value.item);
        }
    }

    public static ItemGroup getItemGroup() {
        if (EYE_PHONE_GROUP == null) throw new NullPointerException("Register items first! The item group is only available after the items are registered.");
        return EYE_PHONE_GROUP;
    }

    public Item getItem() {
        if (item == null) throw new NullPointerException("Register items first!");
        return item;
    }
}
