package me.fabric.eyephonemod.gui.client.util;

import me.fabric.eyephonemod.gui.client.EyePhoneScreen;
import me.fabric.eyephonemod.gui.client.EyePhoneScreen.Apps;
import me.fabric.eyephonemod.gui.client.element.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.function.Consumer;

public class PanelMaker {

    public static final int PADDING = 20;
    public static final int SIZE = 256;
    public static final int BG_WIDTH = 88 * 2;
    public static final int BG_HEIGHT = 118 * 2;

    public static BottomRightAnchoredPanel newPanel(boolean isVisible) {
        final BottomRightAnchoredPanel bottomRightAnchoredPanel = new BottomRightAnchoredPanel(BG_WIDTH, BG_HEIGHT, PADDING, PADDING);
        bottomRightAnchoredPanel.setVisible(isVisible);
        return bottomRightAnchoredPanel;
    }

    public static void configureSettingsPanel(EyePhoneScreen<?> screen, BottomRightAnchoredPanel settingsPanel) {
        settingsPanel.addChild(new Label("Phone Name", 30, 20));

        final TextField textField = newListeningTextField(32, screen.handler::updatePhoneName);
        screen.handler.setPhoneNameUpdateListener(textField::rewrite);
        settingsPanel.addChild(textField);

        settingsPanel.addChild(new Label("Password", 30, 60));
        final TextField passwordField = newListeningTextField(72, screen.handler::updatePassword);
        screen.handler.appendPhonePasswordUpdateListener(passwordField::rewrite);
        settingsPanel.addChild(passwordField);

        settingsPanel.addChild(new LabelledButton(30,
                BG_HEIGHT - 70,
                BG_WIDTH - 60,
                new LiteralText("Return"),
                button -> screen.requestChangePanel(null)));
    }

    private static TextField newListeningTextField(int y, Consumer<String> listener) {
        final TextField textField = new TextField(BG_WIDTH - 60, listener, 30, y);
        textField.setMaxLength(20);
        return textField;
    }

    public static void configureAppsPanel(EyePhoneScreen<?> screen, BottomRightAnchoredPanel appsPanel, List<Apps> availableApps) {
        final int cols = 4;
        final int paddingX = 5;
        final int paddingY = 4;
        final int marginX = 18;
        final int marginY = 20;

        int i = 0;
        for (Apps availableApp : availableApps) {
            final int x = Math.floorMod(i, cols) * Apps.BTN_SIZE + paddingX + marginX;
            final int y = Math.floorDiv(i, cols) * Apps.BTN_SIZE + paddingY + marginY;
            appsPanel.addChild(newListeningButton(availableApp.texture, x, y, () -> screen.requestChangePanel(availableApp)));
            i++;
        }
    }

    private static TexturedButton newListeningButton(TextureSetting texture, int x, int y, Runnable onClick) {
        final TexturedButton texturedButton = new TexturedButton(texture, Apps.BTN_SIZE, Apps.BTN_SIZE, x, y);
        texturedButton.setOnClickCallback(onClick);
        return texturedButton;
    }

    public static void configurePasswordPanel(EyePhoneScreen<?> screen, BottomRightAnchoredPanel passwordPanel) {
        passwordPanel.addChild(new Label("Password", 30, 60));

        final TextField passwordField = newListeningTextField(72, screen.handler::updatePasswordToUnlock);
        passwordPanel.addChild(passwordField);

        passwordPanel.addChild(new LabelledButton(30,
                90,
                BG_WIDTH - 60,
                new LiteralText("Unlock"),
                button -> screen.handler.submitUnlockRequest()));


        final String wrongPasswordMsg = "Wrong Password!";
        final int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(wrongPasswordMsg);
        final Label label = new Label(wrongPasswordMsg, (BG_WIDTH - textWidth) / 2, 120);
        label.setColor(0xFFFF5500);
        label.setVisible(false);
        passwordPanel.addChild(label);

        screen.handler.setOnPasswordFailure(() -> label.setVisible(true));
    }
}
