package me.fabric.eyephonemod.gui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.fabric.eyephonemod.gui.client.element.CenteredPanel;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public abstract class BaseScreen<T extends ClientScreenHandler> extends HandledScreen<T> {

    ArrayList<CenteredPanel> parents = new ArrayList<>();

    public BaseScreen(@NotNull T handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void init(MinecraftClient client, int width, int height) {
        super.init(client, width, height);
        handler.confirmScreenOpenToServer();
        parents.forEach(e -> e.init(width, height));
    }

    @Override
    protected void init() {
        super.init();
    }

    public ClientScreenHandler getEyePhoneScreenHandler() {
        return handler;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final TextureSetting texture = getTexture();
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture.textureId);
        drawTexture(
                matrices,
                x,
                y,
                getZOffset(),
                (float) texture.offsetX,
                (float) texture.offsetY,
                backgroundWidth,
                backgroundHeight,
                texture.height,
                texture.width
        );
    }

    abstract TextureSetting getTexture();

    @SuppressWarnings("deprecation")
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawBackground(matrices, delta, mouseX, mouseY);
        parents.forEach(p -> p.queryMouseOver(mouseX, mouseY));
        final MinecraftClient mc = MinecraftClient.getInstance();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(0, 0, mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
        parents.forEach(p -> p.render(matrices, mouseX, mouseY, delta));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        DiffuseLighting.disable();
        // Needed because super.render leaves dirty state
        // draw cursor item stack

        final ItemStack cursorStack = playerInventory.getCursorStack();
        if (!cursorStack.isEmpty()) {
            RenderSystem.translatef(0.0f, 0.0f, 32.0f);
            setZOffset(200); // ? questionable
            itemRenderer.zOffset = 200.0f;
            itemRenderer.renderInGuiWithOverrides(cursorStack, mouseX - 9, mouseY - 9);
            itemRenderer.renderGuiItemOverlay(
                    textRenderer,
                    cursorStack,
                    mouseX - 9,
                    mouseY - 9,
                    cursorStack.getCount() > 1 ? String.format("%d", cursorStack.getCount()) : null
            );
            setZOffset(0); // ? questionable
            itemRenderer.zOffset = 0.0f;
        }
        DiffuseLighting.enable();
//        renderTooltip(matrices, mouseX, mouseY)
    }
}
