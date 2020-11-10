package me.fabric.eyephonemod;

import me.fabric.eyephonemod.gui.handler.ScreenPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

class EyePhoneMod implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenPacket.initClientScreen();
    }

    @Override
    public void onInitialize() {
        ScreenPacket.initServerScreen();
    }
}