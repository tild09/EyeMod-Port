package me.fabric.eyephonemod.gui.client;

import me.fabric.eyephonemod.EyePhoneMod;
import me.fabric.eyephonemod.gui.handler.ClientScreenHandler;
import me.fabric.eyephonemod.gui.handler.eyephone.EyePhoneClientScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class EyePhoneScreen<T extends ClientScreenHandler> extends BaseScreen<T> {
    public static final TextureSetting BG_TEXTURE = new TextureSetting(
            new Identifier(EyePhoneMod.NAMESPACE, "textures/background/default.png"),
            512, 512
    );

    final EyePhoneClientScreenHandler handler;

    public EyePhoneScreen(@NotNull T handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
        super(handler, inventory, title);
        if (!(handler instanceof EyePhoneClientScreenHandler))
            throw new RuntimeException("Handler must be a DummyClientScreenHandler type!");
        this.handler = (EyePhoneClientScreenHandler) handler;
        this.backgroundWidth = 201;
        this.backgroundHeight = 256;
    }

    @Override
    TextureSetting getTexture() {
        return BG_TEXTURE;
    }
}
