package me.fabric.eyephonemod.gui.client.element;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Panel implements ParentElement, Drawable {

    final ArrayList<DrawableElement> children = new ArrayList<>();
    boolean dragging = false;
    @Nullable Element focused;
    final int height;
    final int width;

    int drawX = 0;
    int drawY = 0;
    int parentWidth;
    int parentHeight;
    private boolean isVisible = true;

    public Panel(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public void init(int parentWidth, int parentHeight) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        configureChildPositions();
    }

    public abstract void configureChildPositions();

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public @Nullable Element getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        this.focused = focused;
    }

    public void addChild(DrawableElement child) {
        children.add(child);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (isInvisible()) return;
        children.forEach(e -> e.render(matrices, mouseX, mouseY, delta));
    }

    public void queryMouseOver(double mouseX, double mouseY) {
        if (isInvisible()) return;
        children.forEach(c -> {
            if (c.isMouseOver(mouseX, mouseY)) c.mouseOver(mouseX, mouseY);
        });
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isInvisible()) return false;
        return children.stream().anyMatch(c -> {
            final boolean mouseOver = c.isMouseOver(mouseX, mouseY);
            if (mouseOver) c.mouseReleased(mouseX, mouseY, button);
            c.setFocused(mouseOver);
            return mouseOver;
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInvisible()) return false;
        return children.stream().anyMatch(c -> c.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isInvisible()) return false;
        return children.stream().anyMatch(c -> c.keyPressed(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (isInvisible()) return false;
        return children.stream().anyMatch(c -> c.keyReleased(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        if (isInvisible()) return false;
        return children.stream().anyMatch(c -> c.charTyped(chr, keyCode));
    }

    public boolean isInvisible() {
        return !isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
