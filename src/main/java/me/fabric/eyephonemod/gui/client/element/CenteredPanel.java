package me.fabric.eyephonemod.gui.client.element;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CenteredPanel implements ParentElement, Drawable {

    final ArrayList<DrawableElement> children = new ArrayList<>();
    boolean dragging = false;
    @Nullable Element focused;
    final int height;
    final int width;

    int drawX = 0;
    int drawY = 0;

    public CenteredPanel(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public void init(int parentWidth, int parentHeight) {
        drawX = (parentHeight - height) / 2;
        drawY = (parentWidth - width) / 2;
    }

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
        children.forEach(e -> e.render(matrices, mouseX, mouseY, delta, drawX, drawY));
    }

    public void queryMouseOver(double mouseX, double mouseY) {
        children.forEach(c -> {
            if (c.isMouseOver(mouseX, mouseY)) c.mouseOver(mouseX, mouseY);
        });
    }
}
