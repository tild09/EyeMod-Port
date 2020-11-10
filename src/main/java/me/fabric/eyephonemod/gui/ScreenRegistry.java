package me.fabric.eyephonemod.gui;

import me.fabric.eyephonemod.gui.client.DummyScreen;
import me.fabric.eyephonemod.gui.client.EyePhoneScreen;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.PacketAction;
import me.fabric.eyephonemod.gui.handler.dummy.DummyClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.dummy.DummyPacketAction;
import me.fabric.eyephonemod.gui.handler.eyephone.EyePhoneClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.eyephone.EyePhonePacketAction;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public enum ScreenRegistry {
    DUMMY_GUI(DummyScreen::new, DummyClientScreenHandler::new, DummyPacketAction::values),
    EYEPHONE_GUI(EyePhoneScreen::new, EyePhoneClientScreenHandler::new, EyePhonePacketAction::values);

    private ScreenHandlerType<? extends ClientScreenHandler> screenHandlerType = null;
    private final ScreenFactory screenFactory;
    private final ClientScreenHandlerFactory<? extends ClientScreenHandler> clientScreenHandlerFactory;
    private final PacketActionsProvider packetActionsProvider;
    public final String path;

    ScreenRegistry(ScreenFactory screenFactory, ClientScreenHandlerFactory<? extends ClientScreenHandler> clientScreenHandlerFactory, PacketActionsProvider packetActionsProvider) {
        this.screenFactory = screenFactory;
        this.clientScreenHandlerFactory = clientScreenHandlerFactory;
        this.path = this.name().toLowerCase();
        this.packetActionsProvider = packetActionsProvider;
    }

    public ScreenHandlerType<? extends ClientScreenHandler> getScreenHandlerType() {
        if (screenHandlerType == null) throw new NullPointerException("Register screen handlers first!");
        return screenHandlerType;
    }

    public static void registerScreenHandlers(String namespace) {
        PacketAction.PACKET_ACTIONS.addAll(Arrays.asList(PacketAction.DefaultPacketAction.values()));
        for (ScreenRegistry value : values()) {
            value.screenHandlerType = ScreenHandlerRegistry.registerSimple(new Identifier(namespace, value.path), ((syncId, inventory) -> value.clientScreenHandlerFactory.createNewScreenHandler(syncId)));
            PacketAction.PACKET_ACTIONS.addAll(Arrays.asList(value.packetActionsProvider.get()));
        }
    }

    public static void registerClientScreens() {
        for (ScreenRegistry value : values()) {
            net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.register(value.getScreenHandlerType(), value.screenFactory::createNewScreen);
        }
    }
}
