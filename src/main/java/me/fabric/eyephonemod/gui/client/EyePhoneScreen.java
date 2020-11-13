package me.fabric.eyephonemod.gui.client;

import com.google.common.collect.Lists;
import me.fabric.eyephonemod.EyePhoneMod;
import me.fabric.eyephonemod.gui.client.element.BottomRightAnchoredPanel;
import me.fabric.eyephonemod.gui.client.util.AnimationKeyframePlayer;
import me.fabric.eyephonemod.gui.client.util.TextureSetting;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.eyephone.EyePhoneClientScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import static me.fabric.eyephonemod.gui.client.util.PanelMaker.*;

public class EyePhoneScreen<T extends ClientScreenHandler> extends BaseScreen<T> {
    public static final TextureSetting BG_TEXTURE = new TextureSetting(
            new Identifier(EyePhoneMod.NAMESPACE, "textures/gui/eyepod_gui.png"),
            SIZE, SIZE
    );
    static final Identifier ID = new Identifier(EyePhoneMod.NAMESPACE, "textures/gui/eye_apps.png");

    public final EyePhoneClientScreenHandler handler;
    final AnimationKeyframePlayer<Integer> onShowAnimationPlayer = new AnimationKeyframePlayer<>(
            0, 8,
            (d, f, t) -> (int) ((t - f) * d + f),
            20,
            AnimationKeyframePlayer.Ease.QUAD_OUT,
            AnimationKeyframePlayer.Type.PERSISTENT
    );

    final BottomRightAnchoredPanel appsPanel = newPanel(true);
    final BottomRightAnchoredPanel settingsPanel = newPanel(false);
    final ArrayList<BottomRightAnchoredPanel> addPanels = Lists.newArrayList(appsPanel, settingsPanel);
    final HashMap<Apps, BottomRightAnchoredPanel> appsMap = new HashMap<>();

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
        this.handler.setPhoneBgUpdateListener(this::updateBackgroundIdentifier);
    }

    private void setWidgets() {
        parents.add(appsPanel);
        parents.add(settingsPanel);
        configureSettingsPanel(this, settingsPanel);
        configureAppsPanel(this, appsPanel, Lists.newArrayList(Apps.SETTINGS));
        appsMap.put(Apps.SETTINGS, settingsPanel);
    }

    private void updateBackgroundIdentifier(String identifier) {
        final String[] split = identifier.split(":", 2);
        customBackground = new TextureSetting(
                new Identifier(split[0], split[1]),
                SIZE, SIZE
        );
    }

    public void requestChangePanel(@Nullable Apps app) {
        addPanels.forEach(p -> p.setVisible(false));
        if (app == null) appsPanel.setVisible(true);
        else {
            final BottomRightAnchoredPanel requestPanel = appsMap.getOrDefault(app, null);
            if (requestPanel != null) requestPanel.setVisible(true);
        }
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
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (customBackground == null) return;
        updateYPlayer();
        parents.forEach(p -> p.setBottomPadding(PADDING - 8 + currentY));
        super.render(matrices, mouseX, mouseY, delta);

        final MinecraftClient mc = MinecraftClient.getInstance();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(0, 0, mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
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
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void updateYPlayer() {
        currentY = onShowAnimationPlayer.next();
    }

    public enum Apps {
        STORE, SETTINGS, INFO, ROUND, EMAIL, CHAT, MUSIC, NOTES, TNT, PERSON,
        AIR, HEART, WEATHER, WORK, HEALTH, EGG, PET, WRITE, CRYSTAL, CONTACT,
        WIFI;

        public static final int BTN_SIZE = 24;
        private static final int TEXTURE_SIZE = 256;
        private static final int COLS = 10;

        public final TextureSetting texture;
        public final String name = WordUtils.capitalize(StringUtils.join(name().toLowerCase().split("_"), ' '));

        Apps() {
            final int offsetX = Math.floorMod(ordinal(), COLS) * BTN_SIZE;
            final int offsetY = Math.floorDiv(ordinal(), COLS) * BTN_SIZE;
            texture = new TextureSetting(ID, TEXTURE_SIZE, TEXTURE_SIZE, offsetX, offsetY);
        }
    }
}
