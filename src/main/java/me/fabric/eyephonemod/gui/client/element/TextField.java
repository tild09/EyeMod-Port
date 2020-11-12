package me.fabric.eyephonemod.gui.client.element;

import com.google.common.base.Preconditions;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class TextField implements DrawableElement {

    private boolean focused = false;

    private int cursor = 0;
    private int visibleLower = 0;
    private int visibleUpper = 0;
    private boolean hasSelection = false;
    private int selection = 0;

    private final int caretMaxTick = 15;
    private int caretTick = caretMaxTick;
    private int caretColor = 0xFFFFFFFF;
    private boolean drawCaret = false;

    private String text = "";
    private int maxLength = 10;
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final Keyboard keyboard = MinecraftClient.getInstance().keyboard;

    private final Consumer<String> textListener;
    private final int width;
    private final int height;
    private final int x;
    private final int y;
    private int parentX = 0;
    private int parentY = 0;

    private int textColor = 0xFFFFFFFF;
    private int focusedBgColor = 0xFF000000;
    private int outFocusedBgColor = 0xFF222222;

    public TextField(int width, Consumer<String> textListener, int x, int y) {
        this.width = width;
        this.height = 15;
        this.textListener = textListener;
        this.x = x;
        this.y = y;
    }

    public void render(MatrixStack matrices) {
        validateCursor();
        final int absX = x + parentX + 2;
        final int absY = y + parentY + 4;

        drawBackground();

        if (text.length() > 0) textRenderer.drawWithShadow(
                matrices,
                text.substring(visibleLower, visibleUpper),
                (float) absX,
                (float) absY,
                textColor
        );

        if (!focused) return;
        if (--caretTick < 0) {
            caretTick = caretMaxTick;
            drawCaret = !drawCaret;
        }

        final int caretX = textRenderer.getWidth(text.substring(visibleLower, cursor));
        if (drawCaret) {
            DrawableElement.coloredRect(
                    caretX + absX,
                    absY - 1,
                    1,
                    textRenderer.fontHeight + 1,
                    caretColor
            );
            DrawableElement.coloredRect(
                    caretX + absX + 1,
                    absY - 1 + 1,
                    1,
                    textRenderer.fontHeight + 1,
                    0x80808080
            );
        }

        if (hasSelection) {
            int drawX;
            int drawWidth;
            if (selection < cursor) {
                int drawIndex = Math.max(selection, visibleLower);
                drawX = textRenderer.getWidth(text.substring(visibleLower, drawIndex));
                drawWidth = textRenderer.getWidth(text.substring(drawIndex, cursor));
            } else {
                int drawIndex = Math.min(selection, visibleUpper);
                drawX = textRenderer.getWidth(text.substring(visibleLower, cursor));
                drawWidth = textRenderer.getWidth(text.substring(cursor, drawIndex));
            }
            DrawableElement.invertedRect(absX + drawX, absY - 1, drawWidth, textRenderer.fontHeight + 1);
        }
    }

    private void sendTextUpdate() {
        textListener.accept(text);
    }

    private void drawBackground() {
        // background
        DrawableElement.coloredRect(
                parentX + x - 1,
                parentY + y - 1,
                width + 2,
                height + 2,
                0xFF808080
        );

        // top border
        DrawableElement.coloredRect(
                parentX + x - 1,
                parentY + y - 1,
                width + 2,
                1,
                0x80303030
        );

        // left border
        DrawableElement.coloredRect(
                parentX + x - 1,
                parentY + y,
                1,
                height + 1,
                0x80303030
        );

        // bottom border
        DrawableElement.coloredRect(
                parentX + x - 1,
                parentY + y + height,
                width + 2,
                1,
                0x80FFFFFF
        );

        // right border
        DrawableElement.coloredRect(
                parentX + x + width,
                parentY + y - 1,
                1,
                height + 2,
                0x80FFFFFF
        );

        DrawableElement.coloredRect(
                parentX + x,
                parentY + y,
                width,
                height,
                isFocused() ? focusedBgColor : outFocusedBgColor
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        render(matrices);
    }

    private void validateCursor() {
        cursor = Math.min(cursor, text.length());
        cursor = Math.max(cursor, 0);

        if (visibleLower == visibleUpper || visibleLower < 0 || visibleUpper > text.length()) {
            // try to get new visible index
            visibleUpper = cursor;
            visibleLower = (cursor - textRenderer.trimToWidth(
                    text,
                    width,
                    true
            ).length());
            visibleLower = Math.max(visibleLower, 0);
        }

        visibleUpper = visibleLower + textRenderer.trimToWidth(text.substring(visibleLower), width).length();

        if (cursor < visibleLower) {
            visibleLower = cursor;
            visibleUpper = visibleLower + textRenderer.trimToWidth(text.substring(cursor), width).length();
        }

        if (cursor > visibleUpper) {
            visibleUpper = cursor;
            visibleLower = cursor - textRenderer.trimToWidth(text, width, true).length();
        }
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
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isFocused()) return false;

        if (Screen.hasShiftDown() && !hasSelection) {
            pinSelection();
        }

        if (Screen.isSelectAll(keyCode)) {
            selectAll();
        } else if (Screen.isCopy(keyCode)) {
            keyboard.setClipboard(getSelectedText());
        } else if (Screen.isPaste(keyCode)) {
            write(keyboard.getClipboard());
        } else if (Screen.isCut(keyCode)) {
            keyboard.setClipboard(getSelectedText());
            erase(false);
        } else {
            switch (keyCode) {
                case GLFW.GLFW_KEY_ESCAPE:
                    setFocused(false);
                    break;

                case GLFW.GLFW_KEY_BACKSPACE:
                    erase(false);
                    break;
                case GLFW.GLFW_KEY_DELETE:
                    erase(true);
                    break;

                case GLFW.GLFW_KEY_RIGHT:
                    if (hasSelection && !Screen.hasShiftDown()) releaseSelection();
                    else moveCursor(true, Screen.hasControlDown());
                    break;

                case GLFW.GLFW_KEY_LEFT:
                    if (hasSelection && !Screen.hasShiftDown()) releaseSelection();
                    else moveCursor(false, Screen.hasControlDown());
                    break;

                case GLFW.GLFW_KEY_HOME:
                    setCursorToStart();
                    if (!Screen.hasShiftDown()) releaseSelection();
                    break;

                case GLFW.GLFW_KEY_END:
                    setCursorToEnd();
                    if (!Screen.hasShiftDown()) releaseSelection();
                    break;
            }
        }
        sendTextUpdate();
        return true;
    }

    private void moveCursor(boolean forward, boolean skipWord) {
        if (forward) {
            if (skipWord) {
                final int newCursor = text.indexOf(' ', cursor);
                if (newCursor == -1) {
                    setCursorToEnd();
                } else {
                    cursor = Math.min(newCursor + 1, text.length());
                }
            } else {
                cursor = Math.min(cursor + 1, text.length());
            }
        } else {
            if (skipWord) {
                final int newCursor =
                        StringUtils.reverse(text.substring(0, cursor)).indexOf(' ');
                if (newCursor == -1) {
                    setCursorToStart();
                } else {
                    cursor = cursor - newCursor - 1;
                }
            } else {
                cursor = Math.max(cursor - 1, 0);
            }
        }
    }

    private void setCursorToStart() {
        cursor = 0;
    }

    private void setCursorToEnd() {
        cursor = text.length();
    }

    private void erase(boolean forward) {
        if (hasSelection) {
            final int lower = Math.min(cursor, selection);
            final int upper = Math.max(cursor, selection);
            text = text.substring(0, lower) + text.substring(upper);
            releaseSelection();
        } else if (forward) {
            if (cursor >= text.length()) return;  // cant delete if it reached the end
            text = text.substring(0, cursor) + text.substring(cursor + 1);
        } else {
            if (cursor <= 0) return;  // cant delete if it reached the start
            text = text.substring(0, cursor - 1) + text.substring(cursor);
            cursor = Math.max(cursor - 1, 0);
        }
    }

    private void releaseSelection() {
        hasSelection = false;
    }

    public void write(String clipboard) {
        for (char c : clipboard.toCharArray()) {
            write(c);
        }
    }

    private void write(char c) {
        int lower;
        int upper;
        if (hasSelection) {
            lower = Math.min(cursor, selection);
            upper = Math.max(cursor, selection);
        } else {
            upper = cursor;
            lower = upper;
        }

        text = text.substring(0, lower) + c + text.substring(upper);
        cursor = Math.min(cursor + 1, text.length());
        releaseSelection();
    }

    private String getSelectedText() {
        if (!hasSelection) return "";
        Preconditions.checkState(
                selection != cursor, "Error in programming! Is selecting but cursor is the same as selection"
        );
        final int lower = Math.min(selection, cursor);
        final int upper = Math.max(selection, cursor);
        return text.substring(lower, upper);
    }

    private void selectAll() {
        cursor = 0;
        pinSelection();
        cursor = text.length();
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        if (!isFocused() || hasReachMaxLength()) return false;
        write(chr);
        sendTextUpdate();
        return true;
    }

    private void pinSelection() {
        selection = cursor;
        hasSelection = true;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean hasReachMaxLength() {
        return text.length() >= getMaxLength();
    }

    public int getCaretColor() {
        return caretColor;
    }

    public void setCaretColor(int caretColor) {
        this.caretColor = caretColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getFocusedBgColor() {
        return focusedBgColor;
    }

    public void setFocusedBgColor(int focusedBgColor) {
        this.focusedBgColor = focusedBgColor;
    }

    public int getOutFocusedBgColor() {
        return outFocusedBgColor;
    }

    public void setOutFocusedBgColor(int outFocusedBgColor) {
        this.outFocusedBgColor = outFocusedBgColor;
    }

    /**
     * Checks if the mouse position is within the bound
     * of the element.
     *
     * @param mouseX the X coordinate of the mouse
     * @param mouseY the Y coordinate of the mouse
     * @return {@code true} if the mouse is within the bound of the element, otherwise {@code false}
     */
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        mouseX -= parentX + x;
        mouseY -= parentY + y;
        return mouseX > 0 && width > mouseX &&
                mouseY > 0 && height > mouseY;
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

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
