package me.fabric.eyephonemod.gui.client;

import me.fabric.eyephonemod.EyePhoneMod;
import me.fabric.eyephonemod.gui.client.element.BottomRightAnchoredPanel;
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
    static final int PADDING = 20;
    static final int SIZE = 256;
    public static final TextureSetting BG_TEXTURE = new TextureSetting(
            new Identifier(EyePhoneMod.NAMESPACE, "textures/gui/eyepod_gui.png"),
            SIZE, SIZE
    );
    static final int BG_WIDTH = 88*2;
    static final int BG_HEIGHT = 118*2;

    final EyePhoneClientScreenHandler handler;
    final AnimationKeyframePlayer<Integer> onShowAnimationPlayer = new AnimationKeyframePlayer<>(
            0, 8,
            (d, f, t) -> (int)((t - f) * d + f),
            20,
            AnimationKeyframePlayer.Ease.QUAD_OUT,
            AnimationKeyframePlayer.Type.PERSISTENT
    );

    @Nullable
    private TextureSetting customBackground = null;
    private int currentY = 0;

    public EyePhoneScreen(@NotNull T handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
        super(handler, inventory, title);
        if (!(handler instanceof EyePhoneClientScreenHandler))
            throw new RuntimeException("Handler must be an EyePhoneClientScreenHandler type!");
        this.handler = (EyePhoneClientScreenHandler) handler;
        this.backgroundWidth = BG_WIDTH;
        this.backgroundHeight = BG_HEIGHT;
        setWidgets();
    }

    private void setWidgets() {
        final BottomRightAnchoredPanel panel = new BottomRightAnchoredPanel(backgroundWidth, backgroundHeight, 20, 20);
        final TextField phoneNameTextField = new TextField(50, 15, handler::updatePhoneName, 10, 50);
        handler.setPhoneNameUpdateListener(phoneNameTextField::write);
        handler.setPhoneBgUpdateListener(this::updateBackgroundIdentifier);
        handler.setPhoneTypeUpdateListener(s -> System.out.println("Phone type is " + s));
        panel.addChild(phoneNameTextField);
        parents.add(panel);
    }

    private void updateBackgroundIdentifier(String identifier) {
        final String[] split = identifier.split(":", 2);
        customBackground = new TextureSetting(
                new Identifier(split[0], split[1]),
                SIZE, SIZE
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
        if (customBackground == null) return;

        MinecraftClient.getInstance().getTextureManager().bindTexture(customBackground.textureId);
        drawTexture(
                matrices,
                width - backgroundWidth - PADDING,
                height - backgroundHeight - PADDING - currentY,
                getZOffset(),
                (float) customBackground.offsetX,
                (float) customBackground.offsetY,
                backgroundWidth,
                backgroundHeight,
                customBackground.height,
                customBackground.width
        );

        final TextureSetting texture = BG_TEXTURE;
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture.textureId);
        drawTexture(
                matrices,
                width - backgroundWidth - PADDING,
                height - backgroundHeight - PADDING - currentY,
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
        updateYPlayer();
        parents.forEach(p -> p.setBottomPadding(PADDING - 8 + currentY));
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void updateYPlayer() {
        currentY = onShowAnimationPlayer.next();
    }
}
