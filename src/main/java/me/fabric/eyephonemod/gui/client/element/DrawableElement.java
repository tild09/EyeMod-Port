package me.fabric.eyephonemod.gui.client.element;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;

public interface DrawableElement extends Element, Drawable {

    void render(MatrixStack matrices,
                int mouseX,
                int mouseY,
                float delta,
                int parentDrawX,
                int parentDrawY);

    default void mouseOver(double mouseX, double mouseY) {
    }
}
