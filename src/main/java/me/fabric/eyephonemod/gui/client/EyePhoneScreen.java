package me.fabric.eyephonemod.gui.client;

import me.fabric.eyephonemod.EyePhoneMod;
import me.fabric.eyephonemod.gui.client.element.CenteredPanel;
import me.fabric.eyephonemod.gui.client.element.DrawableElement;
import me.fabric.eyephonemod.gui.client.element.TextField;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.eyephone.EyePhoneClientScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class EyePhoneScreen<T extends ClientScreenHandler> extends BaseScreen<T> {
    public static final TextureSetting BG_TEXTURE = new TextureSetting(
            new Identifier(EyePhoneMod.NAMESPACE, "textures/gui/eyepod_gui.png"),
            128, 128
    );

    final EyePhoneClientScreenHandler handler;

    @Nullable
    private TextureSetting customBackground = null;

    public EyePhoneScreen(@NotNull T handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
        super(handler, inventory, title);
        if (!(handler instanceof EyePhoneClientScreenHandler))
            throw new RuntimeException("Handler must be an EyePhoneClientScreenHandler type!");
        this.handler = (EyePhoneClientScreenHandler) handler;
        this.backgroundWidth = 88;
        this.backgroundHeight = 118;
        setWidgets();
    }

    private void setWidgets() {
        final CenteredPanel panel = new CenteredPanel(backgroundWidth, backgroundHeight);
        final TextField phoneNameTextField = new TextField(50, 15, handler::updatePhoneName, 10, 50);
        handler.setPhoneUpdateListener((name, identifier) -> {
            phoneNameTextField.write(name);
            updateBackgroundIdentifier(identifier);
        });
        panel.addChild(phoneNameTextField);
        parents.add(panel);
    }

    private void updateBackgroundIdentifier(String identifier) {
        final String[] split = identifier.split(":", 2);
        customBackground = new TextureSetting(
                new Identifier(split[0], split[1]),
                88, 118
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return parents.stream().anyMatch(p -> p.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return parents.stream().anyMatch(p -> p.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (shouldCloseOnEsc() && keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onClose();
            Optional.ofNullable(MinecraftClient.getInstance().player).ifPresent(ClientPlayerEntity::closeHandledScreen);
            return true;
        }
        return parents.stream().anyMatch(p -> p.keyPressed(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return parents.stream().anyMatch(p -> p.charTyped(chr, keyCode));
    }

    @Override
    TextureSetting getTexture() {
        return customBackground;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        DrawableElement.coloredRect(0,
                0,
                MinecraftClient.getInstance().getWindow().getScaledWidth(),
                MinecraftClient.getInstance().getWindow().getScaledHeight(),
                0x80808080
        );

        if (customBackground == null) return;
        super.drawBackground(matrices, delta, mouseX, mouseY);
        final TextureSetting texture = BG_TEXTURE;
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture.textureId);
        drawTexture(
                matrices,
                x,
                y,
                getZOffset(),
                (float) texture.offsetX,
                (float) texture.offsetY,
                backgroundWidth,
                backgroundHeight,
                texture.height,
                texture.width
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (customBackground == null) return;
        super.render(matrices, mouseX, mouseY, delta);
    }
}
