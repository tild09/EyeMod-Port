package me.fabric.eyephonemod.gui.client.element;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public interface DrawableElement extends Element, Drawable {

    default void mouseOver(double mouseX, double mouseY) {
    }

    static void coloredRect(int left, int top, int width, int height, int color) {
        int fixedWidth = width;
        int fixedHeight = height;
        if (fixedWidth <= 0) fixedWidth = 1;
        if (fixedHeight <= 0) fixedHeight = 1;
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.SRC_ALPHA,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SrcFactor.ONE,
                GlStateManager.DstFactor.ZERO
        );
        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(left, top + fixedHeight, 0.0).color(r, g, b, a).next();
        buffer.vertex(left + fixedWidth, top + fixedHeight, 0.0).color(r, g, b, a).next();
        buffer.vertex(left + fixedWidth, top, 0.0).color(r, g, b, a).next();
        buffer.vertex(left, top, 0.0).color(r, g, b, a).next();
        tessellator.draw();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @SuppressWarnings("deprecation")
    static void invertedRect(int x, int y, int width, int height) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buf = tessellator.getBuffer();
        RenderSystem.color4f(0.0f, 0.0f, 255.0f, 255.0f);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        buf.begin(GL11.GL_QUADS, VertexFormats.POSITION);
        buf.vertex(x, y + height, 0.0).next();
        buf.vertex(x + width, y + height, 0.0).next();
        buf.vertex(x + width, y, 0.0).next();
        buf.vertex(x, y, 0.0).next();
        tessellator.draw();
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }

    int getParentX();

    int getParentY();

    void setParentX(int parentX);

    void setParentY(int parentY);

    boolean isFocused();

    void setFocused(boolean focused);
}
