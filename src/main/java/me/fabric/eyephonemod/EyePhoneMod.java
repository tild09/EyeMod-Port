package me.fabric.eyephonemod;

import me.fabric.eyephonemod.block.BlockRegistry;
import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import me.fabric.eyephonemod.item.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class EyePhoneMod implements ModInitializer, ClientModInitializer {
    public static final String NAMESPACE = "eyephone";
    @Override
    public void onInitializeClient() {
        ScreenPacket.initClientScreen();
    }

    @Override
    public void onInitialize() {
        ScreenPacket.initServerScreen();
        ItemRegistry.registerItems(NAMESPACE);
        BlockRegistry.registerBlocks(NAMESPACE);
    }
}