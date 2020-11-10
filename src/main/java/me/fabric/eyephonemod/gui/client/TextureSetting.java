package me.fabric.eyephonemod.gui.client;

import net.minecraft.util.Identifier;

public class TextureSetting {
    public final Identifier textureId;
    public final int width;
    public final int height;
    public final int offsetX;
    public final int offsetY;

    public TextureSetting(Identifier textureId, int width, int height, int offsetX, int offsetY) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public TextureSetting(Identifier textureId, int width, int height) {
        this(textureId, width, height, 0, 0);
    }

    public TextureSetting(Identifier textureId) {
        this(textureId, 256, 256, 0, 0);
    }
}
