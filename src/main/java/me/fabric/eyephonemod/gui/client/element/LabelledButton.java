package me.fabric.eyephonemod.gui.client.element;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class LabelledButton extends ButtonWidget implements DrawableElement {
    private final int x;
    private final int y;
    private int parentX = 0;
    private int parentY = 0;

    public LabelledButton(int x, int y, int width, Text message, PressAction onPress) {
        super(x, y, width, 20, message, onPress);
        this.x = x;
        this.y = y;
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
    public void setParentX(int parentX) {
        this.parentX = parentX;
        super.x = parentX + this.x;
    }

    @Override
    public int getParentY() {
        return parentY;
    }

    @Override
    public void setParentY(int parentY) {
        this.parentY = parentY;
        super.y = parentY + this.y;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        return super.mouseClicked(mouseX, mouseY, button);
    }
}

