package me.fabric.eyephonemod.gui.client;

import me.fabric.eyephonemod.EyePhoneMod;
import me.fabric.eyephonemod.gui.client.util.TextureSetting;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.dummy.DummyClientScreenHandler;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class DummyScreen<T extends ClientScreenHandler> extends BaseScreen<T> implements ScreenHandlerProvider<T> {
    public final DummyClientScreenHandler handler;
    public static final TextureSetting BG_TEXTURE = new TextureSetting(
            new Identifier(EyePhoneMod.NAMESPACE, "gui/dummy_bg.png")
    );
    public DummyScreen(@NotNull T handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
        super(handler, inventory, title);
        if (!(handler instanceof DummyClientScreenHandler)) throw new RuntimeException("Handler must be a DummyClientScreenHandler type!");
        this.handler = (DummyClientScreenHandler) handler;
        this.backgroundWidth = 248;
        this.backgroundHeight = 166;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        handler.onClick(mouseX, mouseY);
        return true;
    }

    @Override
    TextureSetting getTexture() {
        return BG_TEXTURE;
    }
}
