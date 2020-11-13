package me.fabric.eyephonemod.gui.handler.eyephone;

import com.google.common.base.CaseFormat;
import me.fabric.eyephonemod.EyePhoneMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class EyePhoneContext {
    public String name;
    public String backgroundIdentifier;
    public String password;
    private static final EyePhoneContext DEF_CONTEXT = new EyePhoneContext("EyePhone", EyePhoneMod.NAMESPACE + ":textures/background/default.png", "");
    public static final String PHONE_INFO = "phoneInfo";

    private EyePhoneContext(String name, String backgroundIdentifier, String password) {
        this.name = name;
        this.backgroundIdentifier = backgroundIdentifier;
        this.password = password;
    }

    public EyePhoneContext(CompoundTag itemStackTag) {
        if (!itemStackTag.contains(PHONE_INFO)) {
            this.name = DEF_CONTEXT.name;
            this.backgroundIdentifier = DEF_CONTEXT.backgroundIdentifier;
            this.password = DEF_CONTEXT.password;
        } else {
            final CompoundTag phoneInfo = itemStackTag.getCompound(PHONE_INFO);
            this.name = getStringOrDefault(phoneInfo, Attr.NAME.camelCase, DEF_CONTEXT.name);
            this.backgroundIdentifier = getStringOrDefault(phoneInfo, Attr.BACKGROUND_ID.camelCase, DEF_CONTEXT.backgroundIdentifier);
            this.password = getStringOrDefault(phoneInfo, Attr.PASSWORD.camelCase, DEF_CONTEXT.password);
        }
    }

    public static String getStringOrDefault(CompoundTag tag, String path, String def) {
        if (!tag.contains(path)) return def;
        return tag.getString(path);
    }

    public void applyChanges(ItemStack phone) {
        final CompoundTag phoneInfo = phone.getOrCreateSubTag(PHONE_INFO);
        for (Attr value : Attr.values()) {
            value.putInTag(phoneInfo, this);
        }
        phone.putSubTag(PHONE_INFO, phoneInfo);
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
        },

        PASSWORD {
            public void setToContext(EyePhoneContext context, String value) {
                context.password = value;
            }

            public void putInTag(CompoundTag tag, EyePhoneContext context) {
                tag.putString(camelCase, context.password);
            }
        };

        public final String camelCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());

        public abstract void setToContext(EyePhoneContext context, String value);

        public abstract void putInTag(CompoundTag tag, EyePhoneContext context);

    }

    public void updateTag(Attr key, String value) {
        key.setToContext(this, value);
    }
}
