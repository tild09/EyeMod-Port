package me.fabric.eyephonemod.gui.client.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Label implements DrawableElement {
    private final String label;
    private final int x;
    private final int y;
    private int parentX = 0;
    private int parentY = 0;

    private int color = 0xFFFFFFFF;

    public Label(String label, int x, int y) {
        this.label = label;
        this.x = x;
        this.y = y;
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
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setFocused(boolean focused) {
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, label, parentX + x, parentY + y, color);
    }

    public void setColor(int color) {
        this.color = color;
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
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(label);
    }

    @Override
    public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    @Override
    public int getParentX() {
        return parentX;
    }

    @Override
    public int getParentY() {
        return parentY;
    }
}
