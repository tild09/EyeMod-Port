package me.fabric.eyephonemod.gui.client.element;

import me.fabric.eyephonemod.gui.client.TextureSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class TexturedButton implements DrawableElement {
    private final TextureSetting texture;
    private final int width;
    private final int height;
    private final int x;
    private final int y;
    private int parentX = 0;
    private int parentY = 0;
    private Runnable onClickCallback = () -> {
    };

    public TexturedButton(TextureSetting texture, int width, int height, int x, int y) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (isMouseOver(mouseX, mouseY)) drawOutline();
        final int drawX = x + parentX;
        final int drawY = y + parentY;
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture.textureId);
        DrawableHelper.drawTexture(matrices, drawX, drawY, texture.offsetX, texture.offsetY, width, height, texture.width, texture.height);
    }

    private void drawOutline() {
        final int drawX = x + parentX - 1;
        final int drawY = y + parentY - 1;
        DrawableElement.coloredRect(drawX, drawY, width + 2, height + 2, 0xFFFFFF);
    }

    @Override
    public void setParentX(int parentX) {
        this.parentX = parentX;
    }

    @Override
    public void setParentY(int parentY) {
        this.parentY = parentY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getParentX() {
        return parentX;
    }

    @Override
    public int getParentY() {
        return parentY;
    }

    public void setOnClickCallback(Runnable onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        onClickCallback.run();
        return true;
    }
}
