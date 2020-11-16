package me.fabric.eyephonemod.gui.client.util;

import me.fabric.eyephonemod.EyePhoneMod;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public enum EyePhoneBackgrounds {
    DEFAULT(false),
    FROZEN(false),
    MESA(false),
    NETHER(false),
    WOLF(false),
    BLACK, BLUE, BROWN, CYAN, GRAY, GREEN,
    LIGHT_BLUE, LIGHT_GRAY, LIME, MAGENTA,
    ORANGE, PINK, PURPLE, RED, WHITE, YELLOW;

    public final TextureSetting texture;
    public final String name = WordUtils.capitalize(StringUtils.join(name().split("_"), " "));

    EyePhoneBackgrounds(boolean isColor) {
        texture = new TextureSetting(
                new Identifier(EyePhoneMod.NAMESPACE, makePath(isColor, name().toLowerCase())),
                256, 256
        );
    }

    EyePhoneBackgrounds() {
        this(true);
    }

    private static String makePath(boolean isColor, String name) {
        final String COLOR_DIR = "textures/background/color/";
        final String BG_DIR = "textures/background/";
        return (isColor ? COLOR_DIR : BG_DIR) + name;
    }
}
