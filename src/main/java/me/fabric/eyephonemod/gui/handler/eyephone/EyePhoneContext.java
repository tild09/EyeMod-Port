package me.fabric.eyephonemod.gui.handler.eyephone;

import com.google.common.base.CaseFormat;
import me.fabric.eyephonemod.EyePhoneMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class EyePhoneContext {
    public String name;
    public String backgroundIdentifier;
    private static final EyePhoneContext DEF_CONTEXT = new EyePhoneContext("EyePhone", EyePhoneMod.NAMESPACE + ":textures/background/default.png");

    private EyePhoneContext(String name, String backgroundIdentifier) {
        this.name = name;
        this.backgroundIdentifier = backgroundIdentifier;
    }

    public EyePhoneContext(CompoundTag itemStackTag) {
        if (!itemStackTag.contains("phoneInfo")) {
            this.name = DEF_CONTEXT.name;
            this.backgroundIdentifier = DEF_CONTEXT.backgroundIdentifier;
        } else {
            final CompoundTag phoneInfo = itemStackTag.getCompound("phoneInfo");
            this.name = getStringOrDefault(phoneInfo, Attr.NAME.camelCase, DEF_CONTEXT.name);
            this.backgroundIdentifier = getStringOrDefault(phoneInfo, Attr.BACKGROUND_ID.camelCase, DEF_CONTEXT.backgroundIdentifier);
        }
    }

    public static String getStringOrDefault(CompoundTag tag, String path, String def) {
        if (!tag.contains(path)) return def;
        return tag.getString(path);
    }

    public void applyChanges(ItemStack phone) {
        final CompoundTag phoneInfo = phone.getOrCreateSubTag("phoneInfo");
        for (Attr value : Attr.values()) {
            value.putInTag(phoneInfo, this);
        }
        phone.putSubTag("phoneInfo", phoneInfo);
    }

    public enum Attr {
        NAME {
            public void setToContext(EyePhoneContext context, String value) {
                context.name = value;
            }

            public void putInTag(CompoundTag tag, EyePhoneContext context) {
                tag.putString(camelCase, context.name);
            }
        },

        BACKGROUND_ID {
            public void setToContext(EyePhoneContext context, String value) {
                final String[] split = value.split(":", 2);
                context.backgroundIdentifier = new Identifier(split[0], split[1]).toString();
            }

            public void putInTag(CompoundTag tag, EyePhoneContext context) {
                tag.putString(camelCase, context.backgroundIdentifier);
            }
        };

        public final String camelCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());

        public abstract void setToContext(EyePhoneContext context, String value);

        public abstract void putInTag(CompoundTag tag, EyePhoneContext context);

        public static Attr valueOfLowerCamel(String name) {
            for (Attr value : values()) {
                if (value.camelCase.equals(name)) return value;
            }
            throw new IllegalArgumentException("Cannot find " + name);
        }
    }

    public void updateTag(String key, String value) {
        final Attr attrTarget = Attr.valueOfLowerCamel(key);
        attrTarget.setToContext(this, value);
    }
}
