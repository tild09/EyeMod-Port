package me.fabric.eyephonemod.gui.client.element;

import net.minecraft.client.util.math.MatrixStack;

public class TextField implements DrawableElement {
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int parentDrawX, int parentDrawY) {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        render(matrices, mouseX, mouseY, delta, 0, 0);
    }
}
