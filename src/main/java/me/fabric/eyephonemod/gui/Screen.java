package me.fabric.eyephonemod.gui;

import me.fabric.eyephonemod.gui.client.DummyScreen;
import me.fabric.eyephonemod.gui.client.EyePhoneScreen;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.dummy.DummyClientScreenHandler;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public enum Screen {
    DUMMY_GUI(DummyScreen::new, DummyClientScreenHandler::new);

    private ScreenHandlerType<? extends ClientScreenHandler> screenHandlerType = null;
    private final ScreenFactory screenFactory;
    private final ClientScreenHandlerFactory<? extends ClientScreenHandler> clientScreenHandlerFactory;
    public final String path;

    Screen(ScreenFactory screenFactory, ClientScreenHandlerFactory<? extends ClientScreenHandler> clientScreenHandlerFactory) {
        this.screenFactory = screenFactory;
        this.clientScreenHandlerFactory = clientScreenHandlerFactory;
        this.path = this.name().toLowerCase();
    }

    public ScreenHandlerType<? extends ClientScreenHandler> getScreenHandlerType() {
        if (screenHandlerType == null) throw new NullPointerException("Register screen handlers first!");
        return screenHandlerType;
    }

    public static void registerScreenHandlers(String namespace) {
        for (Screen value : values()) {
            value.screenHandlerType = ScreenHandlerRegistry.registerSimple(new Identifier(namespace, value.path), ((syncId, inventory) -> value.clientScreenHandlerFactory.createNewScreenHandler(syncId)));
        }
    }

    public static void registerClientScreens(String namespace) {
        for (Screen value : values()) {
            ScreenRegistry.register(value.getScreenHandlerType(), value.screenFactory::createNewScreen);
        }
    }
}
