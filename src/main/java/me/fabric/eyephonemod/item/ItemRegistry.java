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
    // deprecated
    BLACK_COVER(DummyItem::new),
    COVER(DummyItem::new),
    RED_DISPLAY(DummyItem::new),
    EYEPHONE(EyePhoneItem::new),
    EYEPHONE_OP(DummyItem::new),
    OP_CHIP(DummyItem::new),

    // phones components
    DISPLAY(DummyItem::new),
    BATTERY(DummyItem::new),
    ENGINE(DummyItem::new),
    WIRE(DummyItem::new),

    // cases
    CASE_BLACK(DummyItem::new),
    CASE_BLUE(DummyItem::new),
    CASE_BROWN(DummyItem::new),
    CASE_CYAN(DummyItem::new),
    CASE_GRAY(DummyItem::new),
    CASE_GREEN(DummyItem::new),
    CASE_LIGHT_BLUE(DummyItem::new),
    CASE_LIGHT_GRAY(DummyItem::new),
    CASE_LIME(DummyItem::new),
    CASE_MAGENTA(DummyItem::new),
    CASE_ORANGE(DummyItem::new),
    CASE_PINK(DummyItem::new),
    CASE_PURPLE(DummyItem::new),
    CASE_RED(DummyItem::new),
    CASE_WHITE(DummyItem::new),
    CASE_YELLOW(DummyItem::new),

    // phones
    PHONE_BLACK(EyePhoneItem::new),
    PHONE_BLUE(EyePhoneItem::new),
    PHONE_BROWN(EyePhoneItem::new),
    PHONE_CYAN(EyePhoneItem::new),
    PHONE_GRAY(EyePhoneItem::new),
    PHONE_GREEN(EyePhoneItem::new),
    PHONE_LIGHT_BLUE(EyePhoneItem::new),
    PHONE_LIGHT_GRAY(EyePhoneItem::new),
    PHONE_LIME(EyePhoneItem::new),
    PHONE_MAGENTA(EyePhoneItem::new),
    PHONE_ORANGE(EyePhoneItem::new),
    PHONE_PINK(EyePhoneItem::new),
    PHONE_PURPLE(EyePhoneItem::new),
    PHONE_RED(EyePhoneItem::new),
    PHONE_WHITE(EyePhoneItem::new),
    PHONE_YELLOW(EyePhoneItem::new);

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
